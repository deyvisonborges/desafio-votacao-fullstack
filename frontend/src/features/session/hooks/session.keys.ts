export const sessionKeys = {
  all: ['sessions'] as const,
  byAgenda: (agendaId: number) => [...sessionKeys.all, 'agenda', agendaId] as const,
  detail: (sessionId: number) => [...sessionKeys.all, 'detail', sessionId] as const,
  exists: (agendaId: number) => [...sessionKeys.all, 'exists', agendaId] as const,
  result: (sessionId: number) => [...sessionKeys.all, 'result', sessionId] as const,
}
