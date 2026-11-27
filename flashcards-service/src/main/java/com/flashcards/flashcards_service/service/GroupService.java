package com.flashcards.flashcards_service.service;

import com.flashcards.flashcards_service.dto.GroupRequestDto;
import com.flashcards.flashcards_service.dto.GroupResponseDto;
import com.flashcards.flashcards_service.dto.quiz.ActiveQuizProjection;
import com.flashcards.flashcards_service.dto.quiz.ActiveQuizResponseDto;
import com.flashcards.flashcards_service.dto.quiz.GroupQuizCountProjection;
import com.flashcards.flashcards_service.entity.Group;
import com.flashcards.flashcards_service.enums.quiz.QuizSessionStatus;
import com.flashcards.flashcards_service.mapper.GroupMapper;
import com.flashcards.flashcards_service.mapper.quiz.QuizMapper;
import com.flashcards.flashcards_service.repository.FlashcardRepository;
import com.flashcards.flashcards_service.repository.GroupRepository;
import com.flashcards.flashcards_service.repository.LanguageRepository;
import com.flashcards.flashcards_service.repository.quiz.QuizSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final LanguageRepository languageRepository;
    private final FlashcardRepository flashcardRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizMapper quizMapper;

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
        List<Group> groups = groupRepository.findByDeletedFalse();

        if (groups.isEmpty()) {
            return List.of();
        }

        List<Long> groupIds = groups.stream()
                .map(Group::getId)
                .toList();

        Map<Long, Long> quizCounts = flashcardRepository
                .countQuizEligibleByGroupId(groupIds)
                .stream()
                .collect(
                        Collectors.toMap(
                                GroupQuizCountProjection::getGroupId,
                                GroupQuizCountProjection::getQuizEligibleCount
                        )
                );

        Map<Long, ActiveQuizResponseDto> activeQuizzes = quizSessionRepository
                .findActiveQuizzes(groupIds, QuizSessionStatus.IN_PROGRESS)
                .stream()
                .collect(Collectors.toMap(
                        ActiveQuizProjection::getGroupId,
                        quizMapper::toQuizResponseDto
                ));

        return groups.stream()
                .map(group -> groupMapper.toResponseDto(
                        group,
                        quizCounts.getOrDefault(group.getId(), 0L),
                        activeQuizzes.get(group.getId())
                ))
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
