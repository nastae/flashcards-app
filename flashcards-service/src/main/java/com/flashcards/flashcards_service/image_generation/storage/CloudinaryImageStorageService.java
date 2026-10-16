package com.flashcards.flashcards_service.image_generation.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryImageStorageService {

    private final Cloudinary cloudinary;

    public String upload(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException(
                    "Cannot upload an empty image"
            );
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    imageBytes,
                    ObjectUtils.asMap(
                            "resource_type", "image",
//                            "folder", "devcards/flashcards",
                            "upload_preset", "Flashcards-app",
                            "public_id", UUID.randomUUID().toString(),
                            "overwrite", false
                    )
            );

            Object secureUrl = result.get("secure_url");

            if (!(secureUrl instanceof String imageUrl) ||
                imageUrl.isBlank()) {
                throw new IllegalStateException(
                        "Cloudinary did not return an image URL."
                );
            }

            return imageUrl;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to upload image to Cloudinary",
                    e
            );
        }
    }
}
