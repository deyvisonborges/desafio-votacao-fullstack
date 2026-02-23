package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VotingSessionJpaRepository extends JpaRepository<VotingSessionSchema, Long> {
  boolean existsByIdAndEndsAtAfter(final Long id, final Instant now);
  
  @Query("""
  SELECT s
  FROM VotingSessionSchema s
  WHERE s.agenda.id = :agendaId
    AND s.startAt <= :now
    AND s.endsAt >= :now
""")
  Optional<VotingSessionSchema> findActiveSessionByAgendaId(
    Long agendaId,
    Instant now
  );
  
  @Query("""
  select count(s) > 0
  from VotingSessionSchema s
  where s.agenda.id = :agendaId
    and s.startAt <= :now
    and s.endsAt >= :now
""")
  boolean hasActiveSessionToAgenda(
    Long agendaId,
    Instant now
  );

  @Query("SELECT s FROM VotingSessionSchema s JOIN FETCH s.agenda")
  List<VotingSessionSchema> findAllWithAgenda();
}
