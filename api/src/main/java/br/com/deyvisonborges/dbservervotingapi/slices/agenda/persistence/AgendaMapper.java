package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;

public final class AgendaMapper {
  public static AgendaSchema toNewSchema(final AgendaModel model) {
    var schema = new AgendaSchema();
    schema.setTitle(model.getTitle());
    schema.setDescription(model.getDescription());
    schema.setStatus(model.getStatus());
    return schema;
  }
  
  public static AgendaSchema toExistingSchema(final AgendaModel model, final AgendaSchema schema) {
    schema.setTitle(model.getTitle());
    schema.setDescription(model.getDescription());
    schema.setStatus(model.getStatus());
    return schema;
  }
  
  public static AgendaModel toModel(final AgendaSchema schema) {
    return AgendaModel.restore(
      schema.getId(),
      schema.getTitle(),
      schema.getDescription(),
      schema.getStatus()
    );
  }
}
