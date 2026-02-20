package br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class UpdateAgendaHandler {
  private final AgendaRepositoryService repository;
  
  public UpdateAgendaHandler(final AgendaRepositoryService repository) {
    this.repository = repository;
  }
  
  public void execute(final Long agendaId, final UpdateAgendaCommand command) {
    var existing = repository.findById(agendaId)
      .orElseThrow(() -> new ResourceNotFoundException("Agenda with id " + agendaId + " not found"));
    existing.update(command.title(), command.description());
    repository.update(existing);
  }
}
