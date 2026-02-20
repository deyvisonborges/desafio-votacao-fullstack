package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class VotingSessionRepositoryService {
  private final Logger log = LoggerFactory.getLogger(VotingSessionRepositoryService.class);
  private final VotingSessionJpaRepository repository;
  
  public VotingSessionRepositoryService(final VotingSessionJpaRepository repository) {
    this.repository = repository;
  }
  
  public VotingSessionModel save(final VotingSessionModel model) {
    var schema = VotingSessionMapper.toNewSchema(model);
    var saved = repository.save(schema);
    return VotingSessionMapper.toModel(saved);
  }
  
  public boolean hasActiveSession(final Long sessionId) {
    return repository.existsByIdAndEndsAtAfter(sessionId, Instant.now());
  }
  
  public Optional<VotingSessionModel> findById(final Long sessionId) {
    log.info("Finding agenda by id={}", sessionId);
    return repository.findById(sessionId)
      .map(VotingSessionMapper::toModel);
  }
}
