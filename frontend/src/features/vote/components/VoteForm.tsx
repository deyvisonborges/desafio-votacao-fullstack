'use client'

import { useState } from 'react'
import { useCastVote } from '../hooks/use-vote'
import type { VoteType } from '@/lib/types'

const VALID_CPF_IDS = [
  '52998224725',
  '12345678909',
  '11144477735',
  '93541134780',
  '28625587887',
]

interface Props {
  sessionId: number
  onSuccess: () => void
}

export function VoteForm({ sessionId, onSuccess }: Props) {
  const { mutate, isPending, error, isSuccess } = useCastVote(sessionId)
  const [associatedId, setAssociatedId] = useState('')
  const [selectedVote, setSelectedVote] = useState<VoteType | null>(null)

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!selectedVote) return
    mutate({ associatedId, vote: selectedVote }, { onSuccess })
  }

  if (isSuccess) {
    return (
      <div className="card p-8 text-center">
        <div className="text-4xl mb-3">✅</div>
        <h3 className="text-lg font-bold text-gray-900">Voto registrado!</h3>
        <p className="text-sm text-gray-500 mt-1">Seu voto foi computado com sucesso.</p>
      </div>
    )
  }

  return (
    <form onSubmit={handleSubmit} className="card p-6 space-y-5">
      <div>
        <label className="label" htmlFor="cpf">CPF do Associado</label>
        <input
          id="cpf"
          className="input"
          placeholder="Ex: 52998224725"
          value={associatedId}
          onChange={e => setAssociatedId(e.target.value.replace(/\D/g, ''))}
          maxLength={11}
          required
        />
        <p className="mt-1 text-xs text-gray-400">
          CPFs válidos para teste: {VALID_CPF_IDS.slice(0, 2).join(', ')} ...
        </p>
      </div>

      <div>
        <p className="label">Seu voto</p>
        <div className="grid grid-cols-2 gap-3">
          <button
            type="button"
            onClick={() => setSelectedVote('YES')}
            className={`rounded-xl border-2 py-4 text-base font-bold transition-all ${
              selectedVote === 'YES'
                ? 'border-green-500 bg-green-50 text-green-700 shadow-sm'
                : 'border-gray-200 bg-white text-gray-600 hover:border-green-300 hover:bg-green-50'
            }`}
          >
            👍 Sim
          </button>
          <button
            type="button"
            onClick={() => setSelectedVote('NO')}
            className={`rounded-xl border-2 py-4 text-base font-bold transition-all ${
              selectedVote === 'NO'
                ? 'border-red-500 bg-red-50 text-red-700 shadow-sm'
                : 'border-gray-200 bg-white text-gray-600 hover:border-red-300 hover:bg-red-50'
            }`}
          >
            👎 Não
          </button>
        </div>
      </div>

      {error && (
        <p className="text-sm text-red-600 bg-red-50 rounded-lg px-3 py-2">
          {error.message}
        </p>
      )}

      <button
        type="submit"
        disabled={isPending || !selectedVote || associatedId.length < 11}
        className="btn-primary w-full"
      >
        {isPending ? 'Registrando...' : 'Confirmar Voto'}
      </button>
    </form>
  )
}
