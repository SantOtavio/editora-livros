package br.senai.sc.editoralivros.security.configuration;

import br.senai.sc.editoralivros.security.AutenticacaoFiltro;
import br.senai.sc.editoralivros.security.service.GoogleService;
import br.senai.sc.editoralivros.security.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class AutenticacaoConfig {

    @Autowired
    private JpaService jpaService;

    @Autowired
    private GoogleService googleService;

    //Configura as autorizações de acesso
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jpaService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());


        httpSecurity.authenticationProvider(authenticationProvider);

        httpSecurity
                .authorizeRequests()
                //Libera o acesso sem autenticação no /login
                .antMatchers("/editoraLivros/login", "/editoraLivros/usuarios").permitAll()
                //Determina que todas as demais requisições terão que ser autenticadas
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin().permitAll()
                .loginPage("/editoraLivros/login")
                .defaultSuccessUrl("/editoraLivros/home")
                .and()
                .oauth2Login().userInfoEndpoint().userService(googleService)
                .and()
                .loginPage("/editoraLivros/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                        System.out.println(oAuth2User);
                        try {
                            UserDetails userDetails = jpaService.loadUserByUsername(oAuth2User.getAttribute("email"));
                            response.sendRedirect("/editoraLivros/home");
                        } catch (UsernameNotFoundException e) {
                            response.sendRedirect("/editoraLivros/usuarios");
                        }
                    }
                })
                .and()
                .logout()
                .logoutUrl("/editoraLivros/logout")
                .logoutSuccessUrl("/editoraLivros/login")
                .permitAll();
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().addFilterBefore(new AutenticacaoFiltro(jpaService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
