package com.flashcards.flashcards_service.repository;

import com.flashcards.flashcards_service.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, String> {

    List<Language> findAllByEnabledTrueOrderByEnglishNameAsc();
}
