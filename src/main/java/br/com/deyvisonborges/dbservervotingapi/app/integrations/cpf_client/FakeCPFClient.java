package br.com.deyvisonborges.dbservervotingapi.app.integrations.cpf_client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("external/cpf")
public class FakeCPFClient {
  private static final Set<String> VALID_CPFS = Set.of(
    "52998224725",
    "12345678909",
    "11144477735",
    "93541134780",
    "28625587887"
  );
  
  @GetMapping("/{cpf}")
  public ResponseEntity<FakeCPFResponse> validate(@PathVariable String cpf) {
    if (!VALID_CPFS.contains(cpf))
      return ResponseEntity.notFound().build();
    boolean canVote = ThreadLocalRandom.current().nextBoolean();
    return ResponseEntity.ok(new FakeCPFResponse(canVote ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE"));
  }
}
