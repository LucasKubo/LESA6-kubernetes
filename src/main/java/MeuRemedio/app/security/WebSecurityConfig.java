package MeuRemedio.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementsUserDetailsService implementsUserDetailsService;


    @Bean
    public WebSecurityCustomizer webSecuritycustomizer(){
        return web -> web.ignoring().antMatchers("/static/**", "/template/**","/config/**",
                "/style/**","/assets/**","/script/**", "/images/**", "/languages/**"
        );
    }

    @Override
    public void configure (HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/cadastro", "/", "/enviarEmail","/recuperar_senha","/verificar_cadastro","/sw.js").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("uniqueAndSecret");

        http.headers()
                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy","frame-ancestors 'none'"))
                .addHeaderWriter(new StaticHeadersWriter("Referrer-Policy", "no-referrer, strict-origin-when-cross-origin"))
                .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy","geolocation=(self)"));

    }


    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(implementsUserDetailsService)
                .userDetailsPasswordManager((user, newPassword) -> user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}