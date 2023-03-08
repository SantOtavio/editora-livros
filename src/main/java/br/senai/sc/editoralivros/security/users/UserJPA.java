package br.senai.sc.editoralivros.security.users;

import br.senai.sc.editoralivros.model.entities.Pessoa;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserJPA implements UserDetails {

    private Collection<GrantedAuthority> authorities;

    private Pessoa pessoa;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    public UserJPA(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String getPassword() {
        return pessoa.getSenha();
    }

    @Override
        public String getUsername() {return pessoa.getEmail();}
}
