package com.flashcards.flashcards_service.dto;

//TODO: need to recheck about imageUrl, because plan is that spring boot backend sends image to save on cloudinary
public record FlashcardRequestDto(String frontText, String backText, String imageUrl) {
}
