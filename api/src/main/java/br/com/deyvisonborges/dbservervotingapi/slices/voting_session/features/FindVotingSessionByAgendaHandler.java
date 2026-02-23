package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class FindVotingSessionByAgendaHandler {
  private final VotingSessionRepositoryService repository;

  public FindVotingSessionByAgendaHandler(final VotingSessionRepositoryService repository) {
    this.repository = repository;
  }

  public VotingSessionModel execute(final Long agendaId) {
    return repository.findByAgendaId(agendaId)
      .orElseThrow(() -> new ResourceNotFoundException("Voting session not found for agenda"));
  }
}
