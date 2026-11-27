import { ActiveQuizResponse } from "../../quiz/models/active-quiz-response.interface";

export interface GroupResponse {
  id: number;
  name: string;
  nativeLanguageCode: string;
  learningLanguageCode: string;
  quizEligibleCount: number;
  activeQuiz: ActiveQuizResponse
}
