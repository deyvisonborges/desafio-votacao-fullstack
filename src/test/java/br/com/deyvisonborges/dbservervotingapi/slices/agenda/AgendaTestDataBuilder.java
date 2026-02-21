package br.com.deyvisonborges.dbservervotingapi.slices.agenda;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;

import java.util.List;

public final class AgendaTestDataBuilder {
  public static AgendaModel newAgenda() {
    return AgendaModel.create("Titulo", "Descricao");
  }
  
  public static AgendaModel persistedAgenda() {
    return AgendaModel.restore(1L, "Titulo", "Descricao", AgendaStatus.CREATED);
  }
  
  public static List<AgendaModel> persistedAgendas() {
    return List.of(
      AgendaModel.restore(1L, "Titulo", "Descricao", AgendaStatus.CREATED),
      AgendaModel.restore(2L, "Titulo 2", "Descricao 2", AgendaStatus.CREATED)
    );
  }
}
