import { QuizQuestionResponse } from "./quiz-question-response.interface";

export interface QuizSessionResponse {
    sessionId: number;
    totalQuestions: number;
    question: QuizQuestionResponse;
}
