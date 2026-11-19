package com.flashcards.flashcards_service.service;

import com.flashcards.flashcards_service.dto.GroupRequestDto;
import com.flashcards.flashcards_service.dto.GroupResponseDto;
import com.flashcards.flashcards_service.entity.Group;
import com.flashcards.flashcards_service.mapper.GroupMapper;
import com.flashcards.flashcards_service.repository.GroupRepository;
import com.flashcards.flashcards_service.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final LanguageRepository languageRepository;

    public GroupResponseDto create(GroupRequestDto request) {
        validateLanguages(request);
        Group group = new Group();
        group.setName(request.name());
        group.setNativeLanguageCode(request.nativeLanguageCode());
        group.setLearningLanguageCode(request.learningLanguageCode());

        Group saved = groupRepository.save(group);

        return groupMapper.toResponseDto(saved);
    }

//    TODO: add specific exception and handle that on HTTP request level as response
    public GroupResponseDto getById(Long id) {
        Group group = groupRepository.findById(id)
                .filter(g -> !g.isDeleted())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return groupMapper.toResponseDto(group);
    }

    public List<GroupResponseDto> getAll() {
        return groupRepository.findByDeletedFalse()
                .stream()
                .map(groupMapper::toResponseDto)
                .toList();
    }

//    TODO: write specific exception to later handle it that wrong request data
    public GroupResponseDto update(Long id, GroupRequestDto request) {
        validateLanguages(request);
        Group group = groupRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Group not found"));
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        group.setName(request.name());
        group.setNativeLanguageCode(request.nativeLanguageCode());
        group.setLearningLanguageCode(request.learningLanguageCode());

        Group updatedGroup = groupRepository.save(group);

        return groupMapper.toResponseDto(updatedGroup);
    }

//    TODO: add response status (boolean flag) to say that it was deleted?
    public void softDelete(Long id) {
        Group group = groupRepository.findById(id).orElseThrow();

        group.setDeleted(true);
        groupRepository.save(group);
    }

    private void validateLanguages(GroupRequestDto requestDto) {
        if (requestDto.nativeLanguageCode().equalsIgnoreCase(requestDto.learningLanguageCode())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Native and learning languages must be different. Both are: " + requestDto.nativeLanguageCode()
            );
        }

        if (!languageRepository.existsById(requestDto.nativeLanguageCode())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported native language: " + requestDto.nativeLanguageCode()
            );
        }

        if (!languageRepository.existsById(requestDto.learningLanguageCode())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported learning language: " + requestDto.learningLanguageCode()
            );
        }
    }
}
