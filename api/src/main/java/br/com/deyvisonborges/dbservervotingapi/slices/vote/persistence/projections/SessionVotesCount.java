package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.projections;

public interface SessionVotesCount {
  Long getSessionId();
  Long getTotal();
}
