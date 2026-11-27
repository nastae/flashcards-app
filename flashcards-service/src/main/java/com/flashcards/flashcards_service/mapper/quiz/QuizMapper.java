package com.flashcards.flashcards_service.mapper.quiz;

import com.flashcards.flashcards_service.dto.quiz.ActiveQuizProjection;
import com.flashcards.flashcards_service.dto.quiz.ActiveQuizResponseDto;
import com.flashcards.flashcards_service.dto.quiz.QuizAnswerResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizOptionResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizQuestionResponseDto;
import com.flashcards.flashcards_service.dto.quiz.QuizResultResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizSessionResponse;
import com.flashcards.flashcards_service.entity.Flashcard;
import com.flashcards.flashcards_service.entity.quiz.QuizQuestionOption;
import com.flashcards.flashcards_service.entity.quiz.QuizSession;
import com.flashcards.flashcards_service.entity.quiz.QuizSessionQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    @Mapping(source = "session.id",  target = "sessionId")
    @Mapping(source = "session.totalQuestions", target = "totalQuestions")
    @Mapping(source = "question", target = "question")
    QuizSessionResponse toSessionResponse(QuizSession session, QuizQuestionResponseDto question);

    @Mapping(source = "optionText", target = "text")
    QuizOptionResponse toOptionResponse(QuizQuestionOption option);

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.questionNumber", target = "number")
    @Mapping(source = "flashcard.imageUrl", target = "imageUrl")
    @Mapping(source = "flashcard.backText", target = "definition")
    @Mapping(source = "options", target = "options")
    @Mapping(source = "session.totalQuestions", target = "totalQuestions")
    @Mapping(source = "session.correctAnswers", target = "score")
    QuizQuestionResponseDto toQuestionResponse(
            QuizSessionQuestion question,
            Flashcard flashcard,
            List<QuizOptionResponse> options,
            QuizSession session
    );

    @Mapping(source = "isCorrect", target = "isCorrect")
    @Mapping(source = "correctAnswer", target = "correctAnswer")
    @Mapping(source = "correctFlashcardId", target = "correctFlashcardId")
    @Mapping(source = "session.correctAnswers", target = "correctAnswers")
    @Mapping(source = "session.totalQuestions", target = "totalQuestions")
    @Mapping(source = "quizCompleted", target = "quizCompleted")
    QuizAnswerResponse toAnswerResponse(
            boolean isCorrect,
            String correctAnswer,
            Long correctFlashcardId,
            QuizSession session,
            boolean quizCompleted
    );

    @Mapping(source = "session.id", target = "sessionId")
    @Mapping(source = "session.correctAnswers", target = "correctAnswers")
    @Mapping(source = "session.totalQuestions", target = "totalQuestions")
    @Mapping(expression = "java(Math.round(percentage * 100.0) / 100.0)", target = "percentage")
    QuizResultResponse toResultResponse(QuizSession session, double percentage);

    ActiveQuizResponseDto toQuizResponseDto(ActiveQuizProjection quizProjection);
}
