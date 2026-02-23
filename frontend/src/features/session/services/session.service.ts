import { apiClient } from '@/lib/api-client'
import type { CreateSessionPayload, VotingSession, VotingSessionResult } from '@/lib/types'

export const sessionService = {
  findByAgenda(agendaId: number): Promise<VotingSession> {
    return apiClient.get<VotingSession>(`/sessions/agenda/${agendaId}`)
  },

  findById(sessionId: number): Promise<VotingSession> {
    return apiClient.get<VotingSession>(`/sessions/${sessionId}`)
  },

  existsForAgenda(agendaId: number): Promise<boolean> {
    return apiClient.get<boolean>(`/sessions/agenda/${agendaId}/exists`)
  },

  create(agendaId: number, payload: CreateSessionPayload): Promise<VotingSession> {
    return apiClient.post<VotingSession>(`/sessions/agenda/${agendaId}`, payload)
  },

  getResult(sessionId: number): Promise<VotingSessionResult> {
    return apiClient.get<VotingSessionResult>(`/sessions/${sessionId}/result`)
  },
}
