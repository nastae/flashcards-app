package com.flashcards.flashcards_service.image_generation.impl;

import com.flashcards.flashcards_service.exception.ImageGenerationUnavailableException;
import com.flashcards.flashcards_service.image_generation.ImageGenerationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

//@Service
//@ConditionalOnMissingBean(ImageGenerationService.class)
public class UnavailableImageGenerationService { //implements ImageGenerationService {

//    @Override
//    public String generateImage(String prompt) {
//        throw new ImageGenerationUnavailableException(
//                "No image-generation provider is configured."
//        );
//    }
}
