package br.com.deyvisonborges.dbservervotingapi.e2e;

import br.com.deyvisonborges.dbservervotingapi.configs.AbstractTestcontainersIntegrationTest;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaTestDataBuilder;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create.CreateAgendaCommand;
//import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaMapper;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation.AgendaPresenter;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation.AgendaResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

public class AgendaIntegrationTest extends AbstractTestcontainersIntegrationTest {
  @LocalServerPort
  private int port;
//  final String HEADER_PARAM_ORIGIN = "Origin";
//  final String ORIGIN_REMOTE = "https://deyvisonborges.com.br";
  
  private static RequestSpecification specification;
  private static ObjectMapper mapper;
  private static AgendaModel agendaModel;
  
  @BeforeAll()
  static void init() {
    mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }
  
  @BeforeEach()
  void beforeEach() {
    agendaModel = AgendaTestDataBuilder.newAgenda();
  }
  
  @Test
  @DisplayName("shouldCreateAAgenda")
  void createAgenda() throws Exception {
    specification = new RequestSpecBuilder()
//      .addHeader(HEADER_PARAM_ORIGIN, "http://localhost:8080")
      .setBasePath("/api/v1/agendas")
      .setPort(port)
      .addFilter(new RequestLoggingFilter(LogDetail.ALL))
      .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();
    
    var content = RestAssured.given(specification)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(new CreateAgendaCommand(agendaModel.getTitle(), agendaModel.getDescription()))
      .when()
      .post()
      .then()
      .statusCode(200)
      .extract()
      .body()
      .asString();
    
    var mappedResponse = mapper.readValue(content, AgendaResponse.class);
    agendaModel = AgendaPresenter.fromCommandToModel(mappedResponse);
    
    Assertions.assertNotNull(agendaModel);
    Assertions.assertNotNull(agendaModel.getTitle());
    Assertions.assertNotNull(agendaModel.getDescription());
    Assertions.assertNotNull(agendaModel.getStatus());
    Assertions.assertNotNull(agendaModel.getId());
    Assertions.assertEquals("Titulo", agendaModel.getTitle());
  }
}
