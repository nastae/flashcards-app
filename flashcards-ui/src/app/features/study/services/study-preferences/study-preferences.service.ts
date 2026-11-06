import { Injectable, signal } from '@angular/core';
import { StudyDirection } from '../../models/study-direction/study-direction.type';

const STORAGE_KEY = 'study-preferences';

interface StudyPreferences {
  alwaysShowAnswers: boolean;
  studyDirection: StudyDirection;
}

const DEFAULT_PREFERENCES: StudyPreferences = {
  alwaysShowAnswers: false,
  studyDirection: 'NATIVE_TO_LEARNING'
};

@Injectable({
  providedIn: 'root',
})
export class StudyPreferencesService {
  private readonly preferences = signal<StudyPreferences>(
    this.loadPreferences()
  );

  readonly alwaysShowAnswers = () => 
    this.preferences().alwaysShowAnswers;

  readonly studyDirection = () =>
    this.preferences().studyDirection;

  setAlwaysShowAnswers(value: boolean): void {
    this.preferences.update(preferences => ({
      ...preferences,
      alwaysShowAnswers: value
    }));

    this.savePreferences();
  }

  private savePreferences(): void {
    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify(this.preferences())
    );
  }

  setStudyDirection(value: StudyDirection): void {
    this.preferences.update(preferences => ({
      ...preferences,
      studyDirection: value
    }));

    this.savePreferences();
  }

  private loadPreferences(): StudyPreferences {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      
      return stored 
        ? {...DEFAULT_PREFERENCES, ...JSON.parse(stored)}
        : DEFAULT_PREFERENCES;      
    } catch (error) {
      console.error(`Failed to load preferences from localStorage (${STORAGE_KEY}): `, error);
      return DEFAULT_PREFERENCES;
    }
  }
}
