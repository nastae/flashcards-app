package com.flashcards.flashcards_service.service;

import com.flashcards.flashcards_service.dto.FlashcardRequestDto;
import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.entity.Flashcard;
import com.flashcards.flashcards_service.entity.Group;
import com.flashcards.flashcards_service.mapper.FlashcardMapper;
import com.flashcards.flashcards_service.repository.FlashcardRepository;
import com.flashcards.flashcards_service.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final GroupRepository groupRepository;
    private final FlashcardMapper flashcardMapper;

//    TODO: throw specific error and handle that status on http request as response
    public FlashcardResponseDto create(Long groupId, FlashcardRequestDto request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow();

        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
//        TODO: save image to cloudinary and then set imageUrl
        flashcard.setImageUrl(request.imageUrl());
        flashcard.setGroup(group);

        Flashcard saved = flashcardRepository.save(flashcard);

        return flashcardMapper.toResponseDto(saved);
    }

    public FlashcardResponseDto getById(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .filter(f -> !f.isDeleted())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flashcard not found"));

        return flashcardMapper.toResponseDto(flashcard);
    }

    public List<FlashcardResponseDto> getByGroup(Long groupId) {
        return flashcardRepository.findByGroupIdAndDeletedFalse(groupId)
                .stream()
                .map(flashcardMapper::toResponseDto)
                .toList();
    }

//    TODO: update to throw specific exception and handle that on HTTP request level as response
    public FlashcardResponseDto update(Long id, FlashcardRequestDto request) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow();

        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
//        TODO: need functionality that says if image changed, and change image after that
//        TODO: if image changed, delete old from cloud cloudinary and upload new
//        TODO: upload image to cloudinary from spring boot backend (as planned)
        flashcard.setImageUrl(request.imageUrl());

        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);

        return flashcardMapper.toResponseDto(updatedFlashcard);
    }

//    TODO: set flag (probably boolean) that entity was successfully deleted
    public void softDelete(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow();

        flashcard.setDeleted(true);
        flashcardRepository.save(flashcard);
    }
}
