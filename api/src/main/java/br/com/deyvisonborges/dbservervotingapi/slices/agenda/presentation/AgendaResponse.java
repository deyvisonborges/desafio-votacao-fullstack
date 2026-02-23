package br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AgendaResponse(
  Long id,
  String title,
  String description,
  AgendaStatus status
) {
}
