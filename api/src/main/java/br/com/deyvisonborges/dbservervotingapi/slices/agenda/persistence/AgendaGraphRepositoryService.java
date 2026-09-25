package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteJpaRepository;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.projections.SessionVotesCount;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionJpaRepository;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads agendas together with their voting session and vote tally using a fixed
 * number of batched queries (the JPA/Hibernate equivalent of an entity graph fetch
 * plan), instead of:
 *  - N+1: one query per agenda to fetch its session, then one per session for its votes.
 *  - Cartesian product: a single join across agendas/sessions/votes, which repeats
 *    each agenda row once per vote cast in that session.
 */
@Service
public class AgendaGraphRepositoryService {
  private final AgendaJpaRepository agendaRepository;
  private final VotingSessionJpaRepository sessionRepository;
  private final VoteJpaRepository voteRepository;

  public AgendaGraphRepositoryService(
    final AgendaJpaRepository agendaRepository,
    final VotingSessionJpaRepository sessionRepository,
    final VoteJpaRepository voteRepository
  ) {
    this.agendaRepository = agendaRepository;
    this.sessionRepository = sessionRepository;
    this.voteRepository = voteRepository;
  }

  public List<AgendaGraph> findGraph(final List<Long> agendaIds) {
    // 1) Root of the graph: the agendas themselves.
    var agendas = agendaRepository.findAllById(agendaIds).stream()
      .map(AgendaMapper::toModel)
      .toList();

    if (agendas.isEmpty()) return List.of();
    var ids = agendas.stream().map(agenda -> agenda.getId()).toList();

    // 2) One batched query for every session of every agenda (fixes the N+1).
    Map<Long, VotingSessionModel> sessionsByAgendaId = sessionRepository.findByAgenda_IdIn(ids).stream()
      .map(VotingSessionMapper::toModel)
      .collect(Collectors.toMap(VotingSessionModel::getAgendaId, Function.identity()));

    var sessionIds = sessionsByAgendaId.values().stream().map(VotingSessionModel::getId).toList();

    // 3) One batched, pre-aggregated (GROUP BY) query for every vote of every session.
    // Never join votes directly onto agendas/sessions here — that's the cartesian
    // product trap (each vote row would duplicate its parent agenda row).
    Map<Long, Long> votesBySessionId = sessionIds.isEmpty()
      ? Map.of()
      : voteRepository.countVotesBySessionIds(sessionIds).stream()
          .collect(Collectors.toMap(SessionVotesCount::getSessionId, SessionVotesCount::getTotal));

    // 4) Stitch the pre-fetched batches back into the requested graph shape.
    return agendas.stream()
      .map(agenda -> {
        var session = sessionsByAgendaId.get(agenda.getId());
        var voteCount = session != null ? votesBySessionId.getOrDefault(session.getId(), 0L) : 0L;
        return new AgendaGraph(agenda, session, voteCount);
      })
      .toList();
  }
}
