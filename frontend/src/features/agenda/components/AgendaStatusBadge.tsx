import type { AgendaStatus } from '@/lib/types'

const statusConfig: Record<AgendaStatus, { label: string; className: string }> = {
  CREATED: {
    label: 'Criada',
    className: 'bg-gray-100 text-gray-700 border border-gray-200',
  },
  IN_VOTING: {
    label: 'Em Votação',
    className: 'bg-blue-100 text-blue-700 border border-blue-200',
  },
  CLOSED: {
    label: 'Encerrada',
    className: 'bg-amber-100 text-amber-700 border border-amber-200',
  },
  ARCHIVED: {
    label: 'Arquivada',
    className: 'bg-slate-100 text-slate-600 border border-slate-200',
  },
}

interface Props {
  status: AgendaStatus
}

export function AgendaStatusBadge({ status }: Props) {
  const config = statusConfig[status] ?? statusConfig.CREATED
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ${config.className}`}>
      {status === 'IN_VOTING' && (
        <span className="mr-1.5 h-1.5 w-1.5 rounded-full bg-blue-500 animate-pulse" />
      )}
      {config.label}
    </span>
  )
}
