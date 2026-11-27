package com.flashcards.flashcards_service.dto;

import com.flashcards.flashcards_service.dto.quiz.ActiveQuizResponseDto;

public record GroupResponseDto(
        Long id,
        String name,
        String nativeLanguageCode,
        String learningLanguageCode,
        Long quizEligibleCount,
        ActiveQuizResponseDto activeQuiz
) {
}
