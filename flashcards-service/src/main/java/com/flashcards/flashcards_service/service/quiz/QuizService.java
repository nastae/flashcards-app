package com.flashcards.flashcards_service.service.quiz;

import com.flashcards.flashcards_service.dto.quiz.QuizAnswerRequest;
import com.flashcards.flashcards_service.dto.quiz.QuizAnswerResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizOptionResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizQuestionResponseDto;
import com.flashcards.flashcards_service.dto.quiz.QuizResultResponse;
import com.flashcards.flashcards_service.dto.quiz.QuizSessionResponse;
import com.flashcards.flashcards_service.entity.Flashcard;
import com.flashcards.flashcards_service.entity.quiz.QuizAnswer;
import com.flashcards.flashcards_service.entity.quiz.QuizQuestionOption;
import com.flashcards.flashcards_service.entity.quiz.QuizSession;
import com.flashcards.flashcards_service.entity.quiz.QuizSessionQuestion;
import com.flashcards.flashcards_service.enums.quiz.QuizSessionStatus;
import com.flashcards.flashcards_service.mapper.quiz.QuizMapper;
import com.flashcards.flashcards_service.repository.FlashcardRepository;
import com.flashcards.flashcards_service.repository.quiz.QuizAnswerRepository;
import com.flashcards.flashcards_service.repository.quiz.QuizSessionQuestionRepository;
import com.flashcards.flashcards_service.repository.quiz.QuizSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private static final int MINIMUM_CARDS = 4;
    private static final int OPTIONS_PER_QUESTION = 4;

    private final QuizSessionRepository quizSessionRepository;
    private final QuizSessionQuestionRepository quizSessionQuestionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final FlashcardRepository flashcardRepository;
    private final QuizMapper quizMapper;

    @Transactional
    public QuizQuestionResponseDto getCurrentQuestion(Long sessionId) {
        QuizSession session = getLockedSession(sessionId);
        ensureInProgress(session);

        List<QuizSessionQuestion> questions = quizSessionQuestionRepository
                .findFirstUnansweredQuestion(sessionId, PageRequest.of(0, 1));

        if (questions.isEmpty()) {
            log.warn(
                    "No answered question found in an active quiz. Session id: {}",
                    sessionId
            );

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No answered question found in an active quiz."
            );
        }

        QuizSessionQuestion question = questions.getFirst();

        if (session.getCurrentQuestionNumber() != question.getQuestionNumber()) {
            session.setCurrentQuestionNumber(question.getQuestionNumber());
        }

        return toQuestionResponse(question);
    }

//    public QuizSessionResponse startQuiz(Long groupId, Long userId) {
    @Transactional
    public QuizSessionResponse startQuiz(Long groupId) {
        List<Flashcard> cards = getUniqueCards(groupId);

        if (cards.size() < MINIMUM_CARDS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "At least %d different learning words are required to start a quiz",
                            MINIMUM_CARDS
                    )
            );
        }

        quizSessionRepository.updateStatusByGroupIdAndStatus(
                groupId,
                QuizSessionStatus.IN_PROGRESS,
                QuizSessionStatus.ABANDONED
        );

        Collections.shuffle(cards);

        QuizSession session = new QuizSession();
        session.setGroupId(groupId);
        session.setStatus(QuizSessionStatus.IN_PROGRESS);
//        session.setUserId
        session.setTotalQuestions(cards.size());
        session.setCurrentQuestionNumber(1);
        session.setCorrectAnswers(0);
        session.setStartedAt(LocalDateTime.now());

        quizSessionRepository.save(session);

        for (int i = 0; i < cards.size(); i++) {
            Flashcard correctCard = cards.get(i);

            QuizSessionQuestion question = new QuizSessionQuestion();
            question.setQuizSession(session);
            question.setCorrectFlashcardId(correctCard.getId());
            question.setQuestionNumber(i + 1);

            List<Flashcard> options = createOptions(correctCard, cards);

            for (Flashcard optionCard: options) {
                QuizQuestionOption option = new QuizQuestionOption();
                option.setQuestion(question);
                option.setFlashcardId(optionCard.getId());
                option.setOptionText(optionCard.getFrontText());

                question.getOptions().add(option);
            }

            session.getQuestions().add(question);
        }

        quizSessionRepository.save(session);

        return quizMapper.toSessionResponse(
                session,
                toQuestionResponse(session.getQuestions().getFirst())
        );
    }

    @Transactional
    public QuizAnswerResponse submitAnswer(
            Long sessionId,
//            String userId,
            QuizAnswerRequest request
    ) {
        QuizSession session = getLockedSession(sessionId);
        ensureInProgress(session);

        QuizSessionQuestion question = getCurrentQuestionEntity(session);

        if (!question.getId().equals(request.questionId())) {
            log.warn(
                    "This is not the current quiz question. Question id: {}, requested question id: {}",
                    question.getId(),
                    request.questionId()
            );

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This is not the current quiz question."
            );
        }

        quizAnswerRepository.findByQuestionId(question.getId())
                .ifPresent(answer -> {
                    log.warn(
                            "Question {} has already been answered. Your answer flashcard id: {}",
                            question.getId(),
                            answer.getSelectedFlashcardId()
                    );

                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "This question has already been answered."
                    );
                });

        boolean selectedOptionExists = question.getOptions().stream()
                .anyMatch(option ->
                    option.getFlashcardId().equals(request.selectedFlashcardId())
                );

        if (!selectedOptionExists) {
            log.warn(
                    "The selected option does not belong to this question. Selected flashcard id: {}",
                    request.selectedFlashcardId()
            );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The selected option does not belong to this question."
            );
        }

        boolean isCorrect = question.getCorrectFlashcardId()
                .equals(request.selectedFlashcardId());

        QuizAnswer answer = new QuizAnswer();
        answer.setQuestion(question);
        answer.setSelectedFlashcardId(request.selectedFlashcardId());
        answer.setCorrect(isCorrect);

        quizAnswerRepository.save(answer);

        if (isCorrect) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }

        boolean quizCompleted =
                session.getCurrentQuestionNumber() == session.getTotalQuestions();

        if (quizCompleted) {
            session.setStatus(QuizSessionStatus.COMPLETED);
            session.setCompletedAt(LocalDateTime.now());
        }

        quizSessionRepository.save(session);

        Flashcard correctFlashcard = flashcardRepository
                .findById(question.getCorrectFlashcardId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Correct flashcard not found"
                ));
        Long correctFlashcardId = correctFlashcard.getId();
        String correctAnswer = correctFlashcard.getFrontText();

        return quizMapper.toAnswerResponse(isCorrect, correctAnswer, correctFlashcardId, session, quizCompleted);
    }

    @Transactional
//    public QuizQuestionResponse nextQuestion(Long sessionId, Long userId) {
    public QuizQuestionResponseDto nextQuestion(Long sessionId) {
        QuizSession session = getLockedSession(sessionId);
        ensureInProgress(session);

        QuizSessionQuestion currentQuestion = getCurrentQuestionEntity(session);

        if (quizAnswerRepository.findByQuestionId(currentQuestion.getId()).isEmpty()) {
            log.warn(
                    "Answer the current question before continuing. Question id: {}",
                    currentQuestion.getId()
            );

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Answer the current question before continuing."
            );
        }

        if (currentQuestion.getQuestionNumber() >= session.getTotalQuestions()) {
            log.warn(
                    "The quiz has already reached its final question. Question number: {}, total questions: {}",
                    currentQuestion.getQuestionNumber(),
                    session.getTotalQuestions()
            );

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The quiz has already reached its final question."
            );
        }

        session.setCurrentQuestionNumber(
                session.getCurrentQuestionNumber() + 1
        );

        quizSessionRepository.save(session);

        QuizSessionQuestion nextQuestion = getCurrentQuestionEntity(session);

        return toQuestionResponse(nextQuestion);
    }

    @Transactional(readOnly = true)
//    public QuizResultResponse getResult(Long sessionId, Long userId) {
    public QuizResultResponse getResult(Long sessionId) {
        QuizSession session = getSession(sessionId);

        if (session.getStatus() != QuizSessionStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The quiz has not been completed yet."
            );
        }

        double percentage = session.getTotalQuestions() == 0
                ? 0
                : (double) session.getCorrectAnswers()
                / session.getTotalQuestions() * 100;

        return quizMapper.toResultResponse(session, percentage);
    }

    private List<Flashcard> getUniqueCards(Long groupId) {
        List<Flashcard> cards = flashcardRepository.findActiveQuizCardsByGroupId(groupId);

        Map<String, Flashcard> uniqueCardsMap = cards.stream()
                .collect(Collectors.toMap(
                        card -> card.getFrontText().trim().toLowerCase(Locale.ROOT),
                        card -> card,
                        (first, second) -> first,
                        LinkedHashMap::new
                ));

        List<Flashcard> uniqueCards = new ArrayList<>(uniqueCardsMap.values());
        return uniqueCards;
    }

    private List<Flashcard> createOptions(Flashcard correctCard, List<Flashcard> allCards) {
        List<Flashcard> candidates = new ArrayList<>(allCards);

        candidates.removeIf(card ->
            card.getId().equals(correctCard.getId())
        );

        Collections.shuffle(candidates);

        List<Flashcard> options = new ArrayList<>();
        options.add(correctCard);
        options.addAll(candidates.subList(0, OPTIONS_PER_QUESTION - 1));

        Collections.shuffle(options);

        return options;
    }

    private QuizQuestionResponseDto toQuestionResponse(QuizSessionQuestion question) {
        Flashcard card = flashcardRepository.findById(
                question.getCorrectFlashcardId()
        ).orElseThrow(() -> {
            log.error(
                    "Flashcard not found: " + question.getCorrectFlashcardId()
            );

            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Flashcard not found"
            );
        });

        List<QuizOptionResponse> options = question.getOptions().stream()
                .map(quizMapper::toOptionResponse)
                .toList();

        QuizSession session = question.getQuizSession();

        return quizMapper.toQuestionResponse(question, card, options, session);
    }

//    private QuizSession getLockedSession(Long sessionId, Long useId) {
    private QuizSession getLockedSession(Long sessionId) {
//        return quizSessionRepository.findByIdAndUserId(sessionId, userId)
        return quizSessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                        "Quiz session not found"
                ));
    }

    private void ensureInProgress(QuizSession session) {
        if (session.getStatus() != QuizSessionStatus.IN_PROGRESS) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This quiz session is already completed."
            );
        }
    }

    private QuizSessionQuestion getCurrentQuestionEntity(QuizSession session) {
        return quizSessionQuestionRepository.findByQuizSessionIdAndQuestionNumber(
                session.getId(),
                session.getCurrentQuestionNumber()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Quiz question not found"
        ));
    }

    private QuizSession getSession(Long sessionId) {
//        return sessionRepository.findByIdAndUserId(sessionId, userId)
        return quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Quiz session not found."
                ));
    }
}
