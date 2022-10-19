package br.senai.sc.editoralivros.controller;

import br.senai.sc.editoralivros.DTO.PessoaDTO;
import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.model.service.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/editoraLivros/pessoa")
public class PessoaController {
    PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    public List<Pessoa> findAll() {
        return service.findAll();
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll(Sort sort) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(sort));
    }

    public List<Pessoa> findAllById(Iterable<Long> longs) {
        return service.findAllById(longs);
    }

    public <S extends Pessoa> List<S> saveAll(Iterable<S> entities) {
        return service.saveAll(entities);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> findById(@PathVariable(value = "cpf") Long cpf) {
        Optional<Pessoa> pessoa = service.findById(cpf);
        if (pessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado pessoa com o cpf " + cpf);
        } else {
            return ResponseEntity.ok(pessoa.get());
        }
    }


    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "cpf") Long cpf) {
        if (service.existsById(cpf)) {
            service.deleteById(cpf);
            return ResponseEntity.status(HttpStatus.OK).body("Pessoa com o cpf " + cpf + " foi deletado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado pessoa com o cpf " + cpf);
        }
    }

    public void delete(Pessoa entity) {
        service.delete(entity);
    }

    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<Object> findByEmail(@PathVariable(value = "email") String email) {
        Optional<Pessoa> pessoaOptional = service.findByEmail(email);

        if (pessoaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pessoa com o email informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pessoaOptional.get());
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Object> update(@PathVariable(value = "cpf") Long cpf, @RequestBody @Valid PessoaDTO pessoaDTO) {
        Optional<Pessoa> pessoaOptional = service.findById(cpf);
        if (pessoaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado pessoa com o cpf " + cpf);
        }

        if (service.existsByEmail(pessoaDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe uma pessoa cadastrada com o email informado");
        }

        Pessoa pessoa = pessoaOptional.get();
        BeanUtils.copyProperties(pessoaDTO, pessoa);
        service.save(pessoa);
        return ResponseEntity.status(HttpStatus.OK).body(pessoa);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PessoaDTO pessoaDTO) {
        System.out.println(pessoaDTO);
        if(service.existsById(pessoaDTO.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma pessoa cadastrada com o cpf informado");
        }

        if (service.existsByEmail(pessoaDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma pessoa cadastrada com o email informado");
        }
        System.out.println("Depois do findByEmail");

        Pessoa pessoa = new Pessoa();
        BeanUtils.copyProperties(pessoaDTO, pessoa);
        service.save(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
    }
}
