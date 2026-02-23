"use client";

import { useRouter } from "next/navigation";
import {
  useSession,
  useSessionResult,
} from "@/features/session/hooks/use-session";
import { VoteForm } from "@/features/vote/components/VoteForm";
import { SessionResult } from "@/features/session/components/SessionResult";
import { AgendaStatusBadge } from "@/features/agenda/components/AgendaStatusBadge";
import { LoadingSpinner, ErrorMessage } from "@/components/feedback";

interface Props {
  params: { id: string; sessionId: string };
}

export default function VotePage({ params }: Props) {
  const router = useRouter();
  const agendaId = Number(params.id);
  const sessionId = Number(params.sessionId);

  const { data: session, isLoading, isError, refetch } = useSession(sessionId);
  const { data: result } = useSessionResult(sessionId);

  if (isLoading) return <LoadingSpinner message="Carregando sessão..." />;
  if (isError || !session)
    return (
      <ErrorMessage
        message="Sessão não encontrada."
        onRetry={() => refetch()}
      />
    );

  return (
    <div className="space-y-4">
      <button
        onClick={() => router.push(`/agendas/${agendaId}/session`)}
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800 transition-colors"
      >
        ← Voltar para sessão
      </button>

      {/* Agenda info */}
      <div className="card p-5">
        <div className="flex items-start justify-between gap-3">
          <div>
            <p className="text-xs text-gray-500 mb-1">Pauta em votação</p>
            {/* <h1 className="text-lg font-bold text-gray-900">{session.agenda.title}</h1> */}
            <p className="text-sm text-gray-500 mt-1">
              {/* {session.agenda.description} */}
            </p>
          </div>
          {/* <AgendaStatusBadge status={session.agenda.status} /> */}
        </div>
      </div>

      {/* Voting or closed state */}
      {session.enableToVote || result?.open ? (
        <VoteForm sessionId={sessionId} onSuccess={() => refetch()} />
      ) : (
        <div className="card p-6 text-center space-y-2">
          <div className="text-4xl">🔒</div>
          <h2 className="font-semibold text-gray-900">Votação encerrada</h2>
          <p className="text-sm text-gray-500">
            Esta sessão não está mais aceitando votos.
          </p>
        </div>
      )}

      {/* Result */}
      {result && <SessionResult result={result} />}
    </div>
  );
}
