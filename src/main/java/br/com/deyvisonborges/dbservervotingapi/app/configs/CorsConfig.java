package br.com.deyvisonborges.dbservervotingapi.app.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  private final Environment environment;
  
  public CorsConfig(final Environment environment) {
    this.environment = environment;
  }
  
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    String[] activeProfiles = environment.getActiveProfiles();
    String profile = activeProfiles.length > 0 ? activeProfiles[0] : "dev";
    CorsRegistration registration = registry.addMapping("/api/**");
    
    switch (profile) {
      case "prod":
        registration
          .allowedOrigins("https://deyvisonborges.com.br")
          .allowedMethods("GET", "POST", "PUT", "DELETE")
          .allowedHeaders("Authorization", "Content-Type")
          .allowCredentials(true)
          .maxAge(7200); // Cache CORS preflight response for 1 hour
        break;
      
      case "hml":
        registration
          .allowedOrigins(
            "https://hml.deyvisonborges.com.br",
            "https://teste.deyvisonborges.com.br"
          )
          .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
          .allowedHeaders("Authorization", "Content-Type")
          .allowCredentials(true)
          .maxAge(3600);
        break;
      
      default: // dev
        registration
          .allowedOrigins(
            "http://localhost:3000",
            "http://localhost:4200"
          )
          .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
          // .allowedMethods("*")
          .allowedHeaders("*")
          .allowCredentials(true)
          .maxAge(3600);
    }
  }
}
