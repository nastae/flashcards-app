import { ImageGenerationStatus } from "./image-generation-status";

export interface FlashcardRequest {
    frontText: string;
    backText: string;
    imageUrl: string;
    generateImageWithAi?: boolean;
    nativeWord?: string | null;
}
