import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstsumOptionsQuestionInstructionComponent } from './constsum-options-question-instruction.component';

describe('ConstsumOptionsQuestionInstructionComponent', () => {
  let component: ConstsumOptionsQuestionInstructionComponent;
  let fixture: ComponentFixture<ConstsumOptionsQuestionInstructionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConstsumOptionsQuestionInstructionComponent],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConstsumOptionsQuestionInstructionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
