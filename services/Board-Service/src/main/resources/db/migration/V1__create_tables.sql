CREATE TABLE boards (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        owner_id BIGINT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE columns (
                         id BIGSERIAL PRIMARY KEY,
                         board_id BIGINT NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         position INT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_columns_board
                          FOREIGN KEY (board_id) REFERENCES boards(id)

);

CREATE TABLE board_members (
                               id BIGSERIAL PRIMARY KEY,
                               board_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,
                               role VARCHAR(50) NOT NULL,
                               joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_board_members_board
                                FOREIGN KEY (board_id) REFERENCES boards(id)
);