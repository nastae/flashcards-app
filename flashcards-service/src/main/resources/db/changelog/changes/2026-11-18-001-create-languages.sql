-- changeset Aurimas:2026-11-18-001-create-languages
CREATE TABLE languages (
    code VARCHAR(35) PRIMARY KEY,
    english_name VARCHAR(100) NOT NULL,
    native_name VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO languages (code, english_name, native_name) VALUES
    ('lt', 'Lithuanian', 'Lietuvių'),
    ('en', 'English', 'English'),
    ('de', 'German', 'Deutsch'),
    ('es', 'Spanish', 'Español'),
    ('fr', 'French', 'Français'),
    ('it', 'Italian', 'Italiano'),
    ('pt', 'Portuguese', 'Português'),
    ('pl', 'Polish', 'Polski'),
    ('nl', 'Dutch', 'Nederlands'),
    ('uk', 'Ukrainian', 'Українська'),
    ('ja', 'Japanese', '日本語'),
    ('ko', 'Korean', '한국어'),
    ('zh', 'Chinese', '中文'),
    ('ru', 'Russian', 'Русский');

-- changeset Aurimas:2026-11-18-002-add-group-languages
ALTER TABLE groups
    ADD COLUMN native_language_code VARCHAR(35),
    ADD COLUMN learning_language_code VARCHAR(35);

UPDATE groups
    SET native_language_code = 'lt',
        learning_language_code = 'en';

ALTER TABLE groups
    ALTER COLUMN native_language_code SET NOT NULL,
    ALTER COLUMN learning_language_code SET NOT NULL;

ALTER TABLE groups
    ADD CONSTRAINT fk_groups_native_language
        FOREIGN KEY (native_language_code)
        REFERENCES languages (code);

ALTER TABLE groups
    ADD CONSTRAINT fk_groups_learning_language
        FOREIGN KEY (learning_language_code)
        REFERENCES languages (code);

ALTER TABLE groups
    ADD CONSTRAINT chk_groups_different_language
        CHECK (native_language_code <> learning_language_code);