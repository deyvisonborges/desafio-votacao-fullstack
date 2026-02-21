package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AgendaRepositoryServiceTest {
  @Mock
  private AgendaJpaRepository repository;
  
  @InjectMocks
  private AgendaRepositoryService service;
  
  private AgendaSchema fakeSchema;
  private AgendaSchema fakeSchemaTwo;
  private AgendaModel fakeModel;
  
  @BeforeEach
  public void setUp() {
    fakeSchema = new AgendaSchema();
    fakeSchema.setId(1L);
    fakeSchema.setTitle("Titulo");
    fakeSchema.setDescription("Descrição");
    
    fakeSchemaTwo = new AgendaSchema();
    fakeSchemaTwo.setId(2L);
    fakeSchemaTwo.setTitle("Titulo 2");
    fakeSchemaTwo.setDescription("Descrição 2");
    
    fakeModel = AgendaModel.restore(
      1L,
      "Titulo",
      "Descrição",
      AgendaStatus.CREATED
    );
  }
  
  @Test
  @DisplayName("Should return agenda when id exists")
  public void shouldReturnAgendaWhenIdExists() {
    Mockito.when(repository.findById(1L))
      .thenReturn(Optional.of(fakeSchema));
    
    final var result = service.findById(1L);
    
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(1L, result.get().getId());
    
    Mockito.verify(repository).findById(1L);
  }
  
  @Test
  @DisplayName("Should return empty when id does not exist")
  public void shouldReturnEmptyWhenIdDoesNotExist() {
    Mockito.when(repository.findById(1L))
      .thenReturn(Optional.empty());
    
    final var result = service.findById(1L);
    
    Assertions.assertTrue(result.isEmpty());
  }
  
  @Test
  @DisplayName("Should return list of agendas when repository has data")
  void shouldReturnListOfAgendasWhenRepositoryHasData() {
    Mockito.when(repository.findAll())
      .thenReturn(List.of(fakeSchema, fakeSchemaTwo));
    
    final var result = service.findAll();
    
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("Titulo", result.get(0).getTitle());
    Assertions.assertEquals("Titulo 2", result.get(1).getTitle());
  }
  
  @Test
  @DisplayName("Should update agenda when id exists")
  void shouldUpdateAgendaWhenIdExists() {
    final var updatedData = AgendaModel.restore(
      fakeModel.getId(),
      "Novo titulo",
      fakeSchema.getDescription(),
      AgendaStatus.CREATED
    );
    
    Mockito.when(repository.findById(1L))
      .thenReturn(Optional.of(fakeSchema));
    Mockito.when(repository.save(Mockito.any()))
      .thenReturn(fakeSchema);

    final var result = service.update(updatedData);
    
    Assertions.assertNotNull(result);
    Assertions.assertEquals(updatedData.getTitle(), result.getTitle());
    
    Mockito.verify(repository).findById(1L);
    Mockito.verify(repository).save(Mockito.any(AgendaSchema.class));
  }
  
  @Test
  @DisplayName("Should throw ResourceNotFoundException when id does not exist")
  public void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    var updatedData = AgendaModel.restore(
      99L,
      "Novo titulo",
      fakeSchema.getDescription(),
      AgendaStatus.CREATED
    );
    
    Mockito.when(repository.findById(99L))
      .thenReturn(Optional.empty());
    
    Assertions.assertThrows(
      ResourceNotFoundException.class,
      () -> service.update(updatedData), "Resource not found"
    );

    Mockito.verify(repository, Mockito.never())
      .save(Mockito.any());
  }
}
