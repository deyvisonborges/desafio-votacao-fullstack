import { apiClient } from '@/lib/api-client'
import type { CreateVotePayload, VoteCreated } from '@/lib/types'

export const voteService = {
  create(sessionId: number, payload: CreateVotePayload): Promise<VoteCreated> {
    return apiClient.post<VoteCreated>(`/sessions/${sessionId}/votes`, payload)
  },
}
