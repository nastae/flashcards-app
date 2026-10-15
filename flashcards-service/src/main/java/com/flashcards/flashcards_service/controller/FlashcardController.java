package com.flashcards.flashcards_service.controller;

import com.flashcards.flashcards_service.dto.FlashcardRequestDto;
import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/flashcards")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    @PostMapping("/{groupId}")
    public FlashcardResponseDto create(@PathVariable Long groupId, @RequestBody FlashcardRequestDto request) {
        return flashcardService.create(groupId, request);
    }

//    TODO: maybe for other endpoint requests use also ResponseEntity?
    @PostMapping("/groups/{groupId}/flashcards/{flashcardId}/image-generation/retry")
    public ResponseEntity<Void> retryImageGeneration(
            @PathVariable Long groupId,
            @PathVariable Long flashcardId) {
        flashcardService.retryImageGeneration(groupId, flashcardId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public FlashcardResponseDto getById(@PathVariable Long id) {
        return flashcardService.getById(id);
    }

    @GetMapping("/group/{groupId}")
    public List<FlashcardResponseDto> getByGroup(@PathVariable Long groupId) {
        return flashcardService.getByGroup(groupId);
    }

    @PutMapping("/{id}")
    public FlashcardResponseDto update(@PathVariable Long id, @RequestBody FlashcardRequestDto request) {
        return flashcardService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        flashcardService.softDelete(id);
    }
}
