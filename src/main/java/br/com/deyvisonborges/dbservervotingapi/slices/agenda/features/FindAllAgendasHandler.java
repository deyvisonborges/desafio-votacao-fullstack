package br.com.deyvisonborges.dbservervotingapi.slices.agenda.features;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllAgendasHandler {
  private final AgendaRepositoryService agendaRepositoryService;
  
  public FindAllAgendasHandler(final AgendaRepositoryService agendaRepositoryService) {
    this.agendaRepositoryService = agendaRepositoryService;
  }
  
  public List<AgendaModel> execute() {
    return this.agendaRepositoryService.findAll();
  }
}
