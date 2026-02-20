package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.VoteModel;

public interface IVoteRepository {
  VoteModel save(final VoteModel vote);
  boolean associateAlreadyVoted(final Long sessionId, final String associateId);
  VoteResultProjection countVotesBySessionId(final Long sessionId);
}
