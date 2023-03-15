package br.senai.sc.editoralivros.security.service;

import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.repository.PessoaRepository;
import br.senai.sc.editoralivros.security.users.UserJPA;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JpaService implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pessoa> pessoaOptional;
        try {
            Long cpf = Long.parseLong(username);
            pessoaOptional = pessoaRepository.findById(cpf);
        } catch (NumberFormatException e) {
            pessoaOptional = pessoaRepository.findByEmail(username);
        }
        if (pessoaOptional.isPresent()) {
            System.out.println("Pessoa encontrada");
            return new UserJPA(pessoaOptional.get());
        } else {
            pessoaOptional = pessoaRepository.findById(Long.parseLong(username));
            if (pessoaOptional.isPresent()) {
                System.out.println("Pessoa encontrada");
                return new UserJPA(pessoaOptional.get());
            }
        }
        System.out.println("Pessoa não encontrada");
        throw new UsernameNotFoundException("Usuário não encontrado");
    }

    public UserDetails loadUserByCPF(Long cpf) throws UsernameNotFoundException {
        System.out.println("cpf: " + cpf);
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(cpf);
        if (pessoaOptional.isPresent()) {
            UserJPA userJPA = new UserJPA(pessoaOptional.get());
            System.out.println(userJPA);
            return userJPA;
        }
        System.out.println("Pessoa não encontrada");
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
