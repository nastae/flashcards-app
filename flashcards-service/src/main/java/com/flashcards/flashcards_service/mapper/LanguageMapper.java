package com.flashcards.flashcards_service.mapper;

import com.flashcards.flashcards_service.dto.LanguageResponse;
import com.flashcards.flashcards_service.entity.Language;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    LanguageResponse toResponse(Language language);
}
