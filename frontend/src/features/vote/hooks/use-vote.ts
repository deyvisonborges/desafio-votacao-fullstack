'use client'

import { useMutation, useQueryClient } from '@tanstack/react-query'
import { voteService } from '../services/vote.service'
import { sessionKeys } from '@/features/session/hooks/session.keys'
import type { CreateVotePayload } from '@/lib/types'

export function useCastVote(sessionId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (payload: CreateVotePayload) => voteService.create(sessionId, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: sessionKeys.result(sessionId) })
      queryClient.invalidateQueries({ queryKey: sessionKeys.detail(sessionId) })
    },
  })
}
