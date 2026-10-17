package com.flashcards.flashcards_service.mapper;

import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.entity.Flashcard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {

    @Mapping(target = "groupId", source = "group.id")
    FlashcardResponseDto toResponseDto(Flashcard flashcard);
}
