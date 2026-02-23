package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class CreateVotingSessionHandler {
  private final Logger log = LoggerFactory.getLogger(CreateVotingSessionHandler.class);
  private final AgendaRepositoryService agendaRepositoryService;
  private final VotingSessionRepositoryService repository;
  
  public CreateVotingSessionHandler(
    final AgendaRepositoryService agendaRepositoryService,
    final VotingSessionRepositoryService repository
  ) {
    this.agendaRepositoryService = agendaRepositoryService;
    this.repository = repository;
  }
  
  public VotingSessionModel execute(final Long agendaId, final Integer durationInMinutes) {
    log.info("Execute CreateVotingSessionHandler.");
    
    var agenda = agendaRepositoryService.findById(agendaId)
      .orElseThrow(() -> new ResourceNotFoundException("Agenda " + agendaId + " not found"));
    
    var startAt = Instant.now();
    var endsAt = startAt.plus(durationInMinutes, ChronoUnit.MINUTES);
    
    var model = VotingSessionModel.create(
      agenda.getId(),
      startAt,
      endsAt
    );
    
    log.info("Created VotingSessionModel: {}", model);
    
    return repository.save(model);
  }
}
