--liquibase formatted sql

--changeset flightspotter:004-create-favorites-table
CREATE TABLE IF NOT EXISTS favorites (
    id          UUID            NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID            NOT NULL,
    icao24      VARCHAR(10)     NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_favorites PRIMARY KEY (id),
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_favorites UNIQUE (user_id, icao24)
);

CREATE INDEX IF NOT EXISTS idx_favorites_user_id ON favorites(user_id);

--rollback DROP TABLE IF EXISTS favorites;
