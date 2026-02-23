"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAgenda } from "@/features/agenda/hooks/use-agenda";
import {
  useSessionByAgenda,
  useSessionExists,
} from "@/features/session/hooks/use-session";
import { AgendaStatusBadge } from "@/features/agenda/components/AgendaStatusBadge";
import { UpdateAgendaModal } from "@/features/agenda/components/UpdateAgendaModal";
import { SessionInfo } from "@/features/session/components/SessionInfo";
import { CreateSessionForm } from "@/features/session/components/CreateSessionForm";
import { LoadingSpinner, ErrorMessage } from "@/components/feedback";

interface Props {
  params: { id: string };
}

export default function AgendaDetailPage({ params }: Props) {
  const router = useRouter();
  const agendaId = Number(params.id);
  const { data: agenda, isLoading, isError, refetch } = useAgenda(agendaId);
  const { data: sessionExists } = useSessionExists(agendaId);
  const { data: session, isLoading: sessionLoading } =
    useSessionByAgenda(agendaId);
  const [showEdit, setShowEdit] = useState(false);
  const [showCreateSession, setShowCreateSession] = useState(false);

  if (isLoading) return <LoadingSpinner message="Carregando pauta..." />;
  if (isError || !agenda)
    return (
      <ErrorMessage message="Pauta não encontrada." onRetry={() => refetch()} />
    );

  const canEdit = agenda.status === "CREATED";
  const canOpenSession = agenda.status === "CREATED" && !sessionExists;
  const hasActiveSession = session?.enableToVote;

  return (
    <div className="space-y-4">
      {/* Back */}
      <button
        onClick={() => router.push("/agendas")}
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800 transition-colors"
      >
        ← Voltar para pautas
      </button>

      {/* Header */}
      <div className="card p-6">
        <div className="flex items-start justify-between gap-4">
          <div className="flex-1">
            <h1 className="text-xl font-bold text-gray-900">{agenda.title}</h1>
            <p className="mt-2 text-gray-600 text-sm leading-relaxed">
              {agenda.description}
            </p>
          </div>
          <AgendaStatusBadge status={agenda.status} />
        </div>
        <div className="mt-4 flex items-center gap-3">
          {canEdit && (
            <button
              onClick={() => setShowEdit(true)}
              className="btn-secondary text-xs"
            >
              ✏️ Editar
            </button>
          )}
          {session && (
            <button
              onClick={() =>
                router.push(`/agendas/${agendaId}/session/${session.id}/vote`)
              }
              disabled={!hasActiveSession}
              className={
                hasActiveSession
                  ? "btn-success text-xs"
                  : "btn-secondary text-xs opacity-60"
              }
            >
              {hasActiveSession ? "🗳️ Votar" : "🗳️ Votação encerrada"}
            </button>
          )}
          {session && (
            <button
              onClick={() => router.push(`/agendas/${agendaId}/session`)}
              className="btn-secondary text-xs"
            >
              📊 Ver sessão
            </button>
          )}
          {session && (
            <button
              onClick={() => setShowCreateSession(true)}
              className="btn-secondary text-xs"
            >
              ➕ Nova Sessão
            </button>
          )}
        </div>
      </div>

      {/* Session section */}
      {sessionLoading && <LoadingSpinner message="Verificando sessão..." />}

      {!sessionLoading && session && <SessionInfo session={session} />}

      {!sessionLoading && canOpenSession && (
        <CreateSessionForm
          agendaId={agendaId}
          onSuccess={(sessionId) =>
            router.push(`/agendas/${agendaId}/session/${sessionId}/vote`)
          }
        />
      )}

      {showCreateSession && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-md w-full">
            <div className="flex items-center justify-between p-6 border-b">
              <h2 className="text-lg font-semibold">Criar Nova Sessão</h2>
              <button
                onClick={() => setShowCreateSession(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                ✕
              </button>
            </div>
            <div className="p-6">
              <CreateSessionForm
                agendaId={agendaId}
                onSuccess={(sessionId) => {
                  setShowCreateSession(false);
                  router.push(`/agendas/${agendaId}/session/${sessionId}/vote`);
                }}
              />
            </div>
          </div>
        </div>
      )}

      {!sessionLoading &&
        !canOpenSession &&
        !session &&
        agenda.status !== "CREATED" && (
          <div className="card p-5 text-center text-sm text-gray-500">
            Esta pauta não possui sessão de votação.
          </div>
        )}

      {showEdit && agenda && (
        <UpdateAgendaModal agenda={agenda} onClose={() => setShowEdit(false)} />
      )}
    </div>
  );
}
