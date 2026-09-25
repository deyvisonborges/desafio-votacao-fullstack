package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgendaJpaRepository extends JpaRepository<AgendaSchema, Long>, JpaSpecificationExecutor<AgendaSchema> {
  long countByStatus(AgendaStatus status);

  boolean existsByTitleIgnoreCase(String title);

  List<AgendaSchema> findTop10ByOrderByCreatedAtDesc();

  // Native/raw SQL example: joins across tables not modeled as JPA relations here.
  @Query(value = """
      SELECT a.id AS "agendaId", a.title AS "title", COUNT(v.id) AS "voteCount"
      FROM agendas a
      JOIN voting_sessions vs ON vs.agenda_id = a.id
      LEFT JOIN votes v ON v.session_id = vs.id
      GROUP BY a.id, a.title
      ORDER BY "voteCount" DESC
      """, nativeQuery = true)
  List<AgendaVoteCountProjection> findAgendaVoteCounts();
}

