import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPageScrollCoreModule } from 'ngx-page-scroll-core';
import { PanelChevronModule } from '../../../components/panel-chevron/panel-chevron.module';
import { TeammatesRouterModule } from '../../../components/teammates-router/teammates-router.module';
import { InstructorHelpPanelComponent } from '../instructor-help-panel/instructor-help-panel.component';
import { InstructorHelpCoursesSectionComponent } from './instructor-help-courses-section.component';

describe('InstructorHelpCoursesSectionComponent', () => {
  let component: InstructorHelpCoursesSectionComponent;
  let fixture: ComponentFixture<InstructorHelpCoursesSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        InstructorHelpCoursesSectionComponent,
        InstructorHelpPanelComponent,
      ],
      imports: [NgbModule, RouterTestingModule, NgxPageScrollCoreModule,
        NoopAnimationsModule, PanelChevronModule, TeammatesRouterModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstructorHelpCoursesSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
