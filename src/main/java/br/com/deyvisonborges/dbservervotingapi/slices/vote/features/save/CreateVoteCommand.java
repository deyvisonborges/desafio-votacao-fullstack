package br.com.deyvisonborges.dbservervotingapi.slices.vote.features.save;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteType;

public record CreateVoteCommand(
  String associatedId,
  VoteType vote
) { }
