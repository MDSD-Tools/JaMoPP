import { Component, OnInit } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Student, StudentProfile } from '../../../../types/api-output';
import { collapseAnim } from '../../../components/teammates-common/collapse-anim';
import {
  SearchStudentsListRowTable,
} from '../../../pages-instructor/instructor-search-page/student-result-table/student-result-table.component';
import { InstructorHelpSectionComponent } from '../instructor-help-section.component';
import {
  EXAMPLE_MULTIPLE_STUDENT_RESULT_TABLES,
  EXAMPLE_SINGLE_STUDENT_RESULT_TABLES,
  EXAMPLE_STUDENT_ATTRIBUTES,
  EXAMPLE_STUDENT_PROFILE,
} from './instructor-help-students-data';
import { StudentsSectionQuestions } from './students-section-questions';

/**
 * Students Section of the Instructor Help Page.
 */
@Component({
  selector: 'tm-instructor-help-students-section',
  templateUrl: './instructor-help-students-section.component.html',
  styleUrls: ['./instructor-help-students-section.component.scss'],
  animations: [collapseAnim],
})
export class InstructorHelpStudentsSectionComponent extends InstructorHelpSectionComponent implements OnInit {

  // enum
  StudentsSectionQuestions: typeof StudentsSectionQuestions = StudentsSectionQuestions;

  readonly supportEmail: string = environment.supportEmail;
  readonly exampleStudentProfile: StudentProfile = EXAMPLE_STUDENT_PROFILE;
  readonly exampleStudentAttributes: Student = EXAMPLE_STUDENT_ATTRIBUTES;
  readonly exampleSingleStudentResultTables: SearchStudentsListRowTable[] = EXAMPLE_SINGLE_STUDENT_RESULT_TABLES;
  readonly exampleMultipleStudentResultTables: SearchStudentsListRowTable[] = EXAMPLE_MULTIPLE_STUDENT_RESULT_TABLES;

  readonly questionsOrder: string[] = [
    StudentsSectionQuestions.STUDENT_VIEW_PROFILE,
    StudentsSectionQuestions.STUDENT_EDIT_DETAILS,
    StudentsSectionQuestions.STUDENT_VIEW_RESPONSES,
    StudentsSectionQuestions.STUDENT_SEARCH,
    StudentsSectionQuestions.STUDENT_EMAIL,
    StudentsSectionQuestions.STUDENT_GOOGLE_ACCOUNT,
    StudentsSectionQuestions.STUDENT_CHANGE_ID,
  ];

  getQuestionsOrder(): string[] {
    return this.questionsOrder;
  }

}
