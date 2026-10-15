package com.flashcards.flashcards_service.service;

import com.flashcards.flashcards_service.dto.FlashcardRequestDto;
import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.entity.Flashcard;
import com.flashcards.flashcards_service.entity.Group;
import com.flashcards.flashcards_service.image_generation.ImageGenerationRequestedEvent;
import com.flashcards.flashcards_service.image_generation.ImageGenerationStateService;
import com.flashcards.flashcards_service.mapper.FlashcardMapper;
import com.flashcards.flashcards_service.repository.FlashcardRepository;
import com.flashcards.flashcards_service.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.flashcards.flashcards_service.enums.ImageGenerationStatus.*;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final GroupRepository groupRepository;
    private final FlashcardMapper flashcardMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageGenerationStateService imageGenerationStateService;

//    TODO: throw specific error and handle that status on http request as response
    public FlashcardResponseDto create(Long groupId, FlashcardRequestDto request) {
        boolean generateWithAi = request.requestsAiImage();

        if (generateWithAi &&
            request.frontText() == null || request.frontText().isBlank()) {
            throw new IllegalArgumentException(
                    "And image-generation front text is required"
            );
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Group not found"));

        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
//        TODO: save image to cloudinary and then set imageUrl
//        flashcard.setImageUrl(request.imageUrl());
        flashcard.setGroup(group);

        if (generateWithAi) {
            flashcard.setImageUrl(null);
            flashcard.setImageGenerationStatus(PENDING);
        } else {
            flashcard.setImageUrl(request.imageUrl());
            flashcard.setImageGenerationStatus(NONE);
        }

        Flashcard saved = flashcardRepository.save(flashcard);

        if (generateWithAi) {
            eventPublisher.publishEvent(
                    new ImageGenerationRequestedEvent(saved.getId())
            );
        }

        return flashcardMapper.toResponseDto(saved);
    }

//    TODO: maybe update error handling classes
    @Transactional
    public void retryImageGeneration(Long groupId, Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Flashcard not found"));

        var flashcardGroupId = flashcard.getGroup().getId();
        if (!flashcardGroupId.equals(groupId)) {
            throw new IllegalArgumentException(
                    "Flashcard with group ID %s does not belong to this group ID %s".formatted(
                            flashcardGroupId,
                            groupId
                    )
            );
        }

        if (flashcard.getFrontText() == null || flashcard.getFrontText().isBlank()) {
            throw new IllegalStateException(
                    "This flashcard with ID %s has no image-generation front text".formatted(
                            flashcard.getId()
                    )
            );
        }

        imageGenerationStateService.markPendingForRetry(flashcardId);

        eventPublisher.publishEvent(
                new ImageGenerationRequestedEvent(flashcardId)
        );
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
