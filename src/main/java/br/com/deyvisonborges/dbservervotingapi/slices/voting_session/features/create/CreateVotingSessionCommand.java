package br.com.deyvisonborges.dbservervotingapi.slices.voting_session.features.create;

public record CreateVotingSessionCommand(
  Integer durationInMinutes
) {
  public CreateVotingSessionCommand {
    if (durationInMinutes == null) {
      durationInMinutes = 60;
    }
  }
}
