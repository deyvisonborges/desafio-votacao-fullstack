package br.com.deyvisonborges.dbservervotingapi.slices.agenda.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaJpaRepository extends JpaRepository<AgendaSchema, Long> { }
