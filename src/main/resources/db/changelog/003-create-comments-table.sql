--liquibase formatted sql

--changeset flightspotter:003-create-comments-table
CREATE TABLE IF NOT EXISTS comments (
    id          UUID            NOT NULL DEFAULT gen_random_uuid(),
    spot_id     UUID            NOT NULL,
    user_id     UUID            NOT NULL,
    content     VARCHAR(500)    NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    is_deleted  BOOLEAN         NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_spot FOREIGN KEY (spot_id) REFERENCES spots(id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_comments_spot_id ON comments(spot_id);

--rollback DROP TABLE IF EXISTS comments;
