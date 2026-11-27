package com.flashcards.flashcards_service.dto.quiz;

public record QuizResultResponse(
        Long sessionId,
        int correctAnswers,
        int totalQuestions,
        double percentage
) {
}
