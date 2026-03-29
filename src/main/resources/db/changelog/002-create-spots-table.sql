--liquibase formatted sql

--changeset flightspotter:002-create-spots-table
CREATE TABLE IF NOT EXISTS spots (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    user_id         UUID            NOT NULL,
    icao24          VARCHAR(10)     NOT NULL,
    registration    VARCHAR(20),
    aircraft_model  VARCHAR(100),
    airline         VARCHAR(100),
    image_path      TEXT            NOT NULL,
    notes           VARCHAR(1000),
    spot_latitude   DOUBLE PRECISION,
    spot_longitude  DOUBLE PRECISION,
    spot_location   VARCHAR(200),
    altitude        DOUBLE PRECISION,
    speed           DOUBLE PRECISION,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    is_deleted      BOOLEAN         NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_spots PRIMARY KEY (id),
    CONSTRAINT fk_spots_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_spots_user_id ON spots(user_id);
CREATE INDEX IF NOT EXISTS idx_spots_icao24 ON spots(icao24);

--rollback DROP TABLE IF EXISTS spots;
