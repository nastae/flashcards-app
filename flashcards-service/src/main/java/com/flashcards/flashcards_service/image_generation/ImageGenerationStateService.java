package com.flashcards.flashcards_service.image_generation;

import com.flashcards.flashcards_service.entity.Flashcard;
import com.flashcards.flashcards_service.image_generation.prompt.ImagePromptBuilder;
import com.flashcards.flashcards_service.repository.FlashcardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.flashcards.flashcards_service.enums.ImageGenerationStatus.FAILED;
import static com.flashcards.flashcards_service.enums.ImageGenerationStatus.GENERATING;
import static com.flashcards.flashcards_service.enums.ImageGenerationStatus.PENDING;
import static com.flashcards.flashcards_service.enums.ImageGenerationStatus.READY;

@Service
@RequiredArgsConstructor
public class ImageGenerationStateService {

    private final FlashcardRepository flashcardRepository;
    private final ImagePromptBuilder imagePromptBuilder;

    @Transactional
    public Optional<String> markGenerating(Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Flashcard not found"));

        if (PENDING != flashcard.getImageGenerationStatus()) {
            return Optional.empty();
        }

        flashcard.setImageGenerationStatus(GENERATING);
//        unnecessary because dirty checking persists the change when the transaction commits.
//        flashcardRepository.save(flashcard);

//        TODO: update to use SRP
//        return Optional.of(constructPrompt(flashcard));
        return Optional.ofNullable(imagePromptBuilder.buildPrompt(flashcard));
    }

    @Transactional
    public void markReady(Long flashcardId, String imageUrl) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Flashcard not found"));

        flashcard.setImageUrl(imageUrl);
        flashcard.setImageGenerationStatus(READY);

//        flashcardRepository.save(flashcard);
    }

    @Transactional
    public void markFailed(Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Flashcard not found"));

        flashcard.setImageGenerationStatus(FAILED);

//        flashcardRepository.save(flashcard);
    }

    @Transactional
    public void markPendingForRetry(Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Flashcard not found"));

        if (FAILED != flashcard.getImageGenerationStatus()) {
            throw new IllegalStateException(
                    "Only failed image generation can be retried"
            );
        }

        flashcard.setImageGenerationStatus(PENDING);
//        flashcardRepository.save(flashcard);
    }

//    private String constructPrompt(Flashcard flashcard) {
//        return "Please generate image which represents the word: " + flashcard.getFrontText() +
//                ", additional description about image is: " + flashcard.getBackText() +
//                ", please create image that represent that word: " + flashcard.getFrontText();
//    }
}
