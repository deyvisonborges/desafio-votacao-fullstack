"use client";

import { useRouter } from "next/navigation";
import {
  useSessionByAgenda,
  useSessionResult,
} from "@/features/session/hooks/use-session";
import { SessionInfo } from "@/features/session/components/SessionInfo";
import { SessionResult } from "@/features/session/components/SessionResult";
import { LoadingSpinner, ErrorMessage } from "@/components/feedback";

interface Props {
  params: { id: string };
}

export default function SessionPage({ params }: Props) {
  const router = useRouter();
  const agendaId = Number(params.id);
  const {
    data: session,
    isLoading,
    isError,
    refetch,
  } = useSessionByAgenda(agendaId);
  const { data: result, isLoading: resultLoading } = useSessionResult(
    session?.id ?? 0,
  );

  if (isLoading) return <LoadingSpinner message="Carregando sessão..." />;
  if (isError || !session)
    return (
      <ErrorMessage
        message="Sessão não encontrada para esta pauta."
        onRetry={() => refetch()}
      />
    );

  return (
    <div className="space-y-4">
      <button
        onClick={() => router.push(`/agendas/${agendaId}`)}
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800 transition-colors"
      >
        ← Voltar para pauta
      </button>

      <div className="flex items-center justify-between">
        <h1 className="text-xl font-bold text-gray-900">Sessão de Votação</h1>
        <button
          onClick={() =>
            router.push(`/agendas/${agendaId}/session/${session.id}/vote`)
          }
          disabled={!session.enableToVote}
          className={
            session.enableToVote
              ? "btn-success text-sm"
              : "btn-secondary text-sm opacity-60 cursor-not-allowed"
          }
        >
          {session.enableToVote ? "🗳️ Votar agora" : "🔒 Votação encerrada"}
        </button>
      </div>

      <SessionInfo session={session} />

      {resultLoading && <LoadingSpinner message="Carregando resultado..." />}
      {!resultLoading && result && <SessionResult result={result} />}
    </div>
  );
}
