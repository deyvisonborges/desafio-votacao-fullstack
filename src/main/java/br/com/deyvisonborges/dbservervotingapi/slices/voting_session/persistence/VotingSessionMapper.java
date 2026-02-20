package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaSchema;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;

public final class VotingSessionMapper {
  public static VotingSessionSchema toNewSchema(final VotingSessionModel model) {
    var schema = new VotingSessionSchema();
    var agenda = new AgendaSchema();
    agenda.setId(model.getAgendaId());
    schema.setAgendaId(agenda);
    schema.setStartAt(model.getStartAt());
    schema.setEndsAt(model.getEndsAt());
    return schema;
  }
  
  public static VotingSessionModel toModel(final VotingSessionSchema schema) {
    return VotingSessionModel.restore(
      schema.getId(),
      schema.getAgendaId().getId(),
      schema.getStartAt(),
      schema.getEndsAt()
    );
  }
}
