package br.com.deyvisonborges.dbservervotingapi.slices.agenda;

public final class AgendaTestMocks {
  public static AgendaModel fakeAgendaModel() {
    return AgendaModel.create(
      "Titulo de teste",
      "Descricao de teste"
    );
  }
}
