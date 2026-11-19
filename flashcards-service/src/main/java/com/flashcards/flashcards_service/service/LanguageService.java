package com.flashcards.flashcards_service.service;

import com.flashcards.flashcards_service.dto.LanguageResponse;
import com.flashcards.flashcards_service.mapper.LanguageMapper;
import com.flashcards.flashcards_service.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Transactional(readOnly = true)
    public List<LanguageResponse> getLanguages() {
        return languageRepository.findAllByEnabledTrueOrderByEnglishNameAsc()
                .stream()
                .map(languageMapper::toResponse)
                .toList();
    }
}
