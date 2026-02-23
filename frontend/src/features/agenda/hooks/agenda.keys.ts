export const agendaKeys = {
  all: ['agendas'] as const,
  lists: () => [...agendaKeys.all, 'list'] as const,
  detail: (id: number) => [...agendaKeys.all, 'detail', id] as const,
}
