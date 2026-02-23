package br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class CreateAgendaHandler {
  private final AgendaRepositoryService repository;
  
  public CreateAgendaHandler(AgendaRepositoryService repository) {
    this.repository = repository;
  }
  
  public AgendaModel execute(final CreateAgendaCommand command) {
    
    return repository.save(
      AgendaModel.create(
        command.title(),
        command.description()
      )
    );
  }
}
