package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "agendas")
public class AgendaSchema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String title;
  
  @Column(nullable = false)
  private String description;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AgendaStatus status;
  
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;
  
  @Column(name = "updated_at")
  private Instant updatedAt;
  
  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
    this.status = AgendaStatus.CREATED;
  }
  
  @PreUpdate
  public void preUpdate() {
    this.updatedAt = Instant.now();
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public AgendaStatus getStatus() {
    return status;
  }
  
  public void setStatus(AgendaStatus status) {
    this.status = status;
  }
  
  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
  
  public Instant getUpdatedAt() {
    return updatedAt;
  }
  
  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
