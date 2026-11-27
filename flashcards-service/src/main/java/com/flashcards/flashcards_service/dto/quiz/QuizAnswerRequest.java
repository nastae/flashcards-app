package com.flashcards.flashcards_service.dto.quiz;

import jakarta.validation.constraints.NotNull;

public record QuizAnswerRequest(
        @NotNull Long questionId,
        @NotNull Long selectedFlashcardId
) {
}
