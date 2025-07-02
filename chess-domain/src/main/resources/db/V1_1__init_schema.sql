-- V1_1__init_schema.sql
-- Fecha: 01-01-2024
-- Autor: barrikeit
-- Versión: 0.0.1

DROP TABLE IF EXISTS games_users_colors CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS role_modules CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS modules CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS game_states CASCADE;
DROP TABLE IF EXISTS ranks CASCADE;
DROP TABLE IF EXISTS colors CASCADE;

CREATE TYPE friendship_status AS ENUM ('PENDING', 'ACCEPTED', 'BLOCKED');

CREATE TABLE colors
(
    id_color   BIGSERIAL  NOT NULL, -- identificador del color
    code_color VARCHAR(1) NOT NULL, -- abreviatura del color B o W
    color      VARCHAR(5) NOT NULL  -- color Black o White
);

CREATE TABLE ranks
(
    id_rank   BIGSERIAL   NOT NULL, -- identificador del rango
    code_rank VARCHAR(2)  NOT NULL, -- código único del rango
    rank      VARCHAR(50) NOT NULL  -- rango
);

CREATE TABLE game_states
(
    id_game_state   BIGSERIAL    NOT NULL, -- identificador del estado de la partida
    code_game_state VARCHAR(255) NOT NULL, -- abreviatura del estado de la partida
    game_state      VARCHAR(255) NOT NULL, -- estado de la partida
    observations    VARCHAR(255) NOT NULL  -- información extra para el estado de la partida (ganan blancas, por rendición)
);

CREATE TABLE locations
(
    id_location   BIGSERIAL    NOT NULL, -- identificador de la ubicación
    code_location UUID         NOT NULL, -- código único de la ubicación
    location      VARCHAR(255) NOT NULL, -- ubicación
    country       VARCHAR(255) NOT NULL, -- pais
    city          VARCHAR(255) NULL      -- ciudad
);

CREATE TABLE events
(
    id_event    BIGSERIAL                NOT NULL, -- identificador del evento
    code_event  UUID                     NOT NULL, -- código único del evento
    event       VARCHAR(255)             NOT NULL, -- nombre del evento
    start_date  TIMESTAMP WITH TIME ZONE NOT NULL, -- fecha y hora del comienzo del evento
    end_date    TIMESTAMP WITH TIME ZONE NULL,     -- fecha y hora del final del evento
    round       INTEGER                  NOT NULL, -- ronda
    id_location BIGINT                   NOT NULL  -- identificador de la ubicacion del evento
);

CREATE TABLE modules
(
    id_module   BIGSERIAL    NOT NULL,
    code_module VARCHAR(3)   NOT NULL,
    module      VARCHAR(200) NOT NULL
);

CREATE TABLE role_modules
(
    id_module BIGINT NOT NULL,
    id_role   BIGINT NOT NULL
);

CREATE TABLE roles
(
    id_role   BIGSERIAL   NOT NULL,
    code_role VARCHAR(2)  NOT NULL,
    role      VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles
(
    id_user BIGINT NOT NULL,
    id_role BIGINT NOT NULL
);

CREATE TABLE users
(
    id_user           BIGSERIAL                NOT NULL,               -- identificador del usuario
    code_user         UUID                     NOT NULL,               -- código único del usuario
    username          VARCHAR(50)              NOT NULL,               -- usuario de registro
    name              VARCHAR(50)              NOT NULL,               -- nombre real
    surname1          VARCHAR(50)              NOT NULL,               -- apellido 1 real
    surname2          VARCHAR(50)              NULL,                   -- aprellido 2 real
    email             VARCHAR(100)             NOT NULL,               -- email de registro y método de contacto
    phone             VARCHAR(50)              NULL,                   -- para posible verificación en 2 pasos
    password          VARCHAR(255)             NULL,                   -- contraseña de registro hasheada
    id_location       BIGINT                   NULL,                   -- identificador del pais para bandera
    elo               INTEGER                  NULL,                   -- ELO rating
    id_rank           BIGINT                   NULL,                   -- Rango
    registered        BOOLEAN                  NOT NULL default false, -- si está registrado (0: no, 1: si)
    registration_code VARCHAR(255)             NULL,                   -- token para habilitar
    registration_date TIMESTAMP WITH TIME ZONE NULL,                   -- fecha y hora del registro
    logged            BOOLEAN                  NOT NULL default false, -- si está logeado (0: no, 1: si)
    login_attempts    INTEGER                  NOT NULL,               -- cantidad de intentos mas de 'x' sin acertar bloquea la cuenta
    login_date        TIMESTAMP WITH TIME ZONE NULL,                   -- fecha y hora del ultimo acceso
    banned            BOOLEAN                  NOT NULL default false, -- si esta banneado (0: no, 1: si)
    ban_date          TIMESTAMP WITH TIME ZONE NULL,                   -- fecha y hora del baneo
    ban_reason        VARCHAR(255)             NULL                    -- razón del baneo
);

CREATE TABLE friendships
(
    id_friendship BIGSERIAL                NOT NULL,   -- identificador de la amistad
    id_user1      BIGINT                   NOT NULL,   -- identificador del usuario que envía la solicitud
    id_user2      BIGINT                   NOT NULL,   -- identificador del usuario que recibe la solicitud
    status        friendship_status DEFAULT 'PENDING', -- estado de la relación
    start_date    TIMESTAMP WITH TIME ZONE NOT NULL,   -- fecha y hora del comienzo
    end_date      TIMESTAMP WITH TIME ZONE NULL        -- fecha y hora del final
);

CREATE TABLE games
(
    id_game       BIGSERIAL                NOT NULL, -- identificador de la partida
    code_game     UUID                     NOT NULL, -- código único de la partida
    pgn           VARCHAR(255)             NOT NULL, -- listado de movimientos
    fen           VARCHAR(255)             NOT NULL, -- estado del tablero
    id_game_state BIGINT                   NOT NULL, -- identificador del estado de la partida
    id_event      BIGINT                   NOT NULL, -- evento
    start_date    TIMESTAMP WITH TIME ZONE NOT NULL, -- fecha y hora del comienzo
    end_date      TIMESTAMP WITH TIME ZONE NULL      -- fecha y hora del final
);

CREATE TABLE games_users_colors
(
    id_game_user_color BIGSERIAL NOT NULL, -- identificador de la relación
    id_game            BIGINT    NOT NULL, -- identificador de la partida
    id_user            BIGINT    NOT NULL, -- identificador del usuario
    id_color           BIGINT    NOT NULL  -- identificador del color que le toco al usuario en la partida
);


ALTER TABLE colors
    ADD CONSTRAINT pk_colors PRIMARY KEY (id_color),
    ADD CONSTRAINT uk_colors UNIQUE (code_color);

ALTER TABLE ranks
    ADD CONSTRAINT pk_ranks PRIMARY KEY (id_rank),
    ADD CONSTRAINT uk_ranks UNIQUE (code_rank);

ALTER TABLE game_states
    ADD CONSTRAINT pk_game_states PRIMARY KEY (id_game_state),
    ADD CONSTRAINT uk_game_states UNIQUE (code_game_state);

ALTER TABLE locations
    ADD CONSTRAINT pk_locations PRIMARY KEY (id_location),
    ADD CONSTRAINT uk_locations UNIQUE (code_location);

ALTER TABLE events
    ADD CONSTRAINT pk_events PRIMARY KEY (id_event),
    ADD CONSTRAINT uk_events UNIQUE (code_event),
    ADD CONSTRAINT fk_events_on_location FOREIGN KEY (id_location) REFERENCES locations (id_location);

ALTER TABLE modules
    ADD CONSTRAINT pk_modules PRIMARY KEY (id_module),
    ADD CONSTRAINT uk_modules UNIQUE (code_module);

ALTER TABLE roles
    ADD CONSTRAINT pk_roles PRIMARY KEY (id_role),
    ADD CONSTRAINT uk_roles UNIQUE (code_role);

ALTER TABLE users
    ADD CONSTRAINT pk_users PRIMARY KEY (id_user),
    ADD CONSTRAINT uk_users_code UNIQUE (code_user),
    ADD CONSTRAINT fk_users_on_location FOREIGN KEY (id_location) REFERENCES locations (id_location),
    ADD CONSTRAINT fk_users_on_rank FOREIGN KEY (id_rank) REFERENCES ranks (id_rank);

ALTER TABLE games
    ADD CONSTRAINT pk_games PRIMARY KEY (id_game),
    ADD CONSTRAINT uk_games UNIQUE (code_game),
    ADD CONSTRAINT fk_games_on_event FOREIGN KEY (id_event) REFERENCES events (id_event),
    ADD CONSTRAINT fk_games_on_state FOREIGN KEY (id_game_state) REFERENCES game_states (id_game_state);

ALTER TABLE role_modules
    ADD CONSTRAINT pk_role_modules PRIMARY KEY (id_module, id_role),
    ADD CONSTRAINT fk_role_modules_on_role FOREIGN KEY (id_role) REFERENCES roles (id_role),
    ADD CONSTRAINT fk_role_modules_on_module FOREIGN KEY (id_module) REFERENCES modules (id_module);

ALTER TABLE user_roles
    ADD CONSTRAINT pk_user_roles PRIMARY KEY (id_role, id_user),
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (id_user) REFERENCES users (id_user),
    ADD CONSTRAINT fk_user_roles_on_role FOREIGN KEY (id_role) REFERENCES roles (id_role);

ALTER TABLE friendships
    ADD CONSTRAINT pk_friendships PRIMARY KEY (id_friendship),
    ADD CONSTRAINT fk_friendships_on_user1 FOREIGN KEY (id_user1) REFERENCES users (id_user),
    ADD CONSTRAINT fk_friendships_on_user2 FOREIGN KEY (id_user2) REFERENCES users (id_user);

ALTER TABLE games_users_colors
    ADD CONSTRAINT pk_games_users_colors PRIMARY KEY (id_game_user_color),
    ADD CONSTRAINT fk_games_users_colors_on_game FOREIGN KEY (id_game) REFERENCES games (id_game),
    ADD CONSTRAINT fk_games_users_colors_on_user FOREIGN KEY (id_user) REFERENCES users (id_user),
    ADD CONSTRAINT fk_games_users_colors_on_color FOREIGN KEY (id_color) REFERENCES colors (id_color);
