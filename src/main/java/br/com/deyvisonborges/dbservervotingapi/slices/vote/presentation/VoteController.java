package br.com.deyvisonborges.dbservervotingapi.slices.vote.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.VoteModel;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.features.save.CreateVoteCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.vote.features.save.CreateVoteHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/votes")
public class VoteController {
  private final CreateVoteHandler createVoteHandler;
  
  public VoteController(final CreateVoteHandler createVoteHandler) {
    this.createVoteHandler = createVoteHandler;
  }
  
  @PostMapping
  public ResponseEntity<VoteModel> create(
    @PathVariable final Long sessionId,
    @RequestBody final CreateVoteCommand command
  ) {
    return ResponseEntity.ok(createVoteHandler.save(
      VoteModel.create(sessionId, command.associatedId(), command.vote())
    ));
  }
}
