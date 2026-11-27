-- changeset Aurimas:2026-11-27-001-add-unique-active-quiz-session
CREATE UNIQUE INDEX uk_quiz_sessions_active_group
ON quiz_sessions(group_id)
WHERE status = 'IN_PROGRESS';