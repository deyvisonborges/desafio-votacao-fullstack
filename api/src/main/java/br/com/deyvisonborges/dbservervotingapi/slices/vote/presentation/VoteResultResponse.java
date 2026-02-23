package br.com.deyvisonborges.dbservervotingapi.slices.vote.presentation;

public record VoteResultResponse(
  Long sessionId,
  Long totalVotes,
  Long yesVotes,
  Long noVotes,
  String result
) {}