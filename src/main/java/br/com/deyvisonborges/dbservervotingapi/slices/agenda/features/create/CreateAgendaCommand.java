package br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create;

public record CreateAgendaCommand(
  String title,
  String description
) { }
