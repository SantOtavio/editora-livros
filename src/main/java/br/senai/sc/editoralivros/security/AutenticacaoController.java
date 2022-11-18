package br.senai.sc.editoralivros.security;

import br.senai.sc.editoralivros.DTO.PessoaDTO;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/editoraLivros/login")
public class AutenticacaoController {

    @PostMapping
    public ResponseEntity<Object> autenticar(
            @RequestBody @Valid UsuarioDTO usuarioDTO
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), new BCryptPasswordEncoder().encode(usuarioDTO.getSenha()));
        if (authenticationToken.isAuthenticated()) {
            return ResponseEntity.ok().body(authenticationToken.getPrincipal());
        }
        return ResponseEntity.badRequest().body("Usuário ou senha inválidos");
    }

}
