package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.VoteMapper;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.VoteModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteRepositoryService implements IVoteRepository{
  private final VoteJpaRepository repository;
  
  public VoteRepositoryService(final VoteJpaRepository repository) {
    this.repository = repository;
  }
  
  @Override
  public VoteModel save(final VoteModel vote) {
    return VoteMapper.toModel(
      repository.save(
        VoteMapper.toNewSchema(vote)
      )
    );
  }
  
  @Override
  public boolean associateAlreadyVoted(final Long sessionId, final String associateId) {
    return repository.existsBySession_IdAndAssociateId(sessionId, associateId);
  }
  
  @Override
  public VoteResultProjection countVotesBySessionId(Long sessionId) {
    return repository.countVotes(sessionId);
  }
  
  public List<Long> findSessionIdsWithVotes() {
    return repository.findSessionIdsWithVotes();
  }
}
