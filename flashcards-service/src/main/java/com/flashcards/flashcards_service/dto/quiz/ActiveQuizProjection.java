package com.flashcards.flashcards_service.dto.quiz;

public interface ActiveQuizProjection {
    Long getGroupId();
    Long getSessionId();
    Integer getCurrentQuestionNumber();
    Integer getTotalQuestions();
}
