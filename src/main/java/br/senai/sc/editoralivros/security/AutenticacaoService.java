package br.senai.sc.editoralivros.security;

import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.repository.PessoaRepository;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    private final String senhaForte = "8d93c8a196a1d63c95815dffd7eb788f8596fc272770375d4109dc636100de11";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pessoa> pessoa = pessoaRepository.findByEmail(username);
        System.out.println(pessoa.get());
        if (pessoa.isPresent()) {
            return pessoa.get();
        }
        throw new UsernameNotFoundException("Usuário não encontrado");
    }


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
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Pessoa getUsuario(String token) {
        String cpf = Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject();
        return pessoaRepository.findById(Long.parseLong(cpf)).get();
    }
}
