package br.com.deyvisonborges.dbservervotingapi.slices.voting_session;

import java.time.Instant;
import java.util.Objects;

public class VotingSessionModel {
  private final Long id;
  private final Long agendaId;
  private final Instant startAt;
  private final Instant endsAt;
  
  protected VotingSessionModel(
    final Long id,
    final Long agendaId,
    final Instant startAt,
    final Instant endsAt
  ) {
    this.id = id;
    this.agendaId = Objects.requireNonNull(agendaId);
    this.startAt = Objects.requireNonNull(startAt);
    this.endsAt = Objects.requireNonNull(endsAt);
  }
  
  public static VotingSessionModel create(
    final Long agendaId,
    final Instant startAt,
    final Instant endsAt
  ) {
    return new VotingSessionModel(
      null,
      agendaId,
      startAt,
      endsAt
    );
  }
  
  public static VotingSessionModel restore(
    final Long id,
    final Long agendaId,
    final Instant startAt,
    final Instant endsAt
  ) {
    return new VotingSessionModel(
      id,
      agendaId,
      startAt,
      endsAt
    );
  }
  
  public Long getId() {
    return id;
  }
  
  public Long getAgendaId() {
    return agendaId;
  }
  
  public Instant getStartAt() {
    return startAt;
  }
  
  public Instant getEndsAt() {
    return endsAt;
  }
}
