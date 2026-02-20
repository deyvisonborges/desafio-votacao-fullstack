package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class FindVotingSessionHandler {
  private final VotingSessionRepositoryService repository;
  
  public FindVotingSessionHandler(final VotingSessionRepositoryService repository) {
    this.repository = repository;
  }
  
  public VotingSessionModel execute(final Long sessionId) {
    return repository.findById(sessionId)
      .orElseThrow(() -> new ResourceNotFoundException("Voting session not found"));
  }
}
