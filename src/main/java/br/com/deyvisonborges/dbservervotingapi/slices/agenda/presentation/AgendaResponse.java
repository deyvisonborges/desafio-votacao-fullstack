package br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;

public record AgendaResponse(
  Long id,
  String title,
  String description,
  AgendaStatus status
) {
}
