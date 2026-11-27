package com.flashcards.flashcards_service.repository.quiz;

import com.flashcards.flashcards_service.dto.quiz.ActiveQuizProjection;
import com.flashcards.flashcards_service.entity.quiz.QuizSession;
import com.flashcards.flashcards_service.enums.quiz.QuizSessionStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    Optional<QuizSession> findByIdAndUserId(Long id, Long userId);
    @Query("SELECT qs FROM QuizSession qs WHERE qs.id = :id")
    Optional<QuizSession> findByIdForUpdate(@Param("id") Long id);//TODO: must be in transaction

//            WHERE qs.userId := userId
    @Query("""
            SELECT
                qs.groupId AS groupId,
                qs.id AS sessionId,
                qs.currentQuestionNumber AS currentQuestionNumber,
                qs.totalQuestions AS totalQuestions
            FROM QuizSession qs
            WHERE qs.groupId IN :groupIds
                AND qs.status = :status
            ORDER BY qs.startedAt DESC
            """)
    List<ActiveQuizProjection> findActiveQuizzes(
//            @Param("userId") Long userId
            @Param("groupIds") List<Long> groupIds,
            @Param("status") QuizSessionStatus status
    );

    @Modifying
    @Query("""
            UPDATE QuizSession qs
            SET qs.status = :newStatus
            WHERE qs.groupId = :groupId
                AND qs.status = :currentStatus
            """)
    int updateStatusByGroupIdAndStatus(
            @Param("groupId") Long groupId,
            @Param("currentStatus") QuizSessionStatus currentStatus,
            @Param("newStatus") QuizSessionStatus newStatus
    );
}
