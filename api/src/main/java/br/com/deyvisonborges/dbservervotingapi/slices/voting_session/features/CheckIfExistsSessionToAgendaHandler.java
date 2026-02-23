package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.BusinessException;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class CheckIfExistsSessionToAgendaHandler {
  private final VotingSessionRepositoryService repositoryService;
  
  public CheckIfExistsSessionToAgendaHandler(final VotingSessionRepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }
  
  public boolean execute(final Long agendaId) {
    if(!repositoryService.existsActiveSessionToAgenda(agendaId))
      throw new BusinessException("Not found session assigned to agenda with id: " + agendaId);
    return true;
  }
}
