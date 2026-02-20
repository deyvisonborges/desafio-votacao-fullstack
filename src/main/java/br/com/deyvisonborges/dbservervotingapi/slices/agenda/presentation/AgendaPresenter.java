package br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;

import java.util.List;

public final class AgendaPresenter {
  public static AgendaResponse toResponse(final AgendaModel model) {
    return new AgendaResponse(
      model.getId(),
      model.getTitle(),
      model.getDescription(),
      model.getStatus()
    );
  }
  
  public static List<AgendaResponse> toResponseList(final List<AgendaModel> models) {
    return models.stream()
      .map(AgendaPresenter::toResponse)
      .toList();
  }
}
