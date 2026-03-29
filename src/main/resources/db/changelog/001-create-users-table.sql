--liquibase formatted sql

--changeset flightspotter:001-create-users-table
CREATE TABLE IF NOT EXISTS users (
    id          UUID            NOT NULL DEFAULT gen_random_uuid(),
    username    VARCHAR(50)     NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    password_hash VARCHAR(255)  NOT NULL,
    role        VARCHAR(20)     NOT NULL DEFAULT 'ROLE_USER',
    display_name VARCHAR(100),
    avatar_url  TEXT,
    bio         VARCHAR(500),
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    is_deleted  BOOLEAN         NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_username UNIQUE (username)
);

--rollback DROP TABLE IF EXISTS users;
