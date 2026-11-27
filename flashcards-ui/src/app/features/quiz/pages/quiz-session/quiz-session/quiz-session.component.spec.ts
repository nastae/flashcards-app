import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizSessionComponent } from './quiz-session.component';

describe('QuizSessionComponent', () => {
  let component: QuizSessionComponent;
  let fixture: ComponentFixture<QuizSessionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizSessionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(QuizSessionComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
