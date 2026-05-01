package com.flashcards.flashcards_service.controller;

import com.flashcards.flashcards_service.dto.GroupRequestDto;
import com.flashcards.flashcards_service.dto.GroupResponseDto;
import com.flashcards.flashcards_service.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public GroupResponseDto create(@RequestBody GroupRequestDto request) {
        return groupService.create(request);
    }

    @GetMapping("/{id}")
    public GroupResponseDto getById(@PathVariable Long id) {
        return groupService.getById(id);
    }

    @GetMapping
    public List<GroupResponseDto> getAll() {
        return groupService.getAll();
    }

//    TODO: idea
//      List<GroupResponseDto> getAllWithFlashcardCount()
//      shows number of cards per group (better UX)

    @PutMapping("/{id}")
    public GroupResponseDto update(@PathVariable Long id, @RequestBody GroupRequestDto request) {
        return groupService.update(id, request);
    }

//    TODO: return something, if successfully deleted or not successfully deleted
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.softDelete(id);
    }
}
