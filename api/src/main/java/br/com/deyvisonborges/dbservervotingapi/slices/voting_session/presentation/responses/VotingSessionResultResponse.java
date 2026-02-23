package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation.responses;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation.AgendaResponse;

public record VotingSessionResultResponse(
  Long id,
  boolean open,
  Integer totalVotes,
  Integer yesVotes,
  Integer noVotes,
  String result,
  AgendaResponse agenda
) {}