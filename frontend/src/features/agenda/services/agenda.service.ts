import { apiClient } from '@/lib/api-client'
import type { Agenda, CreateAgendaPayload, UpdateAgendaPayload } from '@/lib/types'

export const agendaService = {
  findAll(): Promise<Agenda[]> {
    return apiClient.get<Agenda[]>('/agendas')
  },

  findById(id: number): Promise<Agenda> {
    return apiClient.get<Agenda>(`/agendas/${id}`)
  },

  create(payload: CreateAgendaPayload): Promise<Agenda> {
    return apiClient.post<Agenda>('/agendas', payload)
  },

  update(id: number, payload: UpdateAgendaPayload): Promise<void> {
    return apiClient.put<void>(`/agendas/${id}`, payload)
  },
}
