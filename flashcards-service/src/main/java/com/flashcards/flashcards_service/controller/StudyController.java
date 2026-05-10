package com.flashcards.flashcards_service.controller;

import com.flashcards.flashcards_service.dto.FlashcardResponseDto;
import com.flashcards.flashcards_service.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StudyController {

    private final StudyService studyService;

//    TODO: add postman endpoint
//    TODO: Later you can add:
//      GET    /groups/1/study
//      POST   /groups/1/study/session
//      GET    /groups/1/study/progress
//      POST   /groups/1/study/answer
//      /api/study/review
//      /api/study/statistics
    @GetMapping("/groups/{groupId}")
    public List<FlashcardResponseDto> getShuffledFlashcards(@PathVariable Long groupId) {
        return studyService.getShuffledCards(groupId);
    }
}
