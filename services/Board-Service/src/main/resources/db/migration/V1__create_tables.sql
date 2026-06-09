CREATE TABLE boards (
                        id BIGSERIAL PRIMARY KEY,

                        name VARCHAR(255) NOT NULL,
                        description TEXT,

                        owner_id BIGINT NOT NULL,

                        visibility VARCHAR(30) NOT NULL DEFAULT 'PRIVATE',
                        board_type VARCHAR(30) NOT NULL DEFAULT 'KANBAN',

                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT ck_boards_visibility
                            CHECK (visibility IN ('PRIVATE', 'TEAM', 'PUBLIC')),

                        CONSTRAINT ck_boards_type
                            CHECK (board_type IN ('KANBAN', 'SCRUM'))
);

CREATE TABLE board_columns (
                               id BIGSERIAL PRIMARY KEY,

                               board_id BIGINT NOT NULL,

                               name VARCHAR(100) NOT NULL,
                               position INT NOT NULL,

                               wip_limit INT NULL,
                               is_done_column BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_board_columns_board
                                   FOREIGN KEY (board_id) REFERENCES boards(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT uq_board_columns_board_position
                                   UNIQUE (board_id, position),

                               CONSTRAINT uq_board_columns_board_name
                                   UNIQUE (board_id, name),

                               CONSTRAINT ck_board_columns_position
                                   CHECK (position >= 0),

                               CONSTRAINT ck_board_columns_wip_limit
                                   CHECK (wip_limit IS NULL OR wip_limit > 0)
);

CREATE TABLE board_statuses (
                                id BIGSERIAL PRIMARY KEY,

                                board_id BIGINT NOT NULL,

                                name VARCHAR(100) NOT NULL,
                                status_category VARCHAR(30) NOT NULL,

                                position INT NOT NULL,

                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_board_statuses_board
                                    FOREIGN KEY (board_id) REFERENCES boards(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uq_board_statuses_board_name
                                    UNIQUE (board_id, name),

                                CONSTRAINT uq_board_statuses_board_position
                                    UNIQUE (board_id, position),

                                CONSTRAINT ck_board_statuses_category
                                    CHECK (status_category IN ('TODO', 'IN_PROGRESS', 'DONE')),

                                CONSTRAINT ck_board_statuses_position
                                    CHECK (position >= 0)
);

CREATE TABLE board_column_statuses (
                                       id BIGSERIAL PRIMARY KEY,

                                       board_column_id BIGINT NOT NULL,
                                       board_status_id BIGINT NOT NULL,

                                       position INT NOT NULL,

                                       CONSTRAINT fk_board_column_statuses_column
                                           FOREIGN KEY (board_column_id) REFERENCES board_columns(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT fk_board_column_statuses_status
                                           FOREIGN KEY (board_status_id) REFERENCES board_statuses(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT uq_board_column_statuses_status
                                           UNIQUE (board_status_id),

                                       CONSTRAINT uq_board_column_statuses_column_position
                                           UNIQUE (board_column_id, position),

                                       CONSTRAINT ck_board_column_statuses_position
                                           CHECK (position >= 0)
);

CREATE TABLE board_members (
                               id BIGSERIAL PRIMARY KEY,

                               board_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,

                               role VARCHAR(50) NOT NULL,

                               joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_board_members_board
                                   FOREIGN KEY (board_id) REFERENCES boards(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT uq_board_members_board_user
                                   UNIQUE (board_id, user_id),

                               CONSTRAINT ck_board_members_role
                                   CHECK (role IN ('OWNER', 'ADMIN', 'MEMBER', 'VIEWER'))
);