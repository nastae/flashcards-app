import { Injectable, signal } from '@angular/core';

const STORAGE_KEY = 'study-preferences';

interface StudyPreferences {
  alwaysShowAnswers: boolean
}

const DEFAULT_PREFERENCES: StudyPreferences = {
  alwaysShowAnswers: false
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

  setAlwaysShowAnswers(value: boolean): void {
    this.preferences.update(preferences => ({
      ...preferences,
      alwaysShowAnswers: value
    }));

    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify(this.preferences())
    );
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
