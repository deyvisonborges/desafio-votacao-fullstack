package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features;

import java.time.Instant;

public interface VoteSessionViewProjection {
  Long getId();
  Long getAgendaId();
  Instant getStartAt();
  Instant getEndsAt();
}
