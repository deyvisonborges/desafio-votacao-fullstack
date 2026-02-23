package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation;

import java.time.Instant;

public record VotingSessionVotesResult(
  Long id,
  Long agendaId,
  boolean open,
  Integer totalVote,
  Integer yesVotes,
  Integer noVotes,
  String result,
  Instant createdAt
) { }
