import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsModule } from '@angular/forms';
import { MsqFieldComponent } from './msq-field.component';

describe('MsqFieldComponent', () => {
  let component: MsqFieldComponent;
  let fixture: ComponentFixture<MsqFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MsqFieldComponent],
      imports: [
        FormsModule,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MsqFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
