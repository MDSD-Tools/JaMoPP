import { Component } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { finalize } from 'rxjs/operators';
import { AccountService } from '../../../services/account.service';
import { EmailGenerationService } from '../../../services/email-generation.service';
import { InstructorService } from '../../../services/instructor.service';
import { LoadingBarService } from '../../../services/loading-bar.service';
import {
  AdminSearchResult,
  FeedbackSessionsGroup,
  InstructorAccountSearchResult,
  SearchService,
  StudentAccountSearchResult,
} from '../../../services/search.service';
import { SimpleModalService } from '../../../services/simple-modal.service';
import { StatusMessageService } from '../../../services/status-message.service';
import { StudentService } from '../../../services/student.service';
import { ApiConst } from '../../../types/api-const';
import { Email, RegenerateKey } from '../../../types/api-output';
import { SimpleModalType } from '../../components/simple-modal/simple-modal-type';
import { collapseAnim } from '../../components/teammates-common/collapse-anim';
import { ErrorMessageOutput } from '../../error-message-output';

/**
 * Admin search page.
 */
@Component({
  selector: 'tm-admin-search-page',
  templateUrl: './admin-search-page.component.html',
  styleUrls: ['./admin-search-page.component.scss'],
  animations: [collapseAnim],
})
export class AdminSearchPageComponent {

  searchQuery: string = '';
  instructors: InstructorAccountSearchResult[] = [];
  students: StudentAccountSearchResult[] = [];

  constructor(
    private statusMessageService: StatusMessageService,
    private simpleModalService: SimpleModalService,
    private accountService: AccountService,
    private instructorService: InstructorService,
    private studentService: StudentService,
    private searchService: SearchService,
    private emailGenerationService: EmailGenerationService,
    private loadingBarService: LoadingBarService,
  ) {}

  /**
   * Searches for students and instructors matching the search query.
   */
  search(): void {
    this.loadingBarService.showLoadingBar();
    this.searchService.searchAdmin(
        this.searchQuery,
    ).pipe(finalize(() => this.loadingBarService.hideLoadingBar())).subscribe((resp: AdminSearchResult) => {
      const hasStudents: boolean = !!(resp.students && resp.students.length);
      const hasInstructors: boolean = !!(resp.instructors && resp.instructors.length);

      if (!hasStudents && !hasInstructors) {
        this.statusMessageService.showWarningToast('No results found.');
        this.instructors = [];
        this.students = [];
        return;
      }

      this.instructors = resp.instructors;
      this.students = resp.students;
      this.hideAllInstructorsLinks();
      this.hideAllStudentsLinks();

      // prompt user to use more specific terms if search results limit reached
      const limit: number = ApiConst.SEARCH_QUERY_SIZE_LIMIT;
      const limitsReached: string[] = [];
      if (this.students.length >= limit) {
        limitsReached.push(`${limit} student results`);
      }
      if (this.instructors.length >= limit) {
        limitsReached.push(`${limit} instructor results`);
      }
      if (limitsReached.length) {
        this.statusMessageService.showWarningToast(`${limitsReached.join(' and ')} have been shown on this page
            but there may be more results not shown. Consider searching with more specific terms.`);
      }

    }, (resp: ErrorMessageOutput) => {
      this.instructors = [];
      this.students = [];
      this.statusMessageService.showErrorToast(resp.error.message);
    });
  }

  /**
   * Shows all instructors' links in the page.
   */
  showAllInstructorsLinks(): void {
    for (const instructor of this.instructors) {
      instructor.showLinks = true;
    }
  }

  /**
   * Hides all instructors' links in the page.
   */
  hideAllInstructorsLinks(): void {
    for (const instructor of this.instructors) {
      instructor.showLinks = false;
    }
  }

  /**
   * Shows all students' links in the page.
   */
  showAllStudentsLinks(): void {
    for (const student of this.students) {
      student.showLinks = true;
    }
  }

  /**
   * Hides all students' links in the page.
   */
  hideAllStudentsLinks(): void {
    for (const student of this.students) {
      student.showLinks = false;
    }
  }

  /**
   * Resets the instructor's Google ID.
   */
  resetInstructorGoogleId(instructor: InstructorAccountSearchResult, event: any): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    const modalContent: string = `Are you sure you want to reset the Google account ID currently associated for <strong>${ instructor.name }</strong> in the course <strong>${ instructor.courseId }</strong>?
        The user will need to re-associate their account with a new Google ID.`;
    const modalRef: NgbModalRef = this.simpleModalService.openConfirmationModal(
        `Reset <strong>${ instructor.name }</strong>\'s Google ID?`, SimpleModalType.WARNING, modalContent);

    modalRef.result.then(() => {
      this.accountService.resetInstructorAccount(instructor.courseId, instructor.email).subscribe(() => {
        this.search();
        this.statusMessageService.showSuccessToast('The instructor\'s Google ID has been reset.');
      }, (resp: ErrorMessageOutput) => {
        this.statusMessageService.showErrorToast(resp.error.message);
      });
    }, () => {});
  }

  /**
   * Resets the student's Google ID.
   */
  resetStudentGoogleId(student: StudentAccountSearchResult, event: any): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }
    const modalContent: string = `Are you sure you want to reset the Google account ID currently associated for <strong>${ student.name }</strong> in the course <strong>${ student.courseId }</strong>?
        The user will need to re-associate their account with a new Google ID.`;
    const modalRef: NgbModalRef = this.simpleModalService.openConfirmationModal(
        `Reset <strong>${ student.name }</strong>\'s Google ID?`, SimpleModalType.WARNING, modalContent);

    modalRef.result.then(() => {
      this.accountService.resetStudentAccount(student.courseId, student.email).subscribe(() => {
        student.googleId = '';
        this.statusMessageService.showSuccessToast('The student\'s Google ID has been reset.');
      }, (resp: ErrorMessageOutput) => {
        this.statusMessageService.showErrorToast(resp.error.message);
      });
    }, () => {});
  }

  /**
   * Regenerates the student's registration key.
   */
  regenerateStudentKey(student: StudentAccountSearchResult): void {
    const modalContent: string = `Are you sure you want to regenerate the registration key for <strong>${ student.name }</strong> for the course <strong>${ student.courseId }</strong>?
        An email will be sent to the student with all the new course registration and feedback session links.`;
    const modalRef: NgbModalRef = this.simpleModalService.openConfirmationModal(
        `Regenerate <strong>${ student.name }</strong>\'s course links?`, SimpleModalType.WARNING, modalContent);

    modalRef.result.then(() => {
      this.studentService.regenerateStudentKey(student.courseId, student.email)
        .subscribe((resp: RegenerateKey) => {
          this.statusMessageService.showSuccessToast(resp.message);
          this.updateDisplayedStudentCourseLinks(student, resp.newRegistrationKey);
        }, (response: ErrorMessageOutput) => {
          this.statusMessageService.showErrorToast(response.error.message);
        });
    }, () => {});
  }

  /**
   * Regenerates the instructor's registration key.
   */
  regenerateInstructorKey(instructor: InstructorAccountSearchResult): void {
    const modalContent: string = `Are you sure you want to regenerate the registration key for <strong>${ instructor.name }</strong> for the course <strong>${ instructor.courseId }</strong>?
        An email will be sent to the instructor with all the new course registration and feedback session links.`;
    const modalRef: NgbModalRef = this.simpleModalService.openConfirmationModal(
        `Regenerate <strong>${ instructor.name }</strong>'s course links?`, SimpleModalType.WARNING, modalContent);

    modalRef.result.then(() => {
      this.instructorService.regenerateInstructorKey(instructor.courseId, instructor.email)
          .subscribe((resp: RegenerateKey) => {
            this.statusMessageService.showSuccessToast(resp.message);
            this.updateDisplayedInstructorCourseLinks(instructor, resp.newRegistrationKey);
          }, (response: ErrorMessageOutput) => {
            this.statusMessageService.showErrorToast(response.error.message);
          });
    }, () => {});
  }

  /**
   * Updates the student's displayed course join and feedback session links with the value of the newKey.
   */
  private updateDisplayedStudentCourseLinks(student: StudentAccountSearchResult, newKey: string): void {
    const updateSessions: Function = (sessions: FeedbackSessionsGroup): void => {
      Object.keys(sessions).forEach((key: string): void => {
        sessions[key].feedbackSessionUrl = this.getUpdatedUrl(sessions[key].feedbackSessionUrl, newKey);
      });
    };

    student.courseJoinLink = this.getUpdatedUrl(student.courseJoinLink, newKey);
    updateSessions(student.awaitingSessions);
    updateSessions(student.openSessions);
    updateSessions(student.notOpenSessions);
    updateSessions(student.publishedSessions);
  }

  /**
   * Updates the instructor's displayed course join and feedback session links with the value of the newKey.
   */
  private updateDisplayedInstructorCourseLinks(instructor: InstructorAccountSearchResult, newKey: string): void {
    instructor.courseJoinLink = this.getUpdatedUrl(instructor.courseJoinLink, newKey);
  }

  /**
   * Returns the URL after replacing the value of the `key` parameter with that of the new key.
   */
  private getUpdatedUrl(link: string, newVal: string): string {
    const param: string = 'key';
    const regex: RegExp = new RegExp(`(${param}=)[^\&]+`);

    return link.replace(regex, `$1${newVal}`);
  }

  /**
   * Open up an email populated with content for course join invitation.
   */
  openCourseJoinEmail(courseId: string, studentemail: string): void {
    this.emailGenerationService.getCourseJoinEmail(courseId, studentemail)
        .subscribe((email: Email) => {
          window.location.href = `mailto:${email.recipient}`
              + `?Subject=${email.subject}`
              + `&body=${email.content}`;
        }, (err: ErrorMessageOutput) => {
          this.statusMessageService.showErrorToast(err.error.message);
        });
  }

  /**
   * Open up an email populated with content for feedback session reminder.
   */
  openFeedbackSessionReminderEmail(courseId: string, studentemail: string, fsname: string): void {
    this.emailGenerationService.getFeedbackSessionReminderEmail(courseId, studentemail, fsname)
        .subscribe((email: Email) => {
          window.location.href = `mailto:${email.recipient}`
              + `?Subject=${email.subject}`
              + `&body=${email.content}`;
        }, (err: ErrorMessageOutput) => {
          this.statusMessageService.showErrorToast(err.error.message);
        });
  }

}
