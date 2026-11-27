package com.flashcards.flashcards_service.dto.quiz;

public interface GroupQuizCountProjection {
    Long getGroupId();
    Long getQuizEligibleCount();
}
