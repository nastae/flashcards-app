package com.flashcards.flashcards_service.dto;

public record FlashcardResponseDto(Long id, String frontText, String backText, String imageUrl, Long groupId) {
//    String groupName   // 👈 useful for UI
}
