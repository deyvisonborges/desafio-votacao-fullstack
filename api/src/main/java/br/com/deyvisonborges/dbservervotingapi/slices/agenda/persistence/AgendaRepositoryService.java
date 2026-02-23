package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AgendaRepositoryService {
  private final Logger log = LoggerFactory.getLogger(AgendaRepositoryService.class);
  private final AgendaJpaRepository repository;
  
  public AgendaRepositoryService(final AgendaJpaRepository repository) {
    this.repository = repository;
  }
  
  public AgendaModel save(final AgendaModel model) {
    log.info("Creating AgendaModel: {}", model);
    var newAgenda = AgendaMapper.toNewSchema(model);
    var schema = repository.save(newAgenda);
    log.info("Created AgendaModel with id: {}", schema.getId());
    return AgendaMapper.toModel(schema);
  }
  
  public Optional<AgendaModel> findById(final Long id) {
    log.info("Finding agenda by id={}", id);
    return repository.findById(id)
      .map(AgendaMapper::toModel);
  }
  
  public List<AgendaModel> findAll() {
    return repository.findAll()
      .stream()
      .map(AgendaMapper::toModel)
      .toList();
  }
  
  public AgendaModel update(final AgendaModel model) {
    var existing = repository.findById(model.getId())
      .orElseThrow(() -> new ResourceNotFoundException("Agenda not found"));
    var saved = repository.save(
      AgendaMapper.toExistingSchema(model, existing)
    );
    return AgendaMapper.toModel(saved);
  }
  
  public List<AgendaModel> findAllByIds(final Set<Long> agendaIds) {
    return repository.findAllById(agendaIds).stream().map(AgendaMapper::toModel).toList();
  }
}
