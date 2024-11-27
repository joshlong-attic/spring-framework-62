package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ImportRuntimeHints(UiResourcesRuntimeHintsRegistrar.class)
class SecurityConfiguration {

    
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder pw) {
        var josh = User.withUsername("josh").password(pw.encode("pw")).roles("USER").build();
        var rob = User.withUsername("rob").password(pw.encode("pw")).roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(josh, rob);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oneTimeTokenLogin(configurer ->
                        configurer.tokenGenerationSuccessHandler(
                                (request, response, 
                                 oneTimeToken) -> {
            
            var msg = "go to http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue();
            System.out.println(msg);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("you've got console mail!");
                               
                               
                                }))
                .webAuthn(c -> c
                        .rpId("localhost")
                        .rpName("bootiful passkeys")
                        .allowedOrigins("http://localhost:8080")
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }

}
