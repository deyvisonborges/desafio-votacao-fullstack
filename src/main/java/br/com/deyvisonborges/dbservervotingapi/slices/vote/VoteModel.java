package br.com.deyvisonborges.dbservervotingapi.slices.vote;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteType;

import java.util.Objects;

public class VoteModel {
  private final Long sessionId;
  private final String associateId;
  private final VoteType vote;
  
  protected VoteModel(
    final Long sessionId,
    final String associateId,
    final VoteType vote
  ) {
    this.sessionId = Objects.requireNonNull(sessionId);
    this.associateId = Objects.requireNonNull(associateId);
    this.vote = Objects.requireNonNull(vote);
  }
  
  public static VoteModel create(
    final Long sessionId,
    final String associateId,
    final VoteType vote
  ) {
    return new VoteModel(sessionId, associateId, vote);
  }
  
  public static VoteModel restore(
    final Long sessionId,
    final String associateId,
    final VoteType vote
  ) {
    return new VoteModel(
      sessionId,
      associateId,
      vote
    );
  }
  
  public Long getSessionId() {
    return sessionId;
  }
  
  public String getAssociateId() {
    return associateId;
  }
  
  public VoteType getVote() {
    return vote;
  }
}
