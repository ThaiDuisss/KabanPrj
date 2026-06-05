# Group Project - Review 1: Domain-Driven Design & User-Service Architecture

**Course**: MSS301  
**Project Name**: Kanban Board System  
**Service Covered**: User-Service (with overall Domain analysis)

---

## 1. Domain-Driven Design (DDD) Analysis

To establish a strong modular foundation for the microservice architecture, we have analyzed the overall Kanban system database and identified the **Aggregates**, **Entities**, and **Value Objects (VOs)** for all four core services.

### Overall DDD Classification Table

| Service | Aggregate Root | Entities | Value Objects (VOs) | Core Domain Rules / Invariants |
| :--- | :--- | :--- | :--- | :--- |
| **User Service** | `User` | `User` | `UserId`<br/>`Username`<br/>`Email`<br/>`PasswordHash`<br/>`FullName`<br/>`AvatarUrl` | - Email format must be valid (Regex verified).<br/>- Username must be non-empty and between 3-100 characters.<br/>- Avatar URL length must not exceed 500 characters. |
| **Board Service** | `Board` | `Board`<br/>`Column`<br/>`BoardMember` | `BoardId`<br/>`BoardName`<br/>`ColumnId`<br/>`ColumnName`<br/>`BoardMemberId`<br/>`Role` (OWNER, MEMBER) | - A Board must have at least one column (default columns).<br/>- Only the Board Owner can invite members or delete the board.<br/>- Column position must be positive and unique within the board. |
| **Task Service** | `Task` | `Task`<br/>`Attachment`<br/>`Comment`<br/>`TaskAssignee` | `TaskId`<br/>`TaskTitle`<br/>`TaskDescription`<br/>`Priority` (LOW, MEDIUM, HIGH)<br/>`Status` (TODO, IN_PROGRESS, DONE)<br/>`AttachmentId`<br/>`CommentId` | - Task status must align with valid board column IDs.<br/>- Due date cannot be in the past when creating a task.<br/>- File attachments must not exceed storage limits. |
| **Notification Service** | `Notification` | `Notification` | `NotificationId`<br/>`NotificationType`<br/>`NotificationTitle`<br/>`NotificationMessage` | - Notifications must target a valid UserId.<br/>- Read status defaults to `false`. |

---

## 2. User-Service Architecture & Package Structure

The `User-Service` is structured following the **Clean Architecture** (Ports and Adapters / Hexagonal) paradigm, ensuring complete isolation of the business Domain from frameworks (Spring Boot, Hibernate) and external infrastructure (PostgreSQL, Eureka).

### Package Layout
```
org.kanban.userservice
├── domain (Pure Business Logic)
│   ├── model
│   │   ├── User.java (Aggregate Root & Entity)
│   │   ├── UserId.java (Value Object)
│   │   ├── Username.java (Value Object)
│   │   ├── Email.java (Value Object)
│   │   ├── PasswordHash.java (Value Object)
│   │   ├── FullName.java (Value Object)
│   │   └── AvatarUrl.java (Value Object)
│   ├── repository
│   │   └── UserRepository.java (Domain Repository Interface / Port)
│   └── exception
│       └── UserDomainException.java (Domain-specific Exceptions)
│
├── application (Use Cases / Coordination)
│   ├── dto
│   │   ├── RegisterUserCommand.java (Registration Request payload)
│   │   ├── UpdateUserCommand.java (Profile Update Request payload)
│   │   └── UserResponse.java (Data Transfer Output payload)
│   ├── port
│   │   └── in
│   │       ├── CreateUserUseCase.java (Input Port for creating user)
│   │       ├── UpdateUserUseCase.java (Input Port for updating user)
│   │       └── QueryUserUseCase.java (Input Port for querying user)
│   └── service
│       └── UserService.java (Application Service executing use cases)
│
└── infrastructure (External Frameworks & Adapters)
    ├── adapter
    │   ├── in
    │   │   └── web
    │   │       ├── UserController.java (REST Endpoint Controller)
    │   │       └── exception
    │   │           └── GlobalExceptionHandler.java (Global Exception Rest Handler)
    │   └── out
    │       └── persistence
    │           ├── UserJpaEntity.java (JPA Database Mapping Entity)
    │           ├── JpaUserRepository.java (Spring Data JPA Interface)
    │           └── UserRepositoryImpl.java (Outbound Persistence Adapter)
    └── config
        └── BeanConfig.java (Spring Dependency Injection Configurations)
```

### Architecture Diagram
The diagram below shows the dependencies flowing inwards. Infrastructure depends on Application and Domain, while Domain depends on nothing.

```mermaid
flowchart TD
    subgraph Infrastructure Layer [Infrastructure Layer - Frameworks & Drivers]
        Controller[UserController]
        Handler[GlobalExceptionHandler]
        JpaRepo[JpaUserRepository]
        PersistenceAdapter[UserRepositoryImpl]
        JpaEntity[UserJpaEntity]
        Config[BeanConfig]
    end

    subgraph Application Layer [Application Layer - Use Cases]
        InPort[Create/Update/Query UseCases]
        Service[UserService]
        DTO[Command/Response DTOs]
    end

    subgraph Domain Layer [Domain Layer - Business Entities]
        Entity[User Aggregate Root]
        VO[UserId / Email / Username / PasswordHash / etc.]
        DomRepo[UserRepository Interface]
        Exception[UserDomainException]
    end

    %% Dependency Flows Inward
    Controller --> InPort
    Service -- implements --> InPort
    Service --> DomRepo
    Service --> Entity
    Service --> VO
    Entity --> VO
    PersistenceAdapter -- implements --> DomRepo
    PersistenceAdapter --> JpaRepo
    PersistenceAdapter --> JpaEntity
    JpaRepo --> JpaEntity
    Config --> Service
    Config --> PersistenceAdapter
```

---

## 3. Communication Diagrams (Use Case Flows)

These sequence diagrams demonstrate how request objects and data flow through the Clean Architecture layers.

### Case 1: User Registration (Creating User)
This flow validates input formatting, checks for uniqueness in the database, maps variables into Domain Value Objects, instantiates the domain model, saves via the adapter, and returns a sanitized DTO response.

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as UserController
    participant InPort as CreateUserUseCase (Port)
    participant Service as UserService
    participant DomainUser as User (Entity)
    participant DomRepo as UserRepository (Port)
    participant Persistence as UserRepositoryImpl (Adapter)
    participant JpaRepo as JpaUserRepository
    participant DB as Database

    Client->>Controller: POST /api/users/register (RegisterUserCommand)
    Controller->>InPort: registerUser(command)
    Note over Service: 1. Validate domain rules via VOs<br/>2. Check if username/email exists
    Service->>DomRepo: existsByUsername(Username)
    DomRepo->>Persistence: existsByUsername(Username)
    Persistence->>JpaRepo: existsByUsername(String)
    JpaRepo->>DB: SELECT COUNT...
    DB-->>JpaRepo: count
    JpaRepo-->>Persistence: true/false
    Persistence-->>Service: true/false
    
    Service->>DomainUser: User.createNew(...)
    DomainUser-->>Service: userInstance
    
    Service->>DomRepo: save(userInstance)
    DomRepo->>Persistence: save(userInstance)
    Note over Persistence: Map Domain to Jpa Entity
    Persistence->>JpaRepo: save(jpaEntity)
    JpaRepo->>DB: INSERT INTO users ...
    DB-->>JpaRepo: savedJpaEntity
    JpaRepo-->>Persistence: savedJpaEntity
    Note over Persistence: Map Jpa Entity to Domain
    Persistence-->>Service: savedUserInstance
    
    Note over Service: Map Domain to UserResponse DTO
    Service-->>Controller: UserResponse
    Controller-->>Client: HTTP 201 Created (UserResponse)
```

### Case 2: User Profile Update
This flow retrieves the existing user by ID, executes the state transition within the Domain Entity itself (ensuring encapsulation), saves the modified entity to the database, and returns the updated DTO representation.

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as UserController
    participant InPort as UpdateUserUseCase (Port)
    participant Service as UserService
    participant DomRepo as UserRepository (Port)
    participant Persistence as UserRepositoryImpl (Adapter)
    participant JpaRepo as JpaUserRepository
    participant DomainUser as User (Entity)
    participant DB as Database

    Client->>Controller: PUT /api/users/{id} (UpdateUserCommand)
    Controller->>InPort: updateProfile(id, command)
    
    Service->>DomRepo: findById(UserId)
    DomRepo->>Persistence: findById(UserId)
    Persistence->>JpaRepo: findById(Long)
    JpaRepo->>DB: SELECT * FROM users WHERE id = ?
    DB-->>JpaRepo: JpaEntity
    Persistence-->>Service: Optional<User>
    
    Note over Service: If present, update domain profile
    Service->>DomainUser: updateProfile(fullName, avatarUrl)
    Note over DomainUser: Update local state & set updatedAt
    DomainUser-->>Service: updated
    
    Service->>DomRepo: save(userInstance)
    DomRepo->>Persistence: save(userInstance)
    Persistence->>JpaRepo: save(jpaEntity)
    JpaRepo->>DB: UPDATE users SET ...
    DB-->>JpaRepo: savedJpaEntity
    Persistence-->>Service: savedUserInstance
    
    Service-->>Controller: UserResponse
    Controller-->>Client: HTTP 200 OK (UserResponse)
```

---

## 4. Database Schema Design (Database Diagram)

The following entity-relationship diagram shows the relational schema for the complete database, emphasizing the central role of the `users` table and its foreign key associations.

```mermaid
erDiagram
    users {
        bigint id PK
        varchar username
        varchar email
        varchar password_hash
        varchar full_name
        varchar avatar_url
        timestamp created_at
        timestamp updated_at
    }
    boards {
        bigint id PK
        varchar name
        text description
        bigint owner_id FK
        timestamp created_at
        timestamp updated_at
    }
    board_members {
        bigint id PK
        bigint board_id FK
        bigint user_id FK
        varchar role
        timestamp joined_at
    }
    columns {
        bigint id PK
        bigint board_id FK
        varchar name
        int position
        timestamp created_at
    }
    tasks {
        bigint id PK
        bigint board_id FK
        bigint column_id FK
        varchar title
        text description
        varchar priority
        varchar status
        timestamp due_date
        int position
        bigint created_by FK
        timestamp created_at
        timestamp updated_at
    }
    task_assignees {
        bigint id PK
        bigint task_id FK
        bigint user_id FK
        timestamp assigned_at
    }
    comments {
        bigint id PK
        bigint task_id FK
        bigint user_id FK
        text content
        timestamp created_at
    }
    attachments {
        bigint id PK
        bigint task_id FK
        varchar file_name
        varchar file_url
        bigint uploaded_by FK
        timestamp uploaded_at
    }
    notifications {
        bigint id PK
        bigint user_id FK
        varchar type
        varchar title
        text message
        boolean is_read
        bigint task_id FK
        bigint comment_id FK
        timestamp created_at
    }

    users ||--o{ boards : "owns"
    users ||--o{ board_members : "is member"
    users ||--o{ tasks : "creates"
    users ||--o{ task_assignees : "assigned to"
    users ||--o{ comments : "writes"
    users ||--o{ attachments : "uploads"
    users ||--o{ notifications : "receives"

    boards ||--o{ board_members : "has"
    boards ||--o{ columns : "contains"
    boards ||--o{ tasks : "has"

    columns ||--o{ tasks : "holds"

    tasks ||--o{ task_assignees : "assigns"
    tasks ||--o{ comments : "has"
    tasks ||--o{ attachments : "has"
    tasks ||--o{ notifications : "triggers"
```
