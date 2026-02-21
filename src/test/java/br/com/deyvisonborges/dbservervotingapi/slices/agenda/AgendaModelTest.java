package br.com.deyvisonborges.dbservervotingapi.slices.agenda;

import br.com.deyvisonborges.dbservervotingapi.app.exceptions.BusinessException;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AgendaModel unit tests")
public class AgendaModelTest {
  AgendaModel fakeModel;
  @BeforeEach()
  public void beforeEach() {
    fakeModel = AgendaTestMocks.fakeAgendaModel();
  }
  
  @Test
  @DisplayName("Should create entity with valid data")
  void shouldCreateEntityWithValidData() {
    var agenda = AgendaModel.create(fakeModel.getTitle(), fakeModel.getDescription());
    Assertions.assertNotNull(agenda);
    Assertions.assertNull(agenda.getId());
    Assertions.assertNotNull(agenda.getTitle());
    Assertions.assertNotNull(agenda.getDescription());
    Assertions.assertNotNull(agenda.getStatus());
    Assertions.assertEquals(AgendaStatus.CREATED, agenda.getStatus());
    Assertions.assertEquals("Titulo de teste", agenda.getTitle());
    Assertions.assertEquals("Descricao de teste", agenda.getDescription());
  }
  
  @Test
  @DisplayName("Should create agenda with CREATED status when valid data")
  void shouldCreateAgendaWithCreatedStatusWhenValidData() {
    Assertions.assertNotNull(fakeModel);
    Assertions.assertEquals("Titulo de teste", fakeModel.getTitle());
    Assertions.assertEquals("Descricao de teste", fakeModel.getDescription());
    Assertions.assertEquals(AgendaStatus.CREATED, fakeModel.getStatus());
  }
  
  @Test
  @DisplayName("Should change status to IN_VOTING when status is CREATED")
  void shouldChangeStatusToInVotingWhenStatusIsCreated() {
    fakeModel.startVoting();
    Assertions.assertEquals(AgendaStatus.IN_VOTING, fakeModel.getStatus());
  }
  
  @Test
  @DisplayName("Should throw exception when status is not CREATED")
  void shouldThrowExceptionWhenStatusIsNotCreated() {
    final var agenda = AgendaModel.restore(1L, "Title", "Desc", AgendaStatus.CLOSED);
    Assertions.assertThrows(BusinessException.class, agenda::startVoting);
  }
  
  @Test
  @DisplayName("Should change status to CLOSED when status is IN_VOTING")
  void shouldChangeStatusToClosedWhenStatusIsInVoting() {
    final var agenda = AgendaModel.restore(1L, "Title", "Desc", AgendaStatus.IN_VOTING);
    agenda.close();
    Assertions.assertEquals(AgendaStatus.CLOSED, agenda.getStatus());
  }
  
  @Test
  @DisplayName("Should change status to ARCHIVED when status is CLOSED")
  void shouldChangeStatusToArchivedWhenStatusIsClosed() {
    final var agenda = AgendaModel.restore(1L, "Title", "Desc", AgendaStatus.CLOSED);
    agenda.archive();
    Assertions.assertEquals(AgendaStatus.ARCHIVED, agenda.getStatus());
  }
  
  @Test
  @DisplayName("Should throw exception when status is not CLOSED")
  void shouldThrowExceptionWhenStatusIsNotClosed() {
    final var agenda = AgendaModel.restore(1L, "Title", "Desc", AgendaStatus.IN_VOTING);
    Assertions.assertThrows(BusinessException.class, agenda::archive);
  }
  
  @Test
  @DisplayName("Should update title and description when status is CREATED")
  void shouldUpdateTitleAndDescriptionWhenStatusIsCreated() {
    final var agenda = AgendaModel.create("Old", "OldDesc");
    agenda.update("New", "NewDesc");
    Assertions.assertEquals("New", agenda.getTitle());
    Assertions.assertEquals("NewDesc", agenda.getDescription());
  }
  
  @Test
  @DisplayName("Should throw exception when title is null or blank")
  void shouldThrowExceptionWhenTitleIsNullOrBlank() {
    final var agenda = AgendaModel.create("Title", "Desc");
    Assertions.assertThrows(BusinessException.class,
      () -> agenda.update(null, "Desc"));
    Assertions.assertThrows(BusinessException.class,
      () -> agenda.update("   ", "Desc"));
  }
  
  @Test
  @DisplayName("Should throw exception when description is null or blank")
  void shouldThrowExceptionWhenDescriptionIsNullOrBlank() {
    final var agenda = AgendaModel.create("Title", "Desc");
    Assertions.assertThrows(BusinessException.class,
      () -> agenda.update("Title", null));
    Assertions.assertThrows(BusinessException.class,
      () -> agenda.update("Title", "   "));
  }
}
