package com.flashcards.flashcards_service.repository;

import com.flashcards.flashcards_service.dto.quiz.GroupQuizCountProjection;
import com.flashcards.flashcards_service.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByGroupIdAndDeletedFalse(Long groupId);

    @Query("""
            SELECT f.group.id AS groupId, COUNT(f.id) AS quizEligibleCount
            FROM Flashcard f
            WHERE f.group.id IN :groupIds
                AND f.deleted = false
                AND f.frontText IS NOT NULL
                AND TRIM(f.frontText) <> ''
            GROUP BY f.group.id
            """)
    List<GroupQuizCountProjection> countQuizEligibleByGroupId(
            @Param("groupIds") List<Long> groupIds
    );

    @Query("""
            SELECT f
            FROM Flashcard f
            WHERE f.group.id = :groupId
                AND f.deleted = false
                AND f.frontText IS NOT NULL
                AND TRIM(f.frontText) <> ''
            """)
    List<Flashcard> findActiveQuizCardsByGroupId(@Param("groupId") Long groupId);
}
