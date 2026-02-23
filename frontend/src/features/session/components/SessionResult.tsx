import type { VotingSessionResult } from '@/lib/types'

interface Props {
  result: VotingSessionResult
}

export function SessionResult({ result }: Props) {
  const yesPercent = result.totalVote > 0 ? (result.yesVotes / result.totalVote) * 100 : 0
  const noPercent = result.totalVote > 0 ? (result.noVotes / result.totalVote) * 100 : 0

  const isApproved = result.result?.toUpperCase().includes('SIM') ||
    result.result?.toUpperCase().includes('APPROVED') ||
    result.yesVotes > result.noVotes

  return (
    <div className="card p-5 space-y-4">
      <div className="flex items-center justify-between">
        <h3 className="text-base font-semibold text-gray-900">Resultado da Votação</h3>
        <span className={`inline-flex items-center rounded-full px-3 py-1 text-xs font-bold border ${
          result.open
            ? 'bg-blue-100 text-blue-700 border-blue-200'
            : isApproved
              ? 'bg-green-100 text-green-700 border-green-200'
              : 'bg-red-100 text-red-700 border-red-200'
        }`}>
          {result.open ? 'Em andamento' : result.result ?? (isApproved ? 'Aprovada' : 'Reprovada')}
        </span>
      </div>

      <div className="grid grid-cols-3 gap-3 text-center">
        <div className="bg-gray-50 rounded-xl p-3">
          <p className="text-2xl font-bold text-gray-900">{result.totalVote}</p>
          <p className="text-xs text-gray-500 mt-0.5">Total</p>
        </div>
        <div className="bg-green-50 rounded-xl p-3 border border-green-100">
          <p className="text-2xl font-bold text-green-700">{result.yesVotes}</p>
          <p className="text-xs text-green-600 mt-0.5">Sim</p>
        </div>
        <div className="bg-red-50 rounded-xl p-3 border border-red-100">
          <p className="text-2xl font-bold text-red-700">{result.noVotes}</p>
          <p className="text-xs text-red-600 mt-0.5">Não</p>
        </div>
      </div>

      {result.totalVote > 0 && (
        <div className="space-y-2">
          <div>
            <div className="flex justify-between text-xs mb-1">
              <span className="text-green-600 font-medium">Sim</span>
              <span className="text-green-600 font-medium">{yesPercent.toFixed(1)}%</span>
            </div>
            <div className="h-2 rounded-full bg-gray-100 overflow-hidden">
              <div
                className="h-full rounded-full bg-green-500 transition-all duration-500"
                style={{ width: `${yesPercent}%` }}
              />
            </div>
          </div>
          <div>
            <div className="flex justify-between text-xs mb-1">
              <span className="text-red-600 font-medium">Não</span>
              <span className="text-red-600 font-medium">{noPercent.toFixed(1)}%</span>
            </div>
            <div className="h-2 rounded-full bg-gray-100 overflow-hidden">
              <div
                className="h-full rounded-full bg-red-500 transition-all duration-500"
                style={{ width: `${noPercent}%` }}
              />
            </div>
          </div>
        </div>
      )}

      {result.totalVote === 0 && (
        <p className="text-center text-sm text-gray-400 py-2">Nenhum voto registrado ainda.</p>
      )}
    </div>
  )
}
