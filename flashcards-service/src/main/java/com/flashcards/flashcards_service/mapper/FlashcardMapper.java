package com.flashcards.flashcards_service.mapper;

import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.entity.Flashcard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {

    FlashcardResponseDto toResponseDto(Flashcard flashcard);
}
