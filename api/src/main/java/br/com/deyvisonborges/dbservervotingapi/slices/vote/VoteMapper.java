package br.com.deyvisonborges.dbservervotingapi.slices.vote;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteSchema;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionSchema;

public final class VoteMapper {
  public static VoteSchema toNewSchema(final VoteModel voteModel) {
    VoteSchema schema = new VoteSchema();
    schema.setAssociateId(voteModel.getAssociateId());
    schema.setVote(voteModel.getVote());
    var session = new VotingSessionSchema();
    session.setId(voteModel.getSessionId());
    schema.setSession(session);
    return schema;
  }
  
  public static VoteModel toModel(final VoteSchema schema) {
    return VoteModel.restore(
      schema.getSession().getId(),
      schema.getAssociateId(),
      schema.getVote()
    );
  }
}
