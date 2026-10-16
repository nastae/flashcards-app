package com.flashcards.flashcards_service.image_generation.prompt;

import com.flashcards.flashcards_service.entity.Flashcard;
import org.springframework.stereotype.Component;

@Component
public class ImagePromptBuilder {

    public String buildPrompt(Flashcard flashcard) {
        return """
                Generate an educational image representing the following word or concept.

                Word: %s
                Description: %s

                Requirements:
                - Clearly illustrate the main concept.
                - Create a visually understandable, memorable image.
                - Avoid text, labels, and watermarks.
                - Do not include any text, words, letters, numbers, labels, captions, watermarks, or written symbols in the image. Depict the concept purely through visual elements, without spelling out or explicitly naming the object or concept being represented.
                - Use a clean, high-quality illustration style.
                """.formatted(
                flashcard.getFrontText(),
                flashcard.getBackText()
        );
    }
}
