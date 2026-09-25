package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create;

public record CreateVotingSessionCommand(
  Integer durationInMinutes
) {
  public CreateVotingSessionCommand {
    if (durationInMinutes == null) {
      durationInMinutes = 1;
    }
    if(durationInMinutes < 0) {
      throw new IllegalArgumentException("Duration cannot be negative");
    }
  }
}
