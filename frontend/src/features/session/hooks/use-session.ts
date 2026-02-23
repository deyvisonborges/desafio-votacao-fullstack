"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { sessionService } from "../services/session.service";
import { sessionKeys } from "./session.keys";
import { agendaKeys } from "@/features/agenda/hooks/agenda.keys";
import type { CreateSessionPayload } from "@/lib/types";

export function useSessionByAgenda(agendaId: number) {
  return useQuery({
    queryKey: sessionKeys.byAgenda(agendaId),
    queryFn: () => sessionService.findByAgenda(agendaId),
    enabled: !!agendaId,
    staleTime: 10_000, // 10s - mantém dados frescos sem refetchar constantemente
    gcTime: 5 * 60 * 1000, // 5 min - guarda em cache
    refetchInterval: (query) => {
      // Poll less aggressively to avoid detecting premature expiration
      const data = query.state.data;
      if (!data) return false;
      // Poll every 15s when session exists
      return 15_000;
    },
    retry: (failureCount, error) => {
      // Don't retry on 404 (no session yet)
      if ((error as { status?: number })?.status === 404) return false;
      return failureCount < 1;
    },
  });
}

export function useSession(sessionId: number) {
  return useQuery({
    queryKey: sessionKeys.detail(sessionId),
    queryFn: () => sessionService.findById(sessionId),
    enabled: !!sessionId,
    staleTime: 8_000, // 8s - mantém dados frescos
    gcTime: 5 * 60 * 1000, // 5 min
    refetchInterval: (query) => {
      // Poll every 10s while session is open to detect closing but not too aggressively
      const data = query.state.data;
      if (!data) return false;
      return data.enableToVote ? 10_000 : false;
    },
  });
}

export function useSessionExists(agendaId: number) {
  return useQuery({
    queryKey: sessionKeys.exists(agendaId),
    queryFn: () => sessionService.existsForAgenda(agendaId),
    enabled: !!agendaId,
  });
}

export function useSessionResult(sessionId: number) {
  return useQuery({
    queryKey: sessionKeys.result(sessionId),
    queryFn: () => sessionService.getResult(sessionId),
    enabled: !!sessionId,
  });
}

export function useCreateSession(agendaId: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (payload: CreateSessionPayload) =>
      sessionService.create(agendaId, payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: sessionKeys.byAgenda(agendaId),
      });
      queryClient.invalidateQueries({ queryKey: sessionKeys.exists(agendaId) });
      queryClient.invalidateQueries({ queryKey: agendaKeys.detail(agendaId) });
      queryClient.setQueryData(sessionKeys.detail(data.id), data);
    },
  });
}
