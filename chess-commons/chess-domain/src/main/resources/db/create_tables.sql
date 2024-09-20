-- create_tables.sql
-- Fecha: 01-01-2024
-- Autor: barrikeit
-- Versión: 0.0.1

-- Table: usuarios
CREATE TABLE users
(
    id                bigserial                NOT NULL,               -- identificador del usuario
    code              UUID                     NOT NULL,               -- código único del usuario
    username          VARCHAR(50)              NOT NULL,               -- usuario de registro
    name              VARCHAR(50)              NOT NULL,               -- nombre real
    surname1          VARCHAR(50)              NOT NULL,               -- apellido 1 real
    surname2          VARCHAR(50)              NULL,                   -- aprellido 2 real
    email             VARCHAR(100)             NOT NULL,               -- email de registro y método de contacto
    phone             VARCHAR(50)              NULL,                   -- para posible verificación en 2 pasos
    password          VARCHAR(255)             NULL,                   -- contraseña de registro hasheada
    id_location       bigint                   NULL,                   -- identificador del pais para bandera
    elo               VARCHAR(100)             NULL,                   -- ELO rating
    id_rank           bigint                   NULL,                   -- Rango
    registered        boolean                  NOT NULL default false, -- si está registrado (0: no, 1: si)
    registration_code VARCHAR(255)             NULL,                   -- token para habilitar
    registration_date TIMESTAMP WITH TIME ZONE NULL,                   -- fecha y hora del registro
    logged            boolean                  NOT NULL default false, -- si está logeado (0: no, 1: si)
    login_attempts    VARCHAR(255)             NOT NULL,               -- cantidad de intentos mas de 'x' sin acertar bloquea la cuenta
    login_date        TIMESTAMP WITH TIME ZONE NULL,                   -- fecha y hora del ultimo acceso
    banned            boolean                  NOT NULL default false, -- si esta banneado (0: no, 1: si)
    ban_date          TIMESTAMP WITH TIME ZONE NULL,                   -- fecha y hora del baneo
    ban_reason        VARCHAR(255)             NULL                    -- razón del baneo
);

-- Table: amistades
CREATE TABLE friendships
(
    id       bigserial, -- identificador de la amistad
    id_user1 bigint,    -- identificador del usuario que envía la solicitud
    id_user2 bigint     -- identificador del usuario que recibe la solicitud
);

-- Table: roles
CREATE TABLE roles
(
    id   bigserial   NOT NULL, -- identificador del rol
    code VARCHAR(2)  NOT NULL, -- código único del rol
    role VARCHAR(50) NOT NULL  -- rol
);

-- Table: maestría
CREATE TABLE ranks
(
    id   bigserial   NOT NULL, -- identificador del rango
    code VARCHAR(2)  NOT NULL, -- código único del rango
    rank VARCHAR(50) NOT NULL  -- rango
);

-- Table: ubicaciones
CREATE TABLE locations
(
    id      bigserial    NOT NULL, -- identificador del pais
    code    VARCHAR(2)   NOT NULL, -- abreviatura del pais
    country VARCHAR(255) NOT NULL, -- nombre del pais
    city    VARCHAR(255) NULL      -- ciudad
);

-- Table: usuarios_roles
CREATE TABLE users_roles
(
    id_user bigint, -- identificador del usuario
    id_role bigint  -- identificador del rol
);

-- Table: partidas
CREATE TABLE games
(
    id         bigserial                NOT NULL, -- identificador de la partida
    code       UUID                     NOT NULL, -- código único de la partida
    start_date TIMESTAMP WITH TIME ZONE NOT NULL, -- fecha y hora del comienzo de la partida
    end_date   TIMESTAMP WITH TIME ZONE NULL,     -- fecha y hora del final de la partida
    pgn        VARCHAR(255)             NOT NULL, -- listado de movimientos
    fen        VARCHAR(255)             NOT NULL, -- estado del tablero
    id_state   VARCHAR(20)              NOT NULL, -- identificador del estado de la partida
    id_event   bigint                   NOT NULL  -- evento
);

-- Table: partidas_usuarios
CREATE TABLE games_users_colors
(
    id       bigserial NOT NULL, -- identificador de la relación
    id_game  bigint    NOT NULL, -- identificador de la partida
    id_user  bigint    NOT NULL, -- identificador del usuario
    id_color bigint    NOT NULL  -- identificador del color que le toco al usuario en la partida
);

-- Table: estados
CREATE TABLE game_states
(
    id           bigserial    NOT NULL, -- identificador del estado de la partida
    code         VARCHAR(255) NOT NULL, -- abreviatura del estado de la partida 1-0, 0-1, 1/2-1/2, 0-0
    state        VARCHAR(255) NOT NULL, -- estado de la partida
    observations VARCHAR(255) NOT NULL  -- informacion extra para el estado de la partida (ganan blancas, por rendicion)
);

-- Table: color
CREATE TABLE colors
(
    id    bigserial  NOT NULL, -- identificador del color
    code  VARCHAR(1) NOT NULL, -- abreviatura del color B o W
    color VARCHAR(5) NOT NULL  -- color Black o White
);

-- Table: eventos
CREATE TABLE events
(
    id          bigserial                NOT NULL, -- identificador del evento
    code        VARCHAR(255)             NOT NULL, -- código único del evento
    name        VARCHAR(255)             NOT NULL, -- nombre del evento
    start_date  TIMESTAMP WITH TIME ZONE NOT NULL, -- fecha y hora del comienzo del evento
    end_date    TIMESTAMP WITH TIME ZONE NULL,     -- fecha y hora del final del evento
    round       bigint                   NOT NULL, -- ronda
    id_location bigint                   NOT NULL  -- identificador de la ubicacion del evento
);
