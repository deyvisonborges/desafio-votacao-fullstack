package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.BusinessException;
import br.com.deyvisonborges.dbservervotingapi.app.exceptions.ResourceNotFoundException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence.VotingSessionRepositoryService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class CreateVotingSessionHandler {
  private final Logger log = LoggerFactory.getLogger(CreateVotingSessionHandler.class);
  private final AgendaRepositoryService agendaRepositoryService;
  private final VotingSessionRepositoryService repository;
  
  public CreateVotingSessionHandler(
    final AgendaRepositoryService agendaRepositoryService,
    final VotingSessionRepositoryService repository
  ) {
    this.agendaRepositoryService = agendaRepositoryService;
    this.repository = repository;
  }
  
  @Transactional
  public VotingSessionModel execute(final Long agendaId, final Integer durationInMinutes) {
    log.info("Iniciando criação de sessão de votação para a pauta: {}", agendaId);
    
    // 1. Valida se a Agenda existe
    var agenda = agendaRepositoryService.findById(agendaId)
      .orElseThrow(() -> new ResourceNotFoundException("Agenda " + agendaId + " não encontrada"));
    
    // 2. NOVA VALIDAÇÃO: Impede múltiplas sessões ativas para a mesma pauta
    if (repository.existsActiveSessionWithLock(agendaId)) {
      log.warn("Tentativa de criar sessão para pauta {} que já possui sessão ativa", agendaId);
      throw new BusinessException("Já existe uma sessão de votação aberta para esta pauta.");
    }
    
    var startAt = Instant.now();
    var endsAt = startAt.plus(durationInMinutes, ChronoUnit.MINUTES);
    
    var model = VotingSessionModel.create(
      agenda.getId(),
      startAt,
      endsAt
    );
    
    log.info("Salvando nova VotingSessionModel: {}", model);
    return repository.save(model);
  }
}