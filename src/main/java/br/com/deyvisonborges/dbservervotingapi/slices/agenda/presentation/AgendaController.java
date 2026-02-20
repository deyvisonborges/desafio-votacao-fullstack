package br.com.deyvisonborges.dbservervotingapi.slices.agenda.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.FindAgendaByIdHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.FindAllAgendasHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create.CreateAgendaCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.create.CreateAgendaHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/agendas")
public class AgendaController {
  private final CreateAgendaHandler createAgendaHandler;
  private final FindAllAgendasHandler findAllAgendasHandler;
  private final FindAgendaByIdHandler findAgendaByIdHandler;
  private final UpdateAgendaHandler updateAgendaHandler;
  
  public AgendaController(
    final CreateAgendaHandler createAgendaHandler,
    final FindAllAgendasHandler findAllAgendasHandler,
    final FindAgendaByIdHandler findAgendaByIdHandler,
    final UpdateAgendaHandler updateAgendaHandler
  ) {
    this.createAgendaHandler = createAgendaHandler;
    this.findAllAgendasHandler = findAllAgendasHandler;
    this.findAgendaByIdHandler = findAgendaByIdHandler;
    this.updateAgendaHandler = updateAgendaHandler;
  }
  
  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AgendaResponse> create(@RequestBody final CreateAgendaCommand command) {
    return ResponseEntity.ok().body(
      AgendaPresenter.toResponse(
        createAgendaHandler.execute(command)
      )
    );
  }
  
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AgendaResponse>> list() {
    return ResponseEntity.ok().body(AgendaPresenter.toResponseList(
      findAllAgendasHandler.execute()
    ));
  }
  
  @GetMapping(
    value = "{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AgendaResponse> findById(@PathVariable final Long id) {
    return ResponseEntity.ok().body(
      AgendaPresenter.toResponse(
        findAgendaByIdHandler.execute(id)
      )
    );
  }
  
  @PutMapping(
    value = "{agendaId}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> update(
    @PathVariable final Long agendaId,
    @RequestBody final UpdateAgendaCommand command
  ) {
    updateAgendaHandler.execute(agendaId, command);
    return ResponseEntity.noContent().build();
  }
}
