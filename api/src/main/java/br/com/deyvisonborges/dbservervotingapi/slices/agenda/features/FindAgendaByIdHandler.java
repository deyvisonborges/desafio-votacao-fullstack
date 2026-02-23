package br.com.deyvisonborges.dbservervotingapi.slices.agenda.features;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FindAgendaByIdHandler {
  private final Logger log = LoggerFactory.getLogger(FindAgendaByIdHandler.class);
  private final AgendaRepositoryService repository;
  
  public FindAgendaByIdHandler(final AgendaRepositoryService repository) {
    this.repository = repository;
  }
  
  public AgendaModel execute(final Long id) {
    log.info("Finding agenda by id={}", id);
    return repository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Agenda not found with id: " + id));
  }
}
