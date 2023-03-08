package br.senai.sc.editoralivros.security;

import br.senai.sc.editoralivros.model.entities.Pessoa;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class TokenUtils {

    private final String senhaForte = "8d93c8a196a1d63c95815dffd7eb788f8596fc272770375d4109dc636100de11";

    public String gerarToken(Authentication authenticationToken) {
        Pessoa pessoa = (Pessoa) authenticationToken.getPrincipal();
        return Jwts.builder()
                .setIssuer("API da Editora de Livros")
                .setSubject(pessoa.getCpf().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1800000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public Boolean validaToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
            System.out.println("Token válido");
            return true;
        } catch (Exception e) {
            System.out.println("Token inválido");
            return false;
        }
    }

    public long getUsuarioCPF(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject());
//        return new UserJPA(pessoaRepository.findById(Long.parseLong(cpf)).get());
    }
}
