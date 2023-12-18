import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { StatusMessageService } from '../../../services/status-message.service';
import { JoinState, MessageOutput, Student } from '../../../types/api-output';
import { StudentUpdateRequest } from '../../../types/api-request';
import { ErrorMessageOutput } from '../../error-message-output';

import { NavigationService } from '../../../services/navigation.service';
import { SimpleModalService } from '../../../services/simple-modal.service';
import { StudentService } from '../../../services/student.service';
import { FormValidator } from '../../../types/form-validator';
import { SimpleModalType } from '../../components/simple-modal/simple-modal-type';

/**
 * Instructor course student edit page.
 */
@Component({
  selector: 'tm-instructor-course-student-edit-page',
  templateUrl: './instructor-course-student-edit-page.component.html',
  styleUrls: ['./instructor-course-student-edit-page.component.scss'],
})
export class InstructorCourseStudentEditPageComponent implements OnInit, OnDestroy {

  FormValidator: typeof FormValidator = FormValidator; // enum

  @Input() isEnabled: boolean = true;
  courseId: string = '';
  studentEmail: string = '';
  student!: Student;

  isTeamnameFieldChanged: boolean = false;
  isEmailFieldChanged: boolean = false;
  isStudentLoading: boolean = false;
  hasStudentLoadingFailed: boolean = false;
  isFormSaving: boolean = false;

  editForm!: FormGroup;
  teamFieldSubscription?: Subscription;
  emailFieldSubscription?: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private statusMessageService: StatusMessageService,
              private studentService: StudentService,
              private navigationService: NavigationService,
              private ngbModal: NgbModal,
              private simpleModalService: SimpleModalService) { }

  ngOnInit(): void {
    if (!this.isEnabled) {
      this.student = {
        email: 'alice@email.com',
        courseId: '',
        name: 'Alice Betsy',
        comments: 'Alice is a transfer student.',
        teamName: 'Team A',
        sectionName: 'Section A',
        joinState: JoinState.JOINED,
      };
      this.initEditForm();
      return;
    }

    this.route.queryParams.subscribe((queryParams: any) => {
      this.courseId = queryParams.courseid;
      this.studentEmail = queryParams.studentemail;
      this.loadStudentEditDetails(queryParams.courseid, queryParams.studentemail);
    });
  }

  ngOnDestroy(): void {
    if (this.emailFieldSubscription) {
      (this.emailFieldSubscription as Subscription).unsubscribe();
    }
    if (this.teamFieldSubscription) {
      (this.teamFieldSubscription as Subscription).unsubscribe();
    }
  }

  /**
   * Loads student details required for this page.
   */
  loadStudentEditDetails(courseId: string, studentEmail: string): void {
    this.hasStudentLoadingFailed = false;
    this.isStudentLoading = true;
    this.studentService.getStudent(
        courseId, studentEmail,
    ).pipe(finalize(() => this.isStudentLoading = false)).subscribe((student: Student) => {
      this.student = student;
      this.initEditForm();
    }, (resp: ErrorMessageOutput) => {
      this.hasStudentLoadingFailed = true;
      this.statusMessageService.showErrorToast(resp.error.message);
    });
  }

  /**
   * Initializes the student details edit form with the fields fetched from the backend.
   * Subscriptions are set up to listen to changes in the 'teamname' fields and 'newstudentemail' fields.
   */
  private initEditForm(): void {
    this.editForm = new FormGroup({
      studentname: new FormControl(this.student.name,
          [Validators.required, Validators.maxLength(FormValidator.STUDENT_NAME_MAX_LENGTH)]),
      sectionname: new FormControl(this.student.sectionName,
          [Validators.required, Validators.maxLength(FormValidator.SECTION_NAME_MAX_LENGTH)]),
      teamname: new FormControl(this.student.teamName,
          [Validators.required, Validators.maxLength(FormValidator.TEAM_NAME_MAX_LENGTH)]),
      newstudentemail: new FormControl(this.student.email, // original student email initialized
          [Validators.required, Validators.maxLength(FormValidator.EMAIL_MAX_LENGTH)]),
      comments: new FormControl(this.student.comments),
    });
    this.teamFieldSubscription =
        (this.editForm.get('teamname') as AbstractControl).valueChanges
            .subscribe(() => {
              this.isTeamnameFieldChanged = true;
            });

    this.emailFieldSubscription =
        (this.editForm.get('newstudentemail') as AbstractControl).valueChanges
            .subscribe(() => this.isEmailFieldChanged = true);
  }

  /**
   * Displays message to user stating that the field is empty.
   */
  displayEmptyFieldMessage(fieldName: string): string {
    return `The field '${fieldName}' should not be empty.`;
  }

  /**
   * Displays message to user stating that the field exceeds the max length.
   */
  displayExceedMaxLengthMessage(fieldName: string, maxLength: number): string {
    return `The field '${fieldName}' should not exceed ${maxLength} characters.`;
  }

  /**
   * Handles logic related to showing the appropriate modal boxes
   * upon submission of the form. Submits the form otherwise.
   */
  onSubmit(resendPastLinksModal: any): void {
    if (!this.isEnabled) {
      return;
    }

    if (this.isTeamnameFieldChanged) {
      const modalContent: string = `Editing these fields will result in some existing responses from this student to be deleted.
            You may download the data before you make the changes.`;
      const modalRef: NgbModalRef = this.simpleModalService.openConfirmationModal(
          'Delete existing responses?', SimpleModalType.WARNING, modalContent);
      modalRef.result.then(() => {
        this.deleteExistingResponses(resendPastLinksModal);
      }, () => {});
    } else if (this.isEmailFieldChanged) {
      this.ngbModal.open(resendPastLinksModal);
    } else {
      this.submitEditForm(false);
    }
  }

  /**
   * Shows the `resendPastSessionLinks` modal if email field has changed.
   * Submits the form  otherwise.
   */
  deleteExistingResponses(resendPastLinksModal: any): void {
    if (this.isEmailFieldChanged) {
      this.ngbModal.open(resendPastLinksModal);
    } else {
      this.submitEditForm(false);
    }
  }

  /**
   * Submits the form data to edit the student details.
   */
  submitEditForm(shouldResendPastSessionLinks: boolean): void {
    const reqBody: StudentUpdateRequest = {
      name: this.editForm.value.studentname,
      email: this.editForm.value.newstudentemail,
      team: this.editForm.value.teamname,
      section: this.editForm.value.sectionname,
      comments: this.editForm.value.comments,
      isSessionSummarySendEmail: shouldResendPastSessionLinks,
    };

    this.isFormSaving = true;

    this.studentService.updateStudent({
      courseId: this.courseId,
      studentEmail: this.student.email,
      requestBody: reqBody,
    })
      .pipe(finalize(() => {
        this.isFormSaving = false;
      }))
      .subscribe((resp: MessageOutput) => {
        this.navigationService.navigateWithSuccessMessage(this.router, '/web/instructor/courses/details',
            resp.message, { courseid: this.courseId });
      }, (resp: ErrorMessageOutput) => {
        this.statusMessageService.showErrorToast(resp.error.message);
      });
  }
}
