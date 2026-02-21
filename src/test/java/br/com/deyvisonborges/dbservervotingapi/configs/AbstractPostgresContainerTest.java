package br.com.deyvisonborges.dbservervotingapi.configs;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.time.Duration;

public abstract class AbstractPostgresContainerTest {
  @ServiceConnection // Liga o Postgres do Docker ao Spring
  protected static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
    .withReuse(true)
    .withStartupTimeout(Duration.ofMinutes(3));
  
  static {
    postgres.start();
  }
  
  @BeforeAll
  static void checkDocker() {
    Assumptions.assumeTrue(
      DockerClientFactory.instance().isDockerAvailable(),
      "Docker is not available. Skipping integration tests."
    );
  }
}