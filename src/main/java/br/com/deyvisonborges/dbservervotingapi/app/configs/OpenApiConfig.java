package br.com.deyvisonborges.dbservervotingapi.app.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("DBServer Voting API")
        .version("1.0")
        .description("API para gerenciamento de sessões de votação e votos."));
  }
}
