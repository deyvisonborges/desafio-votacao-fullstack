package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.configs.AbstractJpaTest;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgendaJpaRepositoryTest extends AbstractJpaTest {
  @Autowired
  private AgendaJpaRepository repository;
  
  @Test
  @DisplayName("Should persist agenda with defaults values")
  void shouldPersistAgendaWithDefaultValues() {
    var schema = new AgendaSchema();
    schema.setTitle("Assembleia Geral");
    schema.setDescription("Pauta sobre condomínio");
    
    var saved = repository.save(schema);
    
    Assertions.assertNotNull(saved);
    Assertions.assertNotNull(saved.getId());
    Assertions.assertNotNull(saved.getStatus());
    Assertions.assertNotNull(saved.getCreatedAt());
    Assertions.assertEquals(AgendaStatus.CREATED, saved.getStatus());
  }
}
