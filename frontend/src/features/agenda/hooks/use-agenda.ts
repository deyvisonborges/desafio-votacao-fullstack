'use client'

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { agendaService } from '../services/agenda.service'
import { agendaKeys } from './agenda.keys'
import type { CreateAgendaPayload, UpdateAgendaPayload } from '@/lib/types'

export function useAgendas() {
  return useQuery({
    queryKey: agendaKeys.lists(),
    queryFn: agendaService.findAll,
  })
}

export function useAgenda(id: number) {
  return useQuery({
    queryKey: agendaKeys.detail(id),
    queryFn: () => agendaService.findById(id),
    enabled: !!id,
  })
}

export function useCreateAgenda() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (payload: CreateAgendaPayload) => agendaService.create(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: agendaKeys.lists() })
    },
  })
}

export function useUpdateAgenda(id: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (payload: UpdateAgendaPayload) => agendaService.update(id, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: agendaKeys.lists() })
      queryClient.invalidateQueries({ queryKey: agendaKeys.detail(id) })
    },
  })
}
