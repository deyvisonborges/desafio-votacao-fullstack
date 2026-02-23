'use client'

import { useState } from 'react'
import { useCreateAgenda } from '../hooks/use-agenda'
import type { CreateAgendaPayload } from '@/lib/types'

interface Props {
  onClose: () => void
}

export function CreateAgendaModal({ onClose }: Props) {
  const { mutate, isPending, error } = useCreateAgenda()
  const [form, setForm] = useState<CreateAgendaPayload>({ title: '', description: '' })

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    mutate(form, { onSuccess: onClose })
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm">
      <div className="card w-full max-w-md p-6 shadow-xl">
        <div className="flex items-center justify-between mb-5">
          <h2 className="text-lg font-bold text-gray-900">Nova Pauta</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-xl leading-none">✕</button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="label" htmlFor="title">Título</label>
            <input
              id="title"
              className="input"
              placeholder="Ex: Aprovação do orçamento 2025"
              value={form.title}
              onChange={e => setForm(f => ({ ...f, title: e.target.value }))}
              required
            />
          </div>

          <div>
            <label className="label" htmlFor="description">Descrição</label>
            <textarea
              id="description"
              className="input resize-none"
              rows={3}
              placeholder="Descreva a pauta em detalhes..."
              value={form.description}
              onChange={e => setForm(f => ({ ...f, description: e.target.value }))}
              required
            />
          </div>

          {error && (
            <p className="text-sm text-red-600 bg-red-50 rounded-lg px-3 py-2">
              {error.message}
            </p>
          )}

          <div className="flex gap-3 pt-1">
            <button type="button" onClick={onClose} className="btn-secondary flex-1">
              Cancelar
            </button>
            <button type="submit" disabled={isPending} className="btn-primary flex-1">
              {isPending ? 'Criando...' : 'Criar Pauta'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
