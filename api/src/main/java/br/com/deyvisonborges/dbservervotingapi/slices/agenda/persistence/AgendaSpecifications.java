package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import br.com.deyvisonborges.dbservervotingapi.slices.agenda.constants.AgendaStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

/** Reusable predicates (Specification pattern) that can be combined with .and()/.or()/.not(). */
public final class AgendaSpecifications {
  private AgendaSpecifications() { }

  public static Specification<AgendaSchema> statusEquals(final AgendaStatus status) {
    return (root, query, cb) -> cb.equal(root.get("status"), status);
  }

  public static Specification<AgendaSchema> titleContains(final String text) {
    return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + text.toLowerCase() + "%");
  }

  public static Specification<AgendaSchema> createdAfter(final Instant instant) {
    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), instant);
  }

  public static Specification<AgendaSchema> createdBefore(final Instant instant) {
    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), instant);
  }

  public static Specification<AgendaSchema> fromFilter(final AgendaFilter filter) {
    Specification<AgendaSchema> spec = Specification.unrestricted();
    if (filter.status() != null) spec = spec.and(statusEquals(filter.status()));
    if (filter.titleContains() != null) spec = spec.and(titleContains(filter.titleContains()));
    if (filter.createdAfter() != null) spec = spec.and(createdAfter(filter.createdAfter()));
    if (filter.createdBefore() != null) spec = spec.and(createdBefore(filter.createdBefore()));
    return spec;
  }
}
