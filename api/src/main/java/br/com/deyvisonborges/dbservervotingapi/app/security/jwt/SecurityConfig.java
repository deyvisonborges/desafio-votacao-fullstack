package br.com.deyvisonborges.dbservervotingapi.app.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //  Tells Spring “I’ll handle web security myself.”
public class SecurityConfig {
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/v1/**").permitAll()
      .anyRequest().authenticated()
    );
    http.httpBasic(Customizer.withDefaults());
    http.formLogin(Customizer.withDefaults()); // Shows a login form
    http.logout(Customizer.withDefaults());
    return http.build();
  }
  
}
