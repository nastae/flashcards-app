CREATE TABLE groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
--    TODO: add timestamps:
--      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE flashcards (
    id BIGSERIAL PRIMARY KEY,
    front_text VARCHAR(255) NOT NULL,
--    TODO: add validation on controller level
    back_text VARCHAR(500),
    image_url VARCHAR(500),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    group_id BIGINT NOT NULL,
--    TODO: add for spaced repetition system
--      next_review_at TIMESTAMP

    CONSTRAINT fk_flashcards_group
        FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_flashcards_group_id ON flashcards(group_id);
CREATE INDEX idx_flashcards_deleted ON flashcards(deleted);
CREATE INDEX idx_groups_deleted ON groups(deleted);