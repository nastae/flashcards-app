package com.flashcards.flashcards_service.image_generation.prompt;

import com.flashcards.flashcards_service.entity.Flashcard;
import org.springframework.stereotype.Component;

@Component
public class ImagePromptBuilder {

    public String buildPrompt(Flashcard flashcard) {
        return """
                The main rule is do not include any text and the word that need to be described with image.
                Generate an educational image representing the following word or concept, but do not write the word into picture.

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
//        return """
//            Generate an educational, purely visual illustration representing the concept described below.
//
//            Concept/Word to visualize: %s
//            Context/Description: %s
//
//            Visual Style Guidelines:
//            - Focus exclusively on metaphorical, symbolic, and visual elements.
//            - Clean, high-quality, memorable illustration style.
//            - Absolutely zero typography, zero letters, zero characters, zero writing, zero labels, and zero watermarks of any kind.
//            - Do not render, spell out, or write the concept name anywhere in the image. Pure visual imagery only.
//            """.formatted(
//                flashcard.getFrontText(),
//                flashcard.getBackText()
//        );
    }
}
