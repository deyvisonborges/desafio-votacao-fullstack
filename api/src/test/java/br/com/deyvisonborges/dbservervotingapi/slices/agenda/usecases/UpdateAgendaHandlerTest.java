package br.com.deyvisonborges.dbservervotingapi.slices.agenda.usecases;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateAgendaHandlerTest {
  
  @Mock
  private AgendaRepositoryService repository;
  
  @InjectMocks
  private UpdateAgendaHandler handler;
  
  @Test
  void shouldUpdateAgendaSuccessfully() {
    Long agendaId = 1L;
    
    AgendaModel agenda = AgendaModel.create("Old title", "Old desc");
    
    Mockito.when(repository.findById(agendaId))
      .thenReturn(Optional.of(agenda));
    
    UpdateAgendaCommand command =
      new UpdateAgendaCommand("New title", "New desc");
    
    handler.execute(agendaId, command);
    
    Mockito.verify(repository).update(agenda);
    Assertions.assertEquals("New title", agenda.getTitle());
    Assertions.assertEquals("New desc", agenda.getDescription());
  }
  
  @Test
  void shouldThrowExceptionWhenAgendaNotFound() {
    Long agendaId = 99L;
    
    Mockito.when(repository.findById(agendaId))
      .thenReturn(Optional.empty());
    
    UpdateAgendaCommand command =
      new UpdateAgendaCommand("New", "Desc");
    
    Assertions.assertThrows(ResourceNotFoundException.class,
      () -> handler.execute(agendaId, command));
    
    Mockito.verify(repository, Mockito.never()).update(Mockito.any());
  }
}