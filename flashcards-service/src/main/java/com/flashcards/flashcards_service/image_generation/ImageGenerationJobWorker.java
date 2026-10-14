package com.flashcards.flashcards_service.image_generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageGenerationJobWorker {

    private final ImageGenerationService imageGenerationService;
    private final ImageGenerationStateService imageGenerationStateService;

    @Async("imageGenerationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ImageGenerationRequestedEvent event) {
        generate(event.flashcardId());
    }

    public void generate(Long flashcardId) {
        final Optional<String> prompt;

        try {
            prompt = imageGenerationStateService.markGenerating(flashcardId);

            if (prompt.isEmpty() || prompt.get().isBlank()) {
                return;
            }

            String imageUrl = imageGenerationService.generateImage(prompt.get());

            if (imageUrl == null || imageUrl.isBlank()) {
                throw new IllegalStateException(
                        "Image-generation service returned no image URL"
                );
            }

            imageGenerationStateService.markReady(flashcardId, imageUrl);

            log.info("Image generation completed for flashcard {}", flashcardId);

        } catch (Exception e) {
            log.error(
                    "Image generation failed for flashcard {}",
                    flashcardId,
                    e
            );

            imageGenerationStateService.markFailed(flashcardId);
        }
    }
}
