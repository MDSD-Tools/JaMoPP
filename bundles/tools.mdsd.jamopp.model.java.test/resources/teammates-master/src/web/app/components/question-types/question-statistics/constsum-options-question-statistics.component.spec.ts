import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SortableTableModule } from '../../sortable-table/sortable-table.module';
import { ConstsumOptionsQuestionStatisticsComponent } from './constsum-options-question-statistics.component';
import { default as responses } from './test-data/constsumOptionQuestionResponses.json';

describe('ConstsumOptionsQuestionStatisticsComponent', () => {
  let component: ConstsumOptionsQuestionStatisticsComponent;
  let fixture: ComponentFixture<ConstsumOptionsQuestionStatisticsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConstsumOptionsQuestionStatisticsComponent],
      imports: [SortableTableModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConstsumOptionsQuestionStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should calculate statistics correctly', () => {

    component.question.constSumOptions = ['optionA', 'optionB', 'optionC'];
    component.responses = JSON.parse(JSON.stringify(responses.responses));

    component.calculateStatistics();

    const expectedPointsPerOption: Record<string, number[]> = {
      optionA: [10, 30, 50], optionB: [50, 70, 90], optionC: [0, 0, 0],
    };
    const expectedTotalPointsPerOption: Record<string, number> = {
      optionA: 90, optionB: 210, optionC: 0,
    };
    const expectedAveragePointsPerOption: Record<string, number> = {
      optionA: 30, optionB: 70, optionC: 0,
    };

    expect(component.pointsPerOption).toEqual(expectedPointsPerOption);
    expect(component.totalPointsPerOption).toEqual(expectedTotalPointsPerOption);
    expect(component.averagePointsPerOption).toEqual(expectedAveragePointsPerOption);
  });
});
