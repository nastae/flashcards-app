package com.flashcards.flashcards_service.service;

import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.entity.Flashcard;
import com.flashcards.flashcards_service.mapper.FlashcardMapper;
import com.flashcards.flashcards_service.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardMapper flashcardMapper;

    public List<FlashcardResponseDto> getShuffledCards(Long groupId) {
        List<Flashcard> cards = flashcardRepository.findByGroupIdAndDeletedFalse(groupId);
        Collections.shuffle(cards);

        return cards.stream()
                .map(flashcardMapper::toResponseDto)
                .toList();
    }
}
