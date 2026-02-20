package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence;

public interface VoteResultProjection {
  Long getTotal();
  Long getYesCount();
  Long getNoCount();
}
