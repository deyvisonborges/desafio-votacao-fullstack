package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
  VoteResultProjection countVotes(final Long sessionId);
}
