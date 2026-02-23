package br.com.deyvisonborges.dbservervotingapi.slices.vote.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteType;

public record VoteCreatedResponse(
  Long sessionId,
  String associateId,
  VoteType vote
) {}