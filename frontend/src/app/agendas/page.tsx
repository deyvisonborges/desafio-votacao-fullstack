"use client";

import { useState } from "react";
import { useAgendas } from "@/features/agenda/hooks/use-agenda";
import { AgendaCard } from "@/features/agenda/components/AgendaCard";
import { CreateAgendaModal } from "@/features/agenda/components/CreateAgendaModal";
import {
  LoadingSpinner,
  ErrorMessage,
  EmptyState,
} from "@/components/feedback";

export default function AgendasPage() {
  const { data: agendas, isLoading, isError, refetch } = useAgendas();
  const [showCreate, setShowCreate] = useState(false);

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Pautas</h1>
          <p className="text-sm text-gray-500 mt-0.5">
            Gerencie as pautas de votação
          </p>
        </div>
        <button onClick={() => setShowCreate(true)} className="btn-primary">
          + Nova Pauta
        </button>
      </div>

      {isLoading && <LoadingSpinner message="Carregando pautas..." />}

      {isError && (
        <ErrorMessage
          message="Não foi possível carregar as pautas."
          onRetry={() => refetch()}
        />
      )}

      {!isLoading && !isError && agendas && agendas.length === 0 && (
        <EmptyState message="Nenhuma pauta cadastrada ainda." icon="📋" />
      )}

      {!isLoading && !isError && agendas && agendas.length > 0 && (
        <div className="grid gap-3 sm:grid-cols-2">
          {agendas.map((agenda) => (
            <AgendaCard key={agenda.id} agenda={agenda} />
          ))}
        </div>
      )}

      {showCreate && <CreateAgendaModal onClose={() => setShowCreate(false)} />}
    </div>
  );
}
