package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create.CreateVotingSessionCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create.CreateVotingSessionHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.FindVotingSessionHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.GetVotingSessionResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sessions")
public class VotingSessionController {
  private static final Logger log = LoggerFactory.getLogger(VotingSessionController.class);
  private final FindVotingSessionHandler findVotingSessionHandler;
  private final GetVotingSessionResultHandler getVotingSessionResultHandler;
  private final CreateVotingSessionHandler createVotingSessionHandler;
  
  public VotingSessionController(
    final FindVotingSessionHandler findVotingSessionHandler,
    final GetVotingSessionResultHandler getVotingSessionResultHandler,
    final CreateVotingSessionHandler createVotingSessionHandler
  ) {
    this.findVotingSessionHandler = findVotingSessionHandler;
    this.getVotingSessionResultHandler = getVotingSessionResultHandler;
    this.createVotingSessionHandler = createVotingSessionHandler;
  }
  
  @PostMapping("agenda/{agendaId}")
  public ResponseEntity<?> create(
    @PathVariable Long agendaId,
    @RequestBody(required = false) CreateVotingSessionCommand command
  ) {
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
  public ResponseEntity<VotingSessionResponse> result(@PathVariable Long sessionId) {
    var result = getVotingSessionResultHandler.execute(sessionId);
    return ResponseEntity.ok().body(result);
  }
}
