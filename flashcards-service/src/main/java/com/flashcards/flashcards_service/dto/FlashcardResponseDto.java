package com.flashcards.flashcards_service.dto;

import com.flashcards.flashcards_service.enums.ImageGenerationStatus;

public record FlashcardResponseDto(
        Long id,
        String frontText,
        String backText,
        String imageUrl,
        Long groupId,
        ImageGenerationStatus imageGenerationStatus,
        String nativeWord) {
//    String groupName   // 👈 useful for UI
}
