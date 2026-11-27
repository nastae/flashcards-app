package com.flashcards.flashcards_service.repository.quiz;

import com.flashcards.flashcards_service.entity.quiz.QuizSessionQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizSessionQuestionRepository extends JpaRepository<QuizSessionQuestion, Long> {

    Optional<QuizSessionQuestion> findByQuizSessionIdAndQuestionNumber(
            Long sessionId,
            int questionNumber
    );

    @Query("""
            SELECT q
            FROM QuizSessionQuestion q
            WHERE q.quizSession.id = :sessionId
                AND NOT EXISTS (
                    SELECT a.id
                    FROM QuizAnswer a
                    WHERE a.question.id = q.id
                )
            ORDER BY q.questionNumber ASC
            """)
    List<QuizSessionQuestion> findFirstUnansweredQuestion(
            @Param("sessionId") Long sessionId,
            Pageable pageable
    );
}
