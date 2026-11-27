package com.flashcards.flashcards_service.entity.quiz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quiz_question_options")
@Getter
@Setter
public class QuizQuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_session_question_id", nullable = false)
    private QuizSessionQuestion question;

    @Column(name = "flashcard_id", nullable = false)
    private Long flashcardId;

    @Column(name = "option_text", nullable = false)
    private String optionText;
}
