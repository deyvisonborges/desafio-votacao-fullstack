package br.com.deyvisonborges.dbservervotingapi.e2e;

import br.com.deyvisonborges.dbservervotingapi.configs.AbstractTestcontainersIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SwaggerIntegrationTest extends AbstractTestcontainersIntegrationTest {
  @Test
  void shouldRenderSwaggerUIPage() {
    var content = RestAssured.given()
      .when().get("/swagger-ui/index.html")
      .then()
      .statusCode(200)
      .extract().body().asString();
    Assertions.assertTrue(content.contains("Swagger UI"));
  }
}
