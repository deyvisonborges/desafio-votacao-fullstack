package br.com.deyvisonborges.dbservervotingapi.slices.vote.features.save;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.BusinessException;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.VoteModel;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateVoteHandler {
  private static final Logger log = LoggerFactory.getLogger(CreateVoteHandler.class);
  private final VoteRepositoryService repository;
  private final VotingSessionRepositoryService votingSessionRepository;
  
  public CreateVoteHandler(
    final VoteRepositoryService repository,
    final VotingSessionRepositoryService votingSessionRepository
  ) {
    this.repository = repository;
    this.votingSessionRepository = votingSessionRepository;
  }

  public VoteModel save(final VoteModel vote) {
    var session = votingSessionRepository.hasActiveSession(vote.getSessionId());
    if(!session) {
      log.warn("Session has been deleted: {}", false);
      throw new BusinessException("Invalid session");
    }
    
    var alreadyVoted = repository.associateAlreadyVoted(vote.getSessionId(), vote.getAssociateId());
    if(alreadyVoted)
      throw new BusinessException("The member has already registered their vote.");
    return repository.save(vote);
  }
}
