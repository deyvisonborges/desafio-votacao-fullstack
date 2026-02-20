package br.com.deyvisonborges.dbservervotingapi.slices.vote.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteType;

public record VoteResponse(
  Long sessionId,
  String associateId,
  VoteType vote
) { }
