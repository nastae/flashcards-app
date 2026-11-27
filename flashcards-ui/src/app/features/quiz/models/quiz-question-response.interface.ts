import { QuizOptionResponse } from "./quiz-option-response.interface";

export interface QuizQuestionResponse {
    questionId: number;
    number: number;
    imageUrl: string | null;
    definition: string | null;
    options: QuizOptionResponse[];
    totalQuestions: number;
    score: number;
}
