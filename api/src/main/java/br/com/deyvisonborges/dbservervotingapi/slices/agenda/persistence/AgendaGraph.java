package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.AgendaModel;
import br.com.deyvisonborges.dbservervotingapi.slices.voting_session.VotingSessionModel;

public record AgendaGraph(AgendaModel agenda, VotingSessionModel session, long voteCount) {
}
