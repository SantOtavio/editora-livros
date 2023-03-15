package br.senai.sc.editoralivros.security;

import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.security.service.JpaService;
import br.senai.sc.editoralivros.security.users.UserJPA;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {


    private TokenUtils tokenUtils;
    private JpaService jpaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/editoraLivros/login") || request.getRequestURI().equals("/editoraLivros/login/auth") || request.getRequestURI().equals("/logout")) {
            filterChain.doFilter(request, response);
            System.out.println("entrou no filtro errado");
            return;
        }
//        String token = request.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        } else {
//            token = null;
//        }
        System.out.println("entrou no filtro");
        String token = tokenUtils.buscarCookie(request);
        System.out.println("buscou cookie");
        Boolean valido = tokenUtils.validaToken(token);
        if (valido) {
            System.out.println("Token válido!");
            Long usuarioCPF = tokenUtils.getUsuarioCPF(token);
            UserDetails usuario = jpaService.loadUserByCPF(usuarioCPF);
            System.out.println(usuario);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
    }
}
