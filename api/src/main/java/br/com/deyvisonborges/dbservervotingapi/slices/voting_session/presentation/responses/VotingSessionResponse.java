package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation.responses;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation.AgendaResponse;

import java.time.Instant;

public record VotingSessionResponse(
  Long id,
  Instant startAt,
  Instant endsAt,
  AgendaResponse agenda,
  boolean enableToVote
) {}
