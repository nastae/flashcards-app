package com.flashcards.flashcards_service.dto.quiz;

import java.util.List;

public record QuizQuestionResponseDto(
        Long questionId,
        int number,
        String imageUrl,
        String definition,
        List<QuizOptionResponse> options,
        int totalQuestions,
        int score
) {
}
