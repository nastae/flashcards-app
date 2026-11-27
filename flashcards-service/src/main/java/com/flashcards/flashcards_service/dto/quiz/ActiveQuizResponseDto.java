package com.flashcards.flashcards_service.dto.quiz;

public record ActiveQuizResponseDto(
        Long sessionId,
        int currentQuestionNumber,
        int totalQuestions
) {
}
