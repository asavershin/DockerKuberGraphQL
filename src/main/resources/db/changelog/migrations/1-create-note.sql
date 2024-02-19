-- liquibase formatted sql

-- changeset asavershin:createNote
CREATE TABLE note
(
    note_id              serial PRIMARY KEY,
    note_header           varchar(50)                         NOT NULL,
    note_message          TEXT                                ,
    note_created_at      timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    note_last_updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL
);

