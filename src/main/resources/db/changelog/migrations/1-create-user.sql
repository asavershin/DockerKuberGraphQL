-- liquibase formatted sql

-- changeset asavershin:createUser
CREATE TABLE users
(
    user_id        bigserial PRIMARY KEY,
    user_firstname varchar(20)        NOT NULL,
    user_lastname  varchar(20)        NOT NULL,
    user_email     varchar(50) UNIQUE NOT NULL,
    user_password  TEXT               not null,
    user_role      varchar(10)        not null
);