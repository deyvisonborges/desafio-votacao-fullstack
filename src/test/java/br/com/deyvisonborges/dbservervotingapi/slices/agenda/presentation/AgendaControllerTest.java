package br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaTestDataBuilder;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.FindAgendaByIdHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.FindAllAgendasHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create.CreateAgendaCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create.CreateAgendaHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AgendaController.class)
public class AgendaControllerTest {
  private ObjectMapper objectMapper;
  @Autowired MockMvc mockMvc;
  
  @MockitoBean private CreateAgendaHandler createAgendaHandler;
  @MockitoBean private FindAllAgendasHandler findAllAgendasHandler;
  @MockitoBean private FindAgendaByIdHandler findAgendaByIdHandler;
  @MockitoBean private UpdateAgendaHandler updateAgendaHandler;
  
  AgendaModel mockAgenda;
  
  @BeforeEach
  void beforeEach() {
    objectMapper = new ObjectMapper();
    mockAgenda = AgendaTestDataBuilder.persistedAgenda();
  }
  
  @Test
  @DisplayName("Should return agenda when id exists")
  public void shouldReturnAgendaWhenIdExists() throws Exception {
    Long id = mockAgenda.getId();
  
    Mockito.when(findAgendaByIdHandler.execute(id)).thenReturn(mockAgenda);
    
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/agendas/{id}", id)
        .accept(MediaType.APPLICATION_JSON))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Titulo"));
  }
  
  @Test
  @DisplayName("Should return all agendas")
  public void shouldReturnAllAgendas() throws Exception {
    final var agendas = AgendaTestDataBuilder.persistedAgendas();
    
    Mockito.when(findAllAgendasHandler.execute()).thenReturn(agendas);
    
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/agendas"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Titulo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Titulo 2"));
  }
  
  @Test
  @DisplayName("Should create a new agenda successfully")
  public void shouldCreateAgendaSuccessfully() throws Exception {
    var command = new CreateAgendaCommand(
      mockAgenda.getTitle(),
      mockAgenda.getDescription()
    );
  
    Mockito.when(
      createAgendaHandler.execute(
        ArgumentMatchers.any(CreateAgendaCommand.class)
      )
    ).thenReturn(mockAgenda);
    
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/agendas")
        .content(objectMapper.writeValueAsString(command))
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockAgenda.getId()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Titulo"));
  }
  
  @Test()
  @DisplayName("Should update agenda successfully")
  void shouldUpdateAgendaSuccessfully() throws Exception {
    var command = new UpdateAgendaCommand(mockAgenda.getTitle(), mockAgenda.getDescription());
    
    Mockito.doNothing().when(updateAgendaHandler).execute(
      ArgumentMatchers.eq(mockAgenda.getId()),
      ArgumentMatchers.any(UpdateAgendaCommand.class)
    );
    
    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/agendas/{agendaId}", mockAgenda.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(command)))
      .andExpect(MockMvcResultMatchers.status().isNoContent());
    
    Mockito.verify(updateAgendaHandler).execute(
      ArgumentMatchers.eq(mockAgenda.getId()),
      ArgumentMatchers.any(UpdateAgendaCommand.class)
    );
  }
}
