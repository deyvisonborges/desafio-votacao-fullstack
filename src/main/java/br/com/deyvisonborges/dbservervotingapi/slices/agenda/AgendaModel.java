package br.com.deyvisonborges.dbservervotingapi.slices.agenda;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.BusinessException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;

public class AgendaModel {
  private final Long id;
  private String title;
  private String description;
  private AgendaStatus status;
  
  public AgendaModel(
    final Long id,
    final String title,
    final String description,
    final AgendaStatus status
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
  }
  
  public static AgendaModel create(
    final String title,
    final String description
  ) {
    return new AgendaModel(null, title, description, AgendaStatus.CREATED);
  }
  
  public static AgendaModel restore(
    final Long id,
    final String title,
    final String description,
    final AgendaStatus status
  ) {
    return new AgendaModel(id, title, description, status);
  }
  
  public void startVoting() {
    if (this.status != AgendaStatus.CREATED) {
      throw new BusinessException("Agenda cannot start voting");
    }
    this.status = AgendaStatus.IN_VOTING;
  }
  
  public void close() {
    if (this.status != AgendaStatus.IN_VOTING) {
      throw new BusinessException("Agenda is not in voting");
    }
    this.status = AgendaStatus.CLOSED;
  }
  
  public void archive() {
    if (this.status != AgendaStatus.CLOSED) {
      throw new BusinessException("Only closed agendas can be archived");
    }
    this.status = AgendaStatus.ARCHIVED;
  }
  
  public void update(
    final String title,
    final String description
  ) {
    if (this.status != AgendaStatus.CREATED) {
      throw new BusinessException("Cannot update agenda after voting started");
    }
    
    if(title == null || title.isBlank()) {
      throw new BusinessException("Title cannot be null or blank");
    }
    
    if(description == null || description.isBlank()) {
      throw new BusinessException("Description cannot be null or blank");
    }
    this.title = title;
    this.description = description;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getDescription() {
    return description;
  }
  
  public Long getId() {
    return id;
  }
  
  public AgendaStatus getStatus() {
    return status;
  }
}