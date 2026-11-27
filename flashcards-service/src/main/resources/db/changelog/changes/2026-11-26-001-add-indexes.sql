--changeset Aurimas:2026-11-26-001-add-indexes
CREATE INDEX IF NOT EXISTS idx_quiz_sessions_group_id_status_started_at
ON quiz_sessions(group_id, status, started_at DESC);

CREATE INDEX IF NOT EXISTS idx_flashcards_quiz_eligible
ON flashcards(group_id)
WHERE deleted = false
AND front_text IS NOT NULL
AND btrim(front_text) <> '';
