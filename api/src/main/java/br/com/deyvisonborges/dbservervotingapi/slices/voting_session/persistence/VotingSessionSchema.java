package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaSchema;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "voting_sessions")
@NamedEntityGraph(
  name = VotingSessionSchema.WITH_AGENDA,
  attributeNodes = @NamedAttributeNode("agenda")
)
public class VotingSessionSchema {
  public static final String WITH_AGENDA = "VotingSession.withAgenda";
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "agenda_id", nullable = false)
  private AgendaSchema agenda;
  
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
  
  public AgendaSchema getAgenda() {
    return agenda;
  }
  
  public void setAgenda(AgendaSchema agenda) {
    this.agenda = agenda;
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

