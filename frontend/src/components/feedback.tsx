interface LoadingProps {
  message?: string
}

export function LoadingSpinner({ message = 'Carregando...' }: LoadingProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-16 text-gray-500">
      <div className="h-8 w-8 rounded-full border-4 border-blue-200 border-t-blue-600 animate-spin" />
      <p className="text-sm">{message}</p>
    </div>
  )
}

interface ErrorProps {
  message?: string
  onRetry?: () => void
}

export function ErrorMessage({ message = 'Algo deu errado.', onRetry }: ErrorProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-16 text-red-500">
      <span className="text-3xl">⚠️</span>
      <p className="text-sm font-medium">{message}</p>
      {onRetry && (
        <button onClick={onRetry} className="btn-secondary text-xs mt-1">
          Tentar novamente
        </button>
      )}
    </div>
  )
}

interface EmptyProps {
  message?: string
  icon?: string
}

export function EmptyState({ message = 'Nenhum item encontrado.', icon = '📭' }: EmptyProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-16 text-gray-400">
      <span className="text-4xl">{icon}</span>
      <p className="text-sm">{message}</p>
    </div>
  )
}
