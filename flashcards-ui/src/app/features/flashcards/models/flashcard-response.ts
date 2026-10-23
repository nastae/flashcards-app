import { ImageGenerationStatus } from "./image-generation-status";

export interface FlashcardResponse {
    // TODO: It is really needed to expose id?
    id: number;
    frontText: string;
    // TODO: change to required
    backText?: string;
    imageUrl?: string;
    // TODO: I dont know if it is needed for request or response
    imageGenerationStatus?: ImageGenerationStatus;
    nativeWord?: string | null;
}
