'use client'

import { useEffect, useState } from 'react'
import type { VotingSession } from '@/lib/types'

interface Props {
  session: VotingSession
}

function useCountdown(endsAt: string) {
  const [remaining, setRemaining] = useState('')

  useEffect(() => {
    function calc() {
      const diff = new Date(endsAt).getTime() - Date.now()
      if (diff <= 0) {
        setRemaining('Encerrada')
        return
      }
      const m = Math.floor(diff / 60000)
      const s = Math.floor((diff % 60000) / 1000)
      setRemaining(`${m}m ${s}s`)
    }
    calc()
    const id = setInterval(calc, 1000)
    return () => clearInterval(id)
  }, [endsAt])

  return remaining
}

export function SessionInfo({ session }: Props) {
  const countdown = useCountdown(session.endsAt)

  return (
    <div className="card p-5 space-y-3">
      <div className="flex items-center justify-between">
        <span className="text-sm font-semibold text-gray-700">Sessão #{session.id}</span>
        {session.enableToVote ? (
          <span className="inline-flex items-center gap-1.5 rounded-full bg-green-100 px-3 py-1 text-xs font-semibold text-green-700 border border-green-200">
            <span className="h-1.5 w-1.5 rounded-full bg-green-500 animate-pulse" />
            Aberta
          </span>
        ) : (
          <span className="inline-flex items-center gap-1.5 rounded-full bg-red-100 px-3 py-1 text-xs font-semibold text-red-700 border border-red-200">
            Encerrada
          </span>
        )}
      </div>

      <div className="grid grid-cols-2 gap-3 text-sm">
        <div className="bg-gray-50 rounded-lg p-3">
          <p className="text-xs text-gray-500 mb-0.5">Início</p>
          <p className="font-medium text-gray-800">{new Date(session.startAt).toLocaleString('pt-BR')}</p>
        </div>
        <div className="bg-gray-50 rounded-lg p-3">
          <p className="text-xs text-gray-500 mb-0.5">Encerramento</p>
          <p className="font-medium text-gray-800">{new Date(session.endsAt).toLocaleString('pt-BR')}</p>
        </div>
      </div>

      {session.enableToVote && (
        <div className="flex items-center justify-center rounded-lg bg-blue-50 border border-blue-200 py-2 gap-2">
          <span className="text-lg">⏱️</span>
          <span className="text-sm font-semibold text-blue-700">Tempo restante: {countdown}</span>
        </div>
      )}
    </div>
  )
}
