package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.persistence.VoteResultProjection;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;

import java.time.Instant;

public final class VotingSessionPresenter {
  public static VotingSessionVotesResult toVotingSessionVotesResult(
    final VotingSessionModel model,
    final VoteResultProjection projection
  ) {
    long total = 0;
    long yes = 0;
    long no = 0;
    String result = null;
    
    if(projection != null) {
      total = projection.getTotal();
      yes = projection.getYesCount();
      no = projection.getNoCount();
      result = calculateResult(yes, no);
    }
    
    var isOpen = Instant.now().isBefore(model.getEndsAt());
    return new VotingSessionVotesResult(
      model.getId(),
      model.getAgendaId(),
      isOpen,
      (int) total,
      (int) yes,
      (int) no,
      result,
      model.getStartAt()
    );
  }
  
  private static String calculateResult(long yes, long no) {
    if (yes > no) return "APPROVED";
    if (no > yes) return "REJECTED";
    return "TIED";
  }
}
