package br.com.deyvisonborges.dbservervotingapi.slices.vote.presentation;

import br.com.deyvisonborges.dbservervotingapi.slices.vote.VoteModel;

public final class VotePresenter {
  public static VoteCreatedResponse toCreatedResponse(final VoteModel model) {
    return new VoteCreatedResponse(
      model.getSessionId(),
      model.getAssociateId(),
      model.getVote()
    );
  }
  
  private static String calculateResult(long yes, long no) {
    if (yes > no) return "APPROVED";
    if (no > yes) return "REJECTED";
    return "TIED";
  }
}
