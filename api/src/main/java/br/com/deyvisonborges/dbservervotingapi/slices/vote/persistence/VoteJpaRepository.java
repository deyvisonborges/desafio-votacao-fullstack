package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.projections.SessionVotesCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteJpaRepository extends JpaRepository<VoteSchema, Long> {
  boolean existsBySession_IdAndAssociateId(Long sessionId, String associateId);
  
  @Query("""
        SELECT
          COUNT(v) as total,
          COALESCE(SUM(CASE WHEN v.vote = 'YES' THEN 1 ELSE 0 END), 0) as yesCount,
          COALESCE(SUM(CASE WHEN v.vote = 'NO' THEN 1 ELSE 0 END), 0) as noCount
        FROM VoteSchema v
        WHERE v.session.id = :sessionId
    """)
  VoteResultProjection countVotes(@Param("sessionId") Long sessionId);
  
  @Query("""
        SELECT v.session.id as sessionId, COUNT(v) as total
        FROM VoteSchema v
        WHERE v.session.id IN :sessionIds
        GROUP BY v.session.id
    """)
  List<SessionVotesCount> countVotesBySessionIds(@Param("sessionIds") List<Long> sessionIds);
  
  @Query("""
    SELECT DISTINCT v.session.id
    FROM VoteSchema v
""")
  List<Long> findSessionIdsWithVotes();
}
