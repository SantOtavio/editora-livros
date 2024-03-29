package br.senai.sc.editoralivros.security.controller;

import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.security.DTO.TokenDTO;
import br.senai.sc.editoralivros.security.DTO.UsuarioDTO;
import br.senai.sc.editoralivros.security.TokenUtils;
import br.senai.sc.editoralivros.security.service.JpaService;
import br.senai.sc.editoralivros.security.users.UserJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/editoraLivros/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JpaService jpaService;

    private TokenUtils tokenUtils = new TokenUtils();

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticar(
            @RequestBody @Valid UsuarioDTO usuarioDTO,
            HttpServletResponse response
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getUsername(), usuarioDTO.getSenha());

        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            String token = tokenUtils.gerarToken(authentication);
            Cookie cookie = new Cookie("jwt", token);
//            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            UserJPA user = (UserJPA) authentication.getPrincipal();
            Pessoa pessoa = user.getPessoa();
            return ResponseEntity.status(HttpStatus.OK).body(pessoa);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
        }
    }
}
