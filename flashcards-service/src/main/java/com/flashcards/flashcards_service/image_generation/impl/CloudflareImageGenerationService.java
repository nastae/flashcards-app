package com.flashcards.flashcards_service.image_generation.impl;

import com.flashcards.flashcards_service.image_generation.ImageGenerationService;
import com.flashcards.flashcards_service.image_generation.storage.CloudinaryImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudflareImageGenerationService implements ImageGenerationService {

    private static final Long MAX_PROMPT_LENGTH = 2048L;

    private final CloudinaryImageStorageService cloudinaryImageStorageService;

    @Value("${cloudflare.workers-ai.account-id}")
    private String accountId;

    @Value("${cloudflare.workers-ai.api-token}")
    private String apiToken;

    @Value("${cloudflare.workers-ai.model}")
    private String model;

    @Value("${cloudflare.workers-ai.steps:4}")
    private int steps;

    private final RestClient restClient = createRestClient();

    private RestClient createRestClient() {
        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(120));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

//    TODO: maybe separate and orchestrate image generation and upload
    @Override
    public String generateImage(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException(
                    "Image generation prompt must not be empty"
            );
        }

        if (prompt.length() > MAX_PROMPT_LENGTH) {
            throw new IllegalArgumentException(
                    "Image generation prompt must not exceed %s characters.".formatted(
                            MAX_PROMPT_LENGTH
                    )
            );
        }

        String endpoint =
                "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s".formatted(
                        accountId,
                        model
                );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("steps", steps);

        try {
            log.info("Requesting image generation from Cloudflare.");

            long startTime = System.nanoTime();

            JsonNode response = restClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + apiToken)
                    .contentType(APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new IllegalStateException(
                        "Cloudflare returned an empty response"
                );
            }

            String encodedImage = response
                    .path("result")
                    .path("image")
                    .asText(null);

            if (encodedImage == null || encodedImage.isBlank()) {
                throw new IllegalStateException(
                        "Cloudflare response did not contain image data."
                );
            }

            byte[] imageBytes = Base64.getDecoder().decode(encodedImage);

            long generationTime = (System.nanoTime() - startTime) / 1_000_000;

            log.info("Cloudflare generated image data in {} ms ({} bytes)",
                    generationTime,
                    imageBytes.length
            );

            String imageUrl = cloudinaryImageStorageService.upload(imageBytes);

            log.info("Generated image uploaded to Cloudinary.");

            return imageUrl;
        } catch (Exception e) {
            log.error("Image generation or upload failed.", e);

            throw new IllegalStateException(
                    "Failed to generate or upload the flashcard image.",
                    e
            );
        }
    }
}
