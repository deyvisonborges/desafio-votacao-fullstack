package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface VotingSessionJpaRepository extends JpaRepository<VotingSessionSchema, Long> {
  boolean existsByIdAndEndsAtAfter(final Long id, final Instant now);
}
