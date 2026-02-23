package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.*;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create.CreateVotingSessionCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create.CreateVotingSessionHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation.responses.VotingSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sessions")
public class VotingSessionController {
  private static final Logger log = LoggerFactory.getLogger(VotingSessionController.class);
  private final FindVotingSessionHandler findVotingSessionHandler;
  private final FindAllVotingSessionsHandler findAllVotingSessionsHandler;
  private final FindVotingSessionByAgendaHandler findVotingSessionByAgendaHandler;
  private final GetVotingSessionVotesResult getVotingSessionVotesResult;
  private final CreateVotingSessionHandler createVotingSessionHandler;
  private final CheckIfExistsSessionToAgendaHandler checkIfExistsSessionToAgendaHandler;

  public VotingSessionController(
    final FindVotingSessionHandler findVotingSessionHandler, FindAllVotingSessionsHandler findAllVotingSessionsHandler,
    final FindVotingSessionByAgendaHandler findVotingSessionByAgendaHandler,
    final GetVotingSessionVotesResult getVotingSessionVotesResult,
    final CreateVotingSessionHandler createVotingSessionHandler,
    final CheckIfExistsSessionToAgendaHandler checkIfExistsSessionToAgendaHandler
  ) {
    this.findVotingSessionHandler = findVotingSessionHandler;
    this.findAllVotingSessionsHandler = findAllVotingSessionsHandler;
    this.findVotingSessionByAgendaHandler = findVotingSessionByAgendaHandler;
    this.getVotingSessionVotesResult = getVotingSessionVotesResult;
    this.createVotingSessionHandler = createVotingSessionHandler;
    this.checkIfExistsSessionToAgendaHandler = checkIfExistsSessionToAgendaHandler;
  }

  @GetMapping
  public ResponseEntity<List<VotingSessionResponse>> findAllVotingSessions() {
    return ResponseEntity.ok().body(findAllVotingSessionsHandler.execute());
  }
  
  @PostMapping("agenda/{agendaId}")
  public ResponseEntity<?> create(
      @PathVariable Long agendaId,
      @RequestBody(required = false) CreateVotingSessionCommand command) {
    log.info("Received request to create a new voting session for agenda {}", agendaId);
    var session = createVotingSessionHandler.execute(agendaId, command.durationInMinutes());
    log.info("Successfully created a new voting session for agenda {}", agendaId);
    return ResponseEntity.status(HttpStatus.OK).body(session);
  }
  
  @GetMapping("/{sessionId}")
  public ResponseEntity<VotingSessionModel> findById(@PathVariable Long sessionId) {
    return ResponseEntity.ok().body(findVotingSessionHandler.execute(sessionId));
  }

  @GetMapping("/{sessionId}/result")
  public ResponseEntity<VotingSessionVotesResult> result(@PathVariable Long sessionId) {
    var result = getVotingSessionVotesResult.execute(sessionId);
    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/agenda/{agendaId}")
  public ResponseEntity<?> findByAgenda(@PathVariable Long agendaId) {
    var result = findVotingSessionByAgendaHandler.execute(agendaId);
    return ResponseEntity.ok().body(result);
  }
  
  @GetMapping("/agenda/{agendaId}/exists")
  public ResponseEntity<Boolean> existsSessionToAgenda(@PathVariable Long agendaId) {
    var result = checkIfExistsSessionToAgendaHandler.execute(agendaId);
    return ResponseEntity.ok().body(result);
  }
}
