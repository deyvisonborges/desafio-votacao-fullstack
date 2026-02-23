"use client";

import { useState } from "react";
import { useCreateSession } from "../hooks/use-session";

interface Props {
  agendaId: number;
  onSuccess: (sessionId: number) => void;
}

export function CreateSessionForm({ agendaId, onSuccess }: Props) {
  const { mutate, isPending, error } = useCreateSession(agendaId);
  const [duration, setDuration] = useState(15);

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    // Validação: garante que duration seja um número válido entre 1 e 60
    const finalDuration = Math.max(1, Math.min(60, Math.floor(duration) || 5));

    mutate(
      { durationInMinutes: finalDuration },
      {
        onSuccess: (session) => {
          // Aguarda um pouco para garantir que a sessão foi criada com enableToVote=true
          setTimeout(() => onSuccess(session.id), 500);
        },
      },
    );
  }

  function handleDurationChange(e: React.ChangeEvent<HTMLInputElement>) {
    const value = e.target.value;

    // Se o campo estiver vazio, não fazer nada (deixar como está)
    if (value === "") {
      return;
    }

    const numValue = parseInt(value, 10);

    // Validar e limitar entre 1 e 60
    if (!isNaN(numValue)) {
      setDuration(Math.max(1, Math.min(60, numValue)));
    }
  }

  return (
    <div className="card p-6">
      <h2 className="text-base font-semibold text-gray-900 mb-4">
        Abrir Sessão de Votação
      </h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="label" htmlFor="duration">
            Duração (minutos)
          </label>
          <input
            id="duration"
            type="number"
            min={1}
            max={60}
            className="input"
            value={duration}
            onChange={handleDurationChange}
          />
          <p className="mt-1 text-xs text-gray-400">Padrão: 15 minutos</p>
        </div>

        {error && (
          <p className="text-sm text-red-600 bg-red-50 rounded-lg px-3 py-2">
            {error.message}
          </p>
        )}

        <button
          type="submit"
          disabled={isPending}
          className="btn-primary w-full"
        >
          {isPending ? "Abrindo..." : "🗳️  Abrir Sessão"}
        </button>
      </form>
    </div>
  );
}
