package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;

import java.time.Instant;

public record AgendaFilter(
  AgendaStatus status,
  String titleContains,
  Instant createdAfter,
  Instant createdBefore
) {
  public static AgendaFilter empty() {
    return new AgendaFilter(null, null, null, null);
  }
}
