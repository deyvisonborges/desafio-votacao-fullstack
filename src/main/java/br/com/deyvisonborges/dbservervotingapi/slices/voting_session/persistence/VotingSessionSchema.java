package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaSchema;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "voting_sessions")
public class VotingSessionSchema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "agenda_id", nullable = false)
  private AgendaSchema agendaId;
  
  @Column(name = "start_at", nullable = false)
  private Instant startAt;
  
  @Column(name = "ends_at", nullable = false)
  private Instant endsAt;
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public AgendaSchema getAgendaId() {
    return agendaId;
  }
  
  public void setAgendaId(AgendaSchema agendaId) {
    this.agendaId = agendaId;
  }
  
  public Instant getStartAt() {
    return startAt;
  }
  
  public void setStartAt(Instant startAt) {
    this.startAt = startAt;
  }
  
  public Instant getEndsAt() {
    return endsAt;
  }
  
  public void setEndsAt(Instant endsAt) {
    this.endsAt = endsAt;
  }
}

