package com.flashcards.flashcards_service.controller;

import com.flashcards.flashcards_service.dto.quiz.QuizAnswerRequest;
import com.flashcards.flashcards_service.dto.quiz.QuizAnswerResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizQuestionResponseDto;
import com.flashcards.flashcards_service.dto.quiz.QuizResultResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizSessionResponse;
import com.flashcards.flashcards_service.service.quiz.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/quiz-sessions/{sessionId}/current-question")
    public QuizQuestionResponseDto getCurrentQuestion(
            @PathVariable Long sessionId//,
//            Authentication authentication
    ) {
//        return quizService.getCurrentQuestion(
//                sessionId,
//                authentication.getName()
//        );
        return quizService.getCurrentQuestion(sessionId);
    }

    @PostMapping("/groups/{groupId}/quiz-sessions")
//    public QuizSessionResponse startQuiz(@PathVariable Long groupId, Authentication authentication) {
    public QuizSessionResponse startQuiz(@PathVariable Long groupId) {
//        return quizService.startQuiz(
//                groupId,
//                authentication.getName()
//        );
        return quizService.startQuiz(groupId);
    }

    @PostMapping("/quiz-sessions/{sessionId}/answers")
    public QuizAnswerResponse submitAnswer(
            @PathVariable Long sessionId,
            @Valid @RequestBody QuizAnswerRequest request//,
//            Authentication authentication
            ) {
//         authentication.getName(),
        return quizService.submitAnswer(sessionId, request);
    }

    @PostMapping("/quiz-sessions/{sessionId}/next")
    public QuizQuestionResponseDto nextQuestion(
            @PathVariable Long sessionId//,
//            Authentication authentication
    ) {
//        return quizService.nextQuestion(
//                sessionId,
//                authentication.getName()
//        );
        return quizService.nextQuestion(sessionId);
    }

    @GetMapping("/quiz-sessions/{sessionId}/result")
    public QuizResultResponse getResult(
            @PathVariable Long sessionId//,
//            Authentication authentication
    ) {
//        return quizService.getResult(
//                sessionId,
//                authentication.getName()
//        );
        return quizService.getResult(sessionId);
    }
}
