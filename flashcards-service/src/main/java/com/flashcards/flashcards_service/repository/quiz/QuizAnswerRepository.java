package com.flashcards.flashcards_service.repository.quiz;

import com.flashcards.flashcards_service.entity.quiz.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    Optional<QuizAnswer> findByQuestionId(Long questionId);
}
