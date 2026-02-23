package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteResultProjection;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation.VotingSessionPresenter;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation.VotingSessionVotesResult;
import org.springframework.stereotype.Service;

@Service
public class GetVotingSessionVotesResult {
  private final VotingSessionRepositoryService sessionRepositoryService;
  private final VoteRepositoryService  voteRepositoryService;
  
  public GetVotingSessionVotesResult(VotingSessionRepositoryService sessionRepositoryService, VoteRepositoryService voteRepositoryService) {
    this.sessionRepositoryService = sessionRepositoryService;
    this.voteRepositoryService = voteRepositoryService;
  }
  
  public VotingSessionVotesResult execute(Long sessionId) {
    var session = sessionRepositoryService.findById(sessionId)
      .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada"));
    
    VoteResultProjection result = voteRepositoryService.countVotesBySessionId(sessionId);
    return VotingSessionPresenter.toVotingSessionVotesResult(
      session,
      result
    );
  }
}
