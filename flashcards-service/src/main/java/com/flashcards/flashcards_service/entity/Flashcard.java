package com.flashcards.flashcards_service.entity;

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

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "flashcards")
//TODO: maybe to add @Getter @Setter
@Setter
@Getter
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String frontText;

    private String backText;

    private String imageUrl;

    private boolean deleted = false;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
