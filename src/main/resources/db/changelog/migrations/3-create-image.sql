-- liquibase formatted sql

-- changeset asavershin:createImage
CREATE TABLE image
(
    image_id   bigserial PRIMARY KEY,
    image_name varchar(50)  NOT NULL,
    image_size bigint       not null,
    image_link varchar(300) not null
);

CREATE TABLE note_image
(
    note_id  bigint,
    image_id bigint,
    CONSTRAINT fk_note_image_note FOREIGN KEY (note_id) REFERENCES note (note_id) ON DELETE CASCADE,
    CONSTRAINT fk_note_image_image FOREIGN KEY (image_id) REFERENCES image (image_id) ON DELETE CASCADE
);