import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsModule } from '@angular/forms';
import { RankOptionsFieldComponent } from './rank-options-field.component';

describe('RankOptionsFieldComponent', () => {
  let component: RankOptionsFieldComponent;
  let fixture: ComponentFixture<RankOptionsFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RankOptionsFieldComponent],
      imports: [
        FormsModule,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RankOptionsFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
