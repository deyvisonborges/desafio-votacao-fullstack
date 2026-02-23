// ─── Agenda ────────────────────────────────────────────────────────────────

export type AgendaStatus = "CREATED" | "IN_VOTING" | "CLOSED" | "ARCHIVED";

export interface Agenda {
  id: number;
  title: string;
  description: string;
  status: AgendaStatus;
}

export interface CreateAgendaPayload {
  title: string;
  description: string;
}

export interface UpdateAgendaPayload {
  title: string;
  description: string;
}

// ─── Voting Session ────────────────────────────────────────────────────────

export interface VotingSession {
  id: number;
  startAt: string;
  endsAt: string;
  agenda: Agenda;
  enableToVote: boolean;
  open?: boolean;
}

export interface CreateSessionPayload {
  durationInMinutes: number;
}

export interface VotingSessionResult {
  id: number;
  agendaId: number;
  open: boolean;
  totalVote: number;
  yesVotes: number;
  noVotes: number;
  result: string;
  createdAt: string;
}

// ─── Vote ──────────────────────────────────────────────────────────────────

export type VoteType = "YES" | "NO";

export interface CreateVotePayload {
  associatedId: string;
  vote: VoteType;
}

export interface VoteCreated {
  sessionId: number;
  associateId: string;
  vote: VoteType;
}
