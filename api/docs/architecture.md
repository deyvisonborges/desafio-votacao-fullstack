# Arquitetura — dbserver-voting-api

Arquitetura em **Vertical Slices** (organizada por funcionalidade/domínio, não por camada técnica), sobre Spring Boot + PostgreSQL.

## Visão geral em camadas

```mermaid
flowchart TB
    subgraph Client["Cliente"]
        HTTP["HTTP Request"]
    end

    subgraph App["app/ (infraestrutura transversal)"]
        Security["security/jwt\nSecurityConfig, JwtService"]
        Cors["configs/CorsConfig"]
        OpenApi["configs/OpenApiConfig"]
        Exceptions["exceptions/\nGlobalExceptionHandler, BusinessException,\nResourceNotFoundException, ErrorResponse"]
        CpfClient["integrations/cpf_client\nFakeCPFClient"]
    end

    subgraph Slices["slices/ (vertical slices por domínio)"]
        subgraph Agenda["agenda"]
            AgendaController["AgendaController"]
            AgendaPresenter["AgendaPresenter / AgendaResponse"]
            AgendaHandlers["Handlers\nCreateAgendaHandler, UpdateAgendaHandler,\nFindAgendaByIdHandler, FindAllAgendasHandler"]
            AgendaModel["AgendaModel / AgendaStatus"]
            AgendaPersistence["AgendaRepositoryService\nAgendaJpaRepository, AgendaMapper, AgendaSchema"]
        end

        subgraph VotingSession["voting_session"]
            VSController["VotingSessionController"]
            VSPresenter["VotingSessionPresenter / Responses"]
            VSHandlers["Handlers\nCreateVotingSessionHandler,\nFindVotingSessionHandler,\nFindVotingSessionByAgendaHandler,\nCheckIfExistsSessionToAgendaHandler,\nFindAllVotingSessionsHandler"]
            VSModel["VotingSessionModel"]
            VSPersistence["VotingSessionRepositoryService\nVotingSessionJpaRepository, Mapper, Schema"]
        end

        subgraph Vote["vote"]
            VoteController["VoteController"]
            VotePresenter["VotePresenter / Responses"]
            VoteHandlers["Handlers\nCreateVoteHandler"]
            VoteModel["VoteModel / VoteType"]
            VotePersistence["VoteRepositoryService\nVoteJpaRepository, VoteMapper, VoteSchema,\nVoteResultProjection"]
        end
    end

    subgraph Shared["shared/"]
        Result["Result.java\n(padrão de retorno funcional)"]
    end

    subgraph DB["Banco de Dados"]
        Postgres[("PostgreSQL")]
    end

    HTTP --> Security --> AgendaController
    HTTP --> Security --> VSController
    HTTP --> Security --> VoteController

    AgendaController --> AgendaHandlers --> AgendaPersistence
    AgendaHandlers --> AgendaModel
    AgendaController --> AgendaPresenter

    VSController --> VSHandlers --> VSPersistence
    VSHandlers --> VSModel
    VSHandlers -->|valida agenda| AgendaPersistence
    VSController --> VSPresenter

    VoteController --> VoteHandlers --> VotePersistence
    VoteHandlers --> VoteModel
    VoteHandlers -->|valida sessão ativa| VSPersistence
    VoteController --> VotePresenter

    AgendaPersistence --> Postgres
    VSPersistence --> Postgres
    VotePersistence --> Postgres

    AgendaHandlers -.-> Exceptions
    VSHandlers -.-> Exceptions
    VoteHandlers -.-> Exceptions
    VoteHandlers -.-> CpfClient
```

## Fluxo de uma requisição (exemplo: criar voto)

```mermaid
sequenceDiagram
    participant C as Cliente
    participant SC as SecurityConfig (JWT/Basic)
    participant VC as VoteController
    participant VH as CreateVoteHandler
    participant VSR as VotingSessionRepositoryService
    participant VR as VoteRepositoryService
    participant DB as PostgreSQL

    C->>SC: POST /api/v1/votes
    SC->>VC: request autorizada
    VC->>VH: execute(sessionId, command)
    VH->>VSR: hasActiveSession(sessionId)
    VSR->>DB: SELECT
    DB-->>VSR: resultado
    VSR-->>VH: true/false
    alt sessão inválida
        VH-->>VC: BusinessException
    else sessão válida
        VH->>VR: associateAlreadyVoted(sessionId, associatedId)
        VR->>DB: SELECT
        DB-->>VR: resultado
        alt já votou
            VH-->>VC: BusinessException
        else pode votar
            VH->>VR: save(VoteModel)
            VR->>DB: INSERT
            DB-->>VR: ok
            VR-->>VH: VoteModel
            VH-->>VC: VoteModel
            VC-->>C: 200 OK (VoteCreatedResponse)
        end
    end
```

## Camadas por slice (padrão repetido em agenda / voting_session / vote)

```mermaid
flowchart LR
    Controller["presentation/\nController + Presenter + Response"]
    Feature["features/\nCommand + Handler (caso de uso)"]
    Model["Model do domínio\n(regras/estado)"]
    Persistence["persistence/\nRepositoryService + JpaRepository + Mapper + Schema (entidade JPA)"]

    Controller --> Feature
    Feature --> Model
    Feature --> Persistence
    Persistence --> Model
```

**Convenções observadas:**

- Cada slice (`agenda`, `voting_session`, `vote`) tem sua própria pasta com `features/` (casos de uso), `persistence/` (acesso a dados) e `presentation/` (HTTP).
- `Handler` = caso de uso único (CQRS-like); `Command` = DTO de entrada.
- `Schema` = entidade JPA; `Mapper` converte `Schema` ↔ `Model` de domínio.
- `RepositoryService` encapsula o `JpaRepository` e expõe métodos de domínio (ex.: `hasActiveSession`, `associateAlreadyVoted`).
- `app/` contém preocupações transversais (segurança, exceções, configs, integrações externas).
