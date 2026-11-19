package com.flashcards.flashcards_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupRequestDto(
        String name,
        @NotBlank
        @Size(max = 35)
        String nativeLanguageCode,
        @NotBlank
        @Size(max = 35)
        String learningLanguageCode) {
}
