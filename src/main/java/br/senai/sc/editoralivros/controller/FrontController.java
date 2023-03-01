package br.senai.sc.editoralivros.controller;

import br.senai.sc.editoralivros.DTO.PessoaDTO;
import br.senai.sc.editoralivros.model.entities.Pessoa;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/editoraLivros")
public class FrontController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/cadastro-livros")
    public String livro() {
        return "cadastro-livros";
    }

    @GetMapping("/usuarios")
    public String usuario(Authentication authentication, Model model) {

        PessoaDTO pessoaDTO = new PessoaDTO();

        if (authentication != null) {
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
                pessoaDTO.setEmail(oauth.getPrincipal().getAttribute("email"));
                pessoaDTO.setNome(oauth.getPrincipal().getAttribute("given_name"));
                pessoaDTO.setSobrenome(oauth.getPrincipal().getAttribute("family_name"));
            }
        }

        model.addAttribute("pessoa", pessoaDTO);
        return "cadastro-usuarios";
    }
}
