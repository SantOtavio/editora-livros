package br.senai.sc.editoralivros.security.configuration;

import br.senai.sc.editoralivros.security.AutenticacaoFiltro;
import br.senai.sc.editoralivros.security.TokenUtils;
import br.senai.sc.editoralivros.security.service.GoogleService;
import br.senai.sc.editoralivros.security.service.JpaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AutenticacaoConfig {

    @Autowired
    private JpaService jpaService;

    @Autowired
    private GoogleService googleService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    //Configura as autorizações de acesso
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                //Libera o acesso sem autenticação no /login
                .antMatchers("/editoraLivros/login", "/editoraLivros/login/auth", "/editoraLivros/logout", "/editoraLivros/pessoa" ).permitAll()
                .antMatchers(HttpMethod.POST, "/editoraLivros/post/livro").hasAuthority("Autor")
                //Determina que todas as demais requisições terão que ser autenticadas
                .anyRequest().authenticated()
                .and().csrf().disable()
                .cors().disable()
                .formLogin().permitAll()
                .and()
                .logout().permitAll();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(new AutenticacaoFiltro(new TokenUtils(), jpaService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    //Este método é necessário para que o AuthenticationManager seja injetado no AutenticacaoController
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}
