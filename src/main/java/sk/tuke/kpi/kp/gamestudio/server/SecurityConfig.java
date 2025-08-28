package sk.tuke.kpi.kp.gamestudio.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(
                        "/", "/index.html",
                        "/assets/**", "/static/**",
                        "/css/**", "/js/**", "/images/**",
                        "/favicon.ico", "/manifest.json", "/webjars/**"
                ).permitAll()
                .anyRequest().permitAll();
        return http.build();
    }
}
