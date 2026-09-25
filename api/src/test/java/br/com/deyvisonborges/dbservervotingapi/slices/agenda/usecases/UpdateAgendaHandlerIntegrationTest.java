package br.com.deyvisonborges.dbservervotingapi.slices.agenda.usecases;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaCommand;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.features.update.UpdateAgendaHandler;
import br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence.AgendaRepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
class UpdateAgendaHandlerIntegrationTest {
  
  @Autowired
  private UpdateAgendaHandler handler;
  
  @Autowired
  private AgendaRepositoryService repository;
  
  @Test
  void shouldThrowOptimisticLockExceptionWhenConcurrentUpdate() throws Exception {
    
    AgendaModel agenda = repository.save(
      AgendaModel.create("Title", "Desc")
    );
    
    ExecutorService executor = Executors.newFixedThreadPool(2);
    
    Callable<Void> task1 = () -> {
      handler.execute(agenda.getId(),
        new UpdateAgendaCommand("Title 1", "Desc"));
      return null;
    };
    
    Callable<Void> task2 = () -> {
      handler.execute(agenda.getId(),
        new UpdateAgendaCommand("Title 2", "Desc"));
      return null;
    };
    
    var futures = executor.invokeAll(List.of(task1, task2));
    
    int failures = 0;
    
    for (Future<Void> future : futures) {
      try {
        future.get();
      } catch (ExecutionException e) {
        if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
          failures++;
        }
      }
    }
    
    Assertions.assertEquals(1, failures);
  }
}