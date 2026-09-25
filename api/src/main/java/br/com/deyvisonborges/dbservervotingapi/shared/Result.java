package br.com.deyvisonborges.dbservervotingapi.shared;

import java.util.List;

public sealed interface Result<T> permits Result.Success, Result.Failure {
  
  // Record para Sucesso
  record Success<T>(T data) implements Result<T> {}
  
  // Record para Falha (Abstração da Notificação/RFC 7807)
  record Failure<T>(
    String title,
    int status,
    List<NotificationError> errors
  ) implements Result<T> {
    public Failure(String title, int status, String message) {
      this(title, status, List.of(new NotificationError(null, message)));
    }
  }
  
  // Objeto de erro individual (para logs estruturados e RFC 7807)
  record NotificationError(String field, String message) {}
  
  // --- Métodos Utilitários Estáticos ---
  
  static <T> Result<T> ok(T data) {
    return new Success<>(data);
  }
  
  static <T> Result<T> fail(String title, int status, List<NotificationError> errors) {
    return new Failure<>(title, status, errors);
  }
  
  // --- Fluent API para Transformação ---
  
  default boolean isSuccess() {
    return this instanceof Success;
  }
  
  default T getOrThrow() {
    if (this instanceof Success<T> s) return s.data();
    throw new IllegalStateException("Tentativa de acessar dados em um Result de falha.");
  }
}