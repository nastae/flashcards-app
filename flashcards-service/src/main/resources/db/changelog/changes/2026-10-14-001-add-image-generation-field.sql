-- changeset Aurimas:add-image-generation-status
ALTER TABLE flashcards
ADD COLUMN image_generation_status VARCHAR(50) DEFAULT 'NONE' NOT NULL;