package br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update;

public record UpdateAgendaCommand(
  String title,
  String description
) {
}
