package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation.AgendaResponse;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation.responses.VotingSessionResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FindAllVotingSessionsHandler {
  private final VotingSessionRepositoryService sessionRepositoryService;
  private final VoteRepositoryService voteRepositoryService;
  private final AgendaRepositoryService agendaRepositoryService;
  
  public FindAllVotingSessionsHandler(
    final VotingSessionRepositoryService sessionRepositoryService,
    final VoteRepositoryService voteRepositoryService,
    final AgendaRepositoryService agendaRepositoryService1
  ) {
    this.sessionRepositoryService = sessionRepositoryService;
    this.voteRepositoryService = voteRepositoryService;
    this.agendaRepositoryService = agendaRepositoryService1;
  }
  
  public List<VotingSessionResponse> execute() {
    List<VotingSessionModel> sessions = sessionRepositoryService.findAll();
    
    Set<Long> agendaIds = sessions.stream()
      .map(VotingSessionModel::getAgendaId)
      .collect(Collectors.toSet());
    
    List<AgendaModel> agendas =
      agendaRepositoryService.findAllByIds(agendaIds);
    
    Map<Long, AgendaResponse> agendaMap =
      agendas.stream()
        .collect(Collectors.toMap(
          AgendaModel::getId,
          agenda -> new AgendaResponse(
            agenda.getId(),
            agenda.getTitle(),
            agenda.getDescription(),
            agenda.getStatus()
          )
        ));
    
    Set<Long> sessionsWithVotes =
      new HashSet<>(voteRepositoryService.findSessionIdsWithVotes());
    
    return sessions.stream()
      .map(session -> new VotingSessionResponse(
        session.getId(),
        session.getStartAt(),
        session.getEndsAt(),
        agendaMap.get(session.getAgendaId()), // ✅ aqui resolve
        !sessionsWithVotes.contains(session.getId())
      ))
      .toList();
  }
}
