import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPageScrollCoreModule } from 'ngx-page-scroll-core';
import { AddingQuestionPanelModule } from '../../../components/adding-question-panel/adding-question-panel.module';
import {
    CommentBoxModule,
} from '../../../components/comment-box/comment-box.module';
import { FeedbackPathPanelModule } from '../../../components/feedback-path-panel/feedback-path-panel.module';
import { PanelChevronModule } from '../../../components/panel-chevron/panel-chevron.module';
import { PreviewSessionPanelModule } from '../../../components/preview-session-panel/preview-session-panel.module';
import { QuestionResponsePanelModule,
} from '../../../components/question-response-panel/question-response-panel.module';
import { SingleStatisticsModule,
} from '../../../components/question-responses/single-statistics/single-statistics.module';
import { StudentViewResponsesModule,
} from '../../../components/question-responses/student-view-responses/student-view-responses.module';
import { QuestionTextWithInfoModule } from '../../../components/question-text-with-info/question-text-with-info.module';
import { SessionEditFormModule } from '../../../components/session-edit-form/session-edit-form.module';
import {
  SessionsRecycleBinTableModule,
} from '../../../components/sessions-recycle-bin-table/sessions-recycle-bin-table.module';
import { TeammatesRouterModule } from '../../../components/teammates-router/teammates-router.module';
import { ViewResultsPanelModule } from '../../../components/view-results-panel/view-results-panel.module';
import { VisibilityPanelModule } from '../../../components/visibility-panel/visibility-panel.module';
import {
  InstructorSearchComponentsModule,
} from '../../../pages-instructor/instructor-search-page/instructor-search-components.module';
import {
  InstructorSessionResultViewModule,
} from '../../../pages-instructor/instructor-session-result-page/instructor-session-result-view.module';
import { ExampleBoxModule } from '../example-box/example-box.module';
import { InstructorHelpPanelComponent } from '../instructor-help-panel/instructor-help-panel.component';
import { InstructorHelpSessionsSectionComponent } from './instructor-help-sessions-section.component';

describe('InstructorHelpSessionsSectionComponent', () => {
  let component: InstructorHelpSessionsSectionComponent;
  let fixture: ComponentFixture<InstructorHelpSessionsSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        InstructorHelpSessionsSectionComponent,
        InstructorHelpPanelComponent,
      ],
      imports: [
        CommentBoxModule, FormsModule, HttpClientTestingModule, NgbModule, ExampleBoxModule,
        RouterTestingModule, NgxPageScrollCoreModule, NoopAnimationsModule,
        SessionEditFormModule, SessionsRecycleBinTableModule, TeammatesRouterModule,
        InstructorSearchComponentsModule, InstructorSessionResultViewModule,
        PreviewSessionPanelModule, QuestionTextWithInfoModule, AddingQuestionPanelModule,
        FeedbackPathPanelModule, SingleStatisticsModule, StudentViewResponsesModule, ViewResultsPanelModule,
        QuestionResponsePanelModule, VisibilityPanelModule, PanelChevronModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstructorHelpSessionsSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
