package com.sth.userservice.config;

import com.sth.userservice.security.AuthenticationFilter;
import com.sth.userservice.security.CustomAuthenticationManager;
import com.sth.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class Securityconfig {
    private final CustomAuthenticationManager customAuthenticationManager;
    private final UserService userService;
    private final Environment env;
//

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((authorizeHttpRequest) ->
                        authorizeHttpRequest.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
                .addFilter(getAuthenticationFilter());
        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {


        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(customAuthenticationManager, userService, env);

        return authenticationFilter;
    }
}
