package com.flashcards.flashcards_service.dto.quiz;

public record QuizSessionResponse(
        Long sessionId,
        int totalQuestions,
        QuizQuestionResponseDto question
) {
}
