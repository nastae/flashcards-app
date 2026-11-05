import { TestBed } from '@angular/core/testing';

import { StudyPreferencesService } from './study-preferences.service';

describe('StudyPreferencesService', () => {
  let service: StudyPreferencesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudyPreferencesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
