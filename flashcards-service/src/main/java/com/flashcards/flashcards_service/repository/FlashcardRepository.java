package com.flashcards.flashcards_service.repository;

import com.flashcards.flashcards_service.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByGroupIdAndDeletedFalse(Long groupId);
}
