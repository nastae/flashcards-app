package com.flashcards.flashcards_service.controller;

import com.flashcards.flashcards_service.dto.LanguageResponse;
import com.flashcards.flashcards_service.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public List<LanguageResponse> getLanguages() {
        return languageService.getLanguages();
    }
}
