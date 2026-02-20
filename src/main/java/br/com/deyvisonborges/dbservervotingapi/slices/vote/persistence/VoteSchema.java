package br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionSchema;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
  name = "votes",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"session_id", "associate_id"})
  }
)
public class VoteSchema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id", nullable = false)
  private VotingSessionSchema session;
  
  @Column(name = "associate_id", nullable = false, length = 50)
  private String associateId;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private VoteType vote;
  
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;
  
  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public VotingSessionSchema getSession() {
    return session;
  }
  
  public void setSession(VotingSessionSchema session) {
    this.session = session;
  }
  
  public String getAssociateId() {
    return associateId;
  }
  
  public void setAssociateId(String associateId) {
    this.associateId = associateId;
  }
  
  public VoteType getVote() {
    return vote;
  }
  
  public void setVote(VoteType vote) {
    this.vote = vote;
  }
  
  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
