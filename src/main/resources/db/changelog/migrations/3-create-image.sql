-- liquibase formatted sql

-- changeset asavershin:createImage
CREATE TABLE image
(
    image_id   bigserial PRIMARY KEY,
    image_name varchar(50) NOT NULL,
    image_size bigint not null ,
    image_link varchar(300) not null ,
    note_id    bigint not null ,
    CONSTRAINT fk_images_note FOREIGN KEY (note_id) REFERENCES note (note_id) ON DELETE CASCADE
);