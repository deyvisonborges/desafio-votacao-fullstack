package br.com.deyvisonborges.dbservervotingapi.configs;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AbstractTestcontainersIntegrationTest {
  @Container
  protected static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:latest");
  @LocalServerPort
  protected Integer port;
  
  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }
  
  @AfterAll
  static void afterAll() {
    postgres.stop();
  }
  
  @DynamicPropertySource
  static void configureProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }
  
  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    RestAssured.basePath = "";
  }
  
  @Test
  void connectionEstablished() {
    Assertions.assertThat(postgres.isCreated()).isTrue();
    Assertions.assertThat(postgres.isRunning()).isTrue();
  }
}