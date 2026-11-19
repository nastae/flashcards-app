package com.flashcards.flashcards_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "languages")
@Getter
@Setter
public class Language {

    @Id
    private String code;

    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Column(name = "native_name", nullable = false)
    private String nativeName;

    @Column(nullable = false)
    private boolean enabled;
}
