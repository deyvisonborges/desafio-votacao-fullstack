package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

public interface AgendaVoteCountProjection {
  Long getAgendaId();
  String getTitle();
  Long getVoteCount();
}
