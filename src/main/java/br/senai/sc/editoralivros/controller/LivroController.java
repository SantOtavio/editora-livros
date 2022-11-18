package br.senai.sc.editoralivros.controller;

import br.senai.sc.editoralivros.DTO.LivroDTO;
import br.senai.sc.editoralivros.model.entities.Arquivo;
import br.senai.sc.editoralivros.model.entities.Autor;
import br.senai.sc.editoralivros.model.entities.Livro;
import br.senai.sc.editoralivros.model.entities.Status;
import br.senai.sc.editoralivros.model.service.LivroService;
import br.senai.sc.editoralivros.model.service.PessoaService;
import br.senai.sc.editoralivros.util.LivroUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/editoraLivros")
public class LivroController {
    LivroService service;

    @PostMapping("/livro")
    public ResponseEntity<Object> save(
            @RequestParam("livro") String livroIsbn,
            @RequestParam("arquivo") MultipartFile file
    ) throws IOException {
        LivroUtil util = new LivroUtil();

        Livro livro = util.convertJsonToLivro(livroIsbn);

        if (service.findById(livro.getIsbn()).isPresent()) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("Livro já existe");
        }

        if (livro.getAutor() == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Autor não pode ser nulo");
        }

        if (livro.getTitulo() == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Titulo não pode ser nulo");
        }

        if (livro.getQtdPag() == null && livro.getQtdPag() < 0) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade de páginas não pode ser nula ou negativa");
        }

        livro.setStatus(Status.AGUARDANDO_REVISAO);
        livro.setArquivo(file);
        System.out.println(livro.getArquivo().getNome());
        System.out.println(livro.getArquivo().toString());
        service.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body("Livro criado com sucesso" + livro);
    }

    @GetMapping("/livro/{ISBN}")
    public ResponseEntity<Object> findById(@PathVariable(value = "ISBN") Long ISBN) {
        Optional<Livro> livroOptional = service.findById(ISBN);
        if (livroOptional.isPresent()) {
            return ResponseEntity.ok(livroOptional.get());
        } else {
            return ResponseEntity.status(404).body("Não foi encontrado livro com o ISBN " + ISBN);
        }
    }

    @GetMapping("/livro/status/{status}")
    public ResponseEntity<Object> findByStatus(@PathVariable(value = "status") Status status) {
        List<Livro> livros = service.findByStatus(status);
        if (livros.isEmpty()) {
            return ResponseEntity.status(404).body("Não foi encontrado livro com o status " + status);
        } else {
            return ResponseEntity.ok(livros);
        }
    }

    @GetMapping("/livro/autor/{autor}")
    public ResponseEntity<Object> findByAutor(@PathVariable(value = "autor") Autor autor) {
        List<Livro> livros = service.findByAutor(autor);
        if (livros.isEmpty()) {
            return ResponseEntity.status(404).body("Não foi encontrado livro com o autor " + autor);
        } else {
            return ResponseEntity.ok(livros);
        }
    }

    @GetMapping("/livro")
    public ResponseEntity<Object> findAll() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            return ResponseEntity.status(404).body("Não foi encontrado livro");
        } else {
            return ResponseEntity.ok(livros);
        }
    }

    @GetMapping("/livro/page")
    public ResponseEntity<Object> findAllPage(
            @PageableDefault(page = 0, size = 10)
            Pageable pageable
    ) {
        Page<Livro> livros = service.findAll(pageable);
        if (livros.isEmpty()) {
            return ResponseEntity.status(404).body("Não foi encontrado livro");
        } else {
            return ResponseEntity.ok(livros);
        }
    }

    @DeleteMapping("/livro/delete/{ISBN}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "ISBN") Long ISBN) {
        if (service.findById(ISBN).isPresent()) {
            service.deleteById(ISBN);
            return ResponseEntity.ok("Livro com ISBN " + ISBN + " foi deletado com sucesso");
        } else {
            return ResponseEntity.status(404).body("O livro não foi deletado pois não foi encontrado nenhum livro com o ISBN " + ISBN);
        }
    }

    @PutMapping("/livro/{ISBN}")
    public ResponseEntity<Object> update(@RequestBody @Valid LivroDTO livroDTO) {
        if (service.findById(livroDTO.getIsbn()).isPresent()) {
            Livro livro = service.findById(livroDTO.getIsbn()).get();
            BeanUtils.copyProperties(livroDTO, livro);
            service.save(livro);
            return ResponseEntity.ok("Livro com ISBN " + livro.getIsbn() + " foi atualizado com sucesso");
        } else {
            return ResponseEntity.status(404).body("O livro não foi atualizado pois não foi encontrado nenhum livro com o ISBN " + livroDTO.getIsbn());
        }
    }
}
