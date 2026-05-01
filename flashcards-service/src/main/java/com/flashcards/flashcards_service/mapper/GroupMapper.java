package com.flashcards.flashcards_service.mapper;

import com.flashcards.flashcards_service.dto.GroupResponseDto;
import com.flashcards.flashcards_service.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponseDto toResponseDto(Group group);
}
