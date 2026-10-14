package com.flashcards.flashcards_service.image_generation;

public interface ImageGenerationService {

    /**
     * Generates an image from the supplied prompt.
     *
     * @return a publicly accessible URL for the generated image
     */
    String generateImage(String prompt);
}
