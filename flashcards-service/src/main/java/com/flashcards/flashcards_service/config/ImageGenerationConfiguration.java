package com.flashcards.flashcards_service.config;

import com.flashcards.flashcards_service.exception.ImageGenerationUnavailableException;
import com.flashcards.flashcards_service.image_generation.ImageGenerationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageGenerationConfiguration {

    @Bean
    @ConditionalOnMissingBean(ImageGenerationService.class)
    ImageGenerationService unavailableImageGenerationService() {
        return prompt -> {
            throw new ImageGenerationUnavailableException(
                    "No image-generation provider is configured."
            );
        };
    }
}
