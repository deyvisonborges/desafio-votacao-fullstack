"use client";

import { useRouter } from "next/navigation";
import type { Agenda } from "@/lib/types";
import { AgendaStatusBadge } from "./AgendaStatusBadge";

interface Props {
  agenda: Agenda;
}

export function AgendaCard({ agenda }: Props) {
  const router = useRouter();

  return (
    <div
      onClick={() => router.push(`/agendas/${agenda.id}`)}
      className="card p-5 cursor-pointer hover:shadow-md hover:border-blue-200 transition-all duration-200 group"
    >
      <div className="flex items-start justify-between gap-3">
        <div className="flex-1 min-w-0">
          <h3 className="font-semibold text-gray-900 group-hover:text-blue-700 truncate transition-colors">
            {agenda.title}
          </h3>
          <p className="mt-1 text-sm text-gray-500 line-clamp-2">
            {agenda.description}
          </p>
        </div>
        <AgendaStatusBadge status={agenda.status} />
      </div>
      <div className="mt-4 flex items-center justify-between">
        <span className="text-xs text-gray-400">ID #{agenda.id}</span>
        <span className="text-xs font-medium text-blue-600 group-hover:underline">
          Ver detalhes →
        </span>
      </div>
    </div>
  );
}
