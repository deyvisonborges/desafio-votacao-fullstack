create table agendas (
    id bigserial primary key,
    title varchar(255) not null,
    description text not null,
    status varchar not null,
    created_at timestamptz not null,
    updated_at timestamptz
);

create table voting_sessions (
    id bigserial primary key,
    agenda_id bigint not null,
    start_at timestamptz not null,
    ends_at timestamptz not null,

    constraint fk_session_agenda
        foreign key (agenda_id) references agendas(id)
);

create table votes (
    id bigserial primary key,
    session_id bigint not null,
    associate_id varchar(50) not null,
    vote varchar(10) not null,
    created_at timestamptz not null,

    constraint fk_vote_session
        foreign key (session_id) references voting_sessions(id),

    -- Um mesmo associado só pode votar uma vez por pauta.
    constraint uk_vote_unique
        unique (session_id, associate_id)
);

-- Indice Simples
-- Quando você quiser saber todos os votos de uma pauta específica, você fará um JOIN entre voting_sessions e votes. O índice permite que o banco encontre os votos vinculados àquela sessão instantaneamente.
-- SELECT * FROM votes WHERE session_id = 123;
create index idx_vote_session on votes(session_id);

-- Indice Composto
-- Este é o famoso "Índice de Cobertura" para agregação. Imagine que você precisa contar quantos votos "SIM" e quantos "NÃO" uma sessão teve.
-- Com o índice composto, o valor do voto já está dentro do próprio índice. O banco de dados nem precisa "tocar" na tabela principal; ele resolve tudo lendo apenas a estrutura do índice (isso se chama Index Only Scan).
-- SELECT vote, COUNT(*)
-- FROM votes
-- WHERE session_id = 123
-- GROUP BY vote;
create index idx_vote_session_vote on votes(session_id, vote);
