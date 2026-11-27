package com.flashcards.flashcards_service.dto.quiz;

public record QuizAnswerResponse(
        boolean isCorrect,
        String correctAnswer,
        Long correctFlashcardId,
        int correctAnswers,
        int totalQuestions,
        boolean quizCompleted
) {
}
