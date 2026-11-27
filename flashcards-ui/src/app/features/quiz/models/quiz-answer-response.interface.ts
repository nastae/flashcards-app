export interface QuizAnswerResponse {
    isCorrect: boolean;
    correctAnswer: string;
    correctFlashcardId: number;
    correctAnswers: number;
    totalQuestions: number;
    quizCompleted: boolean;
}
