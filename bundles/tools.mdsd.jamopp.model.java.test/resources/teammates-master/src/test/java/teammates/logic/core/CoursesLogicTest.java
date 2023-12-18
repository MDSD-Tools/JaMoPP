package teammates.logic.core;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseCommentAttributes;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.FieldValidator;
import teammates.storage.api.CoursesDb;
import teammates.test.AssertHelper;

/**
 * SUT: {@link CoursesLogic}.
 */
public class CoursesLogicTest extends BaseLogicTest {

    private final AccountsLogic accountsLogic = AccountsLogic.inst();
    private final CoursesLogic coursesLogic = CoursesLogic.inst();
    private final CoursesDb coursesDb = CoursesDb.inst();
    private final FeedbackQuestionsLogic fqLogic = FeedbackQuestionsLogic.inst();
    private final FeedbackResponsesLogic frLogic = FeedbackResponsesLogic.inst();
    private final FeedbackResponseCommentsLogic frcLogic = FeedbackResponseCommentsLogic.inst();
    private final FeedbackSessionsLogic fsLogic = FeedbackSessionsLogic.inst();
    private final InstructorsLogic instructorsLogic = InstructorsLogic.inst();
    private final StudentsLogic studentsLogic = StudentsLogic.inst();

    @Override
    protected void prepareTestData() {
        // test data is refreshed before each test case
    }

    @BeforeMethod
    public void refreshTestData() {
        dataBundle = getTypicalDataBundle();
        removeAndRestoreTypicalDataBundle();
    }

    @Test
    public void testUpdateCourseCascade_shouldCascadeUpdateTimezoneOfFeedbackSessions() throws Exception {
        CourseAttributes typicalCourse1 = dataBundle.courses.get("typicalCourse1");
        assertNotEquals(ZoneId.of("UTC"), typicalCourse1.getTimeZone());

        coursesLogic.updateCourseCascade(
                CourseAttributes.updateOptionsBuilder(typicalCourse1.getId())
                        .withTimezone(ZoneId.of("UTC"))
                        .build());

        List<FeedbackSessionAttributes> sessionsOfCourse = fsLogic.getFeedbackSessionsForCourse(typicalCourse1.getId());
        assertFalse(sessionsOfCourse.isEmpty());
        assertTrue(sessionsOfCourse.stream().allMatch(s -> s.getTimeZone().equals(ZoneId.of("UTC"))));
    }

    @Test
    public void testAll() throws Exception {
        testGetCourse();
        testGetSoftDeletedCoursesForInstructors();
        testIsCoursePresent();
        testVerifyCourseIsPresent();
        testGetSectionsNameForCourse();
        testGetTeamsForCourse();
        testGetCoursesForStudentAccount();
        testCreateCourse();
        testCreateCourseAndInstructor();
        testMoveCourseToRecycleBin();
        testRestoreCourseFromRecycleBin();
        testUpdateCourseCascade();
    }

    private void testGetCourse() throws Exception {

        ______TS("failure: course doesn't exist");

        assertNull(coursesLogic.getCourse("nonexistant-course"));

        ______TS("success: typical case");

        CourseAttributes c = CourseAttributes
                .builder("Computing101-getthis")
                .withName("Basic Computing Getting")
                .withTimezone(ZoneId.of("UTC"))
                .withInstitute("Test institute")
                .build();
        coursesDb.createEntity(c);

        assertEquals(c.getId(), coursesLogic.getCourse(c.getId()).getId());
        assertEquals(c.getName(), coursesLogic.getCourse(c.getId()).getName());

        coursesDb.deleteCourse(c.getId());
        ______TS("Null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.getCourse(null));
    }

    private void testGetSoftDeletedCoursesForInstructors() {

        ______TS("success: instructors with deleted courses");

        InstructorAttributes instructor = dataBundle.instructors.get("instructor1OfCourse3");

        List<InstructorAttributes> instructors = new ArrayList<>();
        instructors.add(instructor);

        List<CourseAttributes> courses = coursesLogic.getSoftDeletedCoursesForInstructors(instructors);

        assertEquals(1, courses.size());

        ______TS("boundary: instructor without any courses");

        instructors.remove(0);
        instructor = dataBundle.instructors.get("instructor5");
        instructors.add(instructor);

        courses = coursesLogic.getSoftDeletedCoursesForInstructors(instructors);

        assertEquals(0, courses.size());

        ______TS("Null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.getSoftDeletedCoursesForInstructors(null));
    }

    private void testIsCoursePresent() {

        ______TS("typical case: not an existent course");

        CourseAttributes nonExistentCourse = CourseAttributes
                .builder("non-existent-course")
                .withName("non existent course")
                .withTimezone(ZoneId.of("UTC"))
                .build();

        assertFalse(coursesLogic.isCoursePresent(nonExistentCourse.getId()));

        ______TS("typical case: an existent course");

        CourseAttributes existingCourse = CourseAttributes
                .builder("idOfTypicalCourse1")
                .withName("existing course")
                .withTimezone(ZoneId.of("UTC"))
                .build();

        assertTrue(coursesLogic.isCoursePresent(existingCourse.getId()));

        ______TS("Null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.isCoursePresent(null));
    }

    private void testVerifyCourseIsPresent() throws Exception {

        ______TS("typical case: verify a non-existent course");

        CourseAttributes nonExistentCourse = CourseAttributes
                .builder("non-existent-course")
                .withName("non existent course")
                .withTimezone(ZoneId.of("UTC"))
                .build();

        EntityDoesNotExistException ednee = assertThrows(EntityDoesNotExistException.class,
                () -> coursesLogic.verifyCourseIsPresent(nonExistentCourse.getId()));
        AssertHelper.assertContains("Course does not exist: ", ednee.getMessage());

        ______TS("typical case: verify an existent course");

        CourseAttributes existingCourse = CourseAttributes
                .builder("idOfTypicalCourse1")
                .withName("existing course")
                .withTimezone(ZoneId.of("UTC"))
                .build();
        coursesLogic.verifyCourseIsPresent(existingCourse.getId());

        ______TS("Null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.verifyCourseIsPresent(null));
    }

    private void testGetSectionsNameForCourse() throws Exception {

        ______TS("Typical case: course with sections");

        CourseAttributes typicalCourse1 = dataBundle.courses.get("typicalCourse1");
        assertEquals(2, coursesLogic.getSectionsNameForCourse(typicalCourse1.getId()).size());
        assertEquals("Section 1", coursesLogic.getSectionsNameForCourse(typicalCourse1.getId()).get(0));
        assertEquals("Section 2", coursesLogic.getSectionsNameForCourse(typicalCourse1.getId()).get(1));

        ______TS("Typical case: course without sections");

        CourseAttributes typicalCourse2 = dataBundle.courses.get("typicalCourse2");
        assertTrue(coursesLogic.getSectionsNameForCourse(typicalCourse2.getId()).isEmpty());

        ______TS("Failure case: course does not exists");

        EntityDoesNotExistException ednee = assertThrows(EntityDoesNotExistException.class,
                () -> coursesLogic.getSectionsNameForCourse("non-existent-course"));
        AssertHelper.assertContains("does not exist", ednee.getMessage());

        ______TS("Failure case: null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.getSectionsNameForCourse(null));
    }

    private void testGetTeamsForCourse() throws Exception {

        ______TS("typical case");

        CourseAttributes course = dataBundle.courses.get("typicalCourse1");
        List<String> teams = coursesLogic.getTeamsForCourse(course.getId());

        assertEquals(2, teams.size());
        assertEquals("Team 1.1</td></div>'\"", teams.get(0));
        assertEquals("Team 1.2", teams.get(1));

        ______TS("course without students");

        accountsLogic.createAccount(AccountAttributes.builder("instructor1")
                .withName("Instructor 1")
                .withEmail("instructor@email.tmt")
                .withInstitute("TEAMMATES Test Institute 1")
                .withIsInstructor(true)
                .build());
        coursesLogic.createCourseAndInstructor("instructor1",
                CourseAttributes.builder("course1")
                        .withName("course 1")
                        .withTimezone(ZoneId.of("UTC"))
                        .withInstitute("TEAMMATES Test Institute 1")
                        .build());
        teams = coursesLogic.getTeamsForCourse("course1");

        assertEquals(0, teams.size());

        coursesLogic.deleteCourseCascade("course1");
        accountsLogic.deleteAccountCascade("instructor1");

        ______TS("non-existent");

        EntityDoesNotExistException ednee = assertThrows(EntityDoesNotExistException.class,
                () -> coursesLogic.getTeamsForCourse("non-existent-course"));
        AssertHelper.assertContains("does not exist", ednee.getMessage());

        ______TS("null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.getTeamsForCourse(null));
    }

    private void testGetCoursesForStudentAccount() {

        ______TS("student having two courses");

        StudentAttributes studentInTwoCourses = dataBundle.students
                .get("student2InCourse1");
        List<CourseAttributes> courseList = coursesLogic
                .getCoursesForStudentAccount(studentInTwoCourses.getGoogleId());
        CourseAttributes.sortById(courseList);
        assertEquals(2, courseList.size());

        CourseAttributes course1 = dataBundle.courses.get("typicalCourse1");

        CourseAttributes course2 = dataBundle.courses.get("typicalCourse2");

        List<CourseAttributes> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        CourseAttributes.sortById(courses);

        assertEquals(courses.get(0).getId(), courseList.get(0).getId());
        assertEquals(courses.get(0).getName(), courseList.get(0).getName());

        assertEquals(courses.get(1).getId(), courseList.get(1).getId());
        assertEquals(courses.get(1).getName(), courseList.get(1).getName());

        ______TS("student having one course");

        StudentAttributes studentInOneCourse = dataBundle.students
                .get("student1InCourse1");
        courseList = coursesLogic.getCoursesForStudentAccount(studentInOneCourse.getGoogleId());
        assertEquals(1, courseList.size());
        course1 = dataBundle.courses.get("typicalCourse1");
        assertEquals(course1.getId(), courseList.get(0).getId());
        assertEquals(course1.getName(), courseList.get(0).getName());

        // Student having zero courses is not applicable

        ______TS("non-existent student");

        courseList = coursesLogic.getCoursesForStudentAccount("non-existent-student");
        assertEquals(0, courseList.size());

        ______TS("null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.getCoursesForStudentAccount(null));
    }

    private void testCreateCourse() throws Exception {

        ______TS("typical case");

        CourseAttributes c = CourseAttributes
                .builder("Computing101-fresh")
                .withName("Basic Computing")
                .withTimezone(ZoneId.of("Asia/Singapore"))
                .withInstitute("Test institute")
                .build();
        coursesLogic.createCourse(
                CourseAttributes.builder(c.getId())
                        .withName(c.getName())
                        .withTimezone(c.getTimeZone())
                        .withInstitute(c.getInstitute())
                        .build());
        verifyPresentInDatabase(c);
        coursesLogic.deleteCourseCascade(c.getId());

        ______TS("Null parameter");

        assertThrows(AssertionError.class,
                () -> coursesLogic.createCourse(null));
    }

    private void testCreateCourseAndInstructor() throws Exception {

        /* Explanation: SUT has 5 paths. They are,
         * path 1 - exit because the account doesn't' exist.
         * path 2 - exit because the account exists but doesn't have instructor privileges.
         * path 3 - exit because course creation failed.
         * path 4 - exit because instructor creation failed.
         * path 5 - success.
         * Accordingly, we have 5 test cases.
         */

        ______TS("fails: account doesn't exist");

        CourseAttributes c = CourseAttributes
                .builder("fresh-course-tccai")
                .withName("Fresh course for tccai")
                .withTimezone(ZoneId.of("America/Los_Angeles"))
                .withInstitute("Test institute")
                .build();

        InstructorAttributes i = InstructorAttributes
                .builder(c.getId(), "ins.for.iccai@gmail.tmt")
                .withGoogleId("instructor-for-tccai")
                .withName("Instructor for tccai")
                .build();

        AssertionError ae = assertThrows(AssertionError.class,
                () -> coursesLogic.createCourseAndInstructor(i.getGoogleId(),
                        CourseAttributes.builder(c.getId())
                                .withName(c.getName())
                                .withTimezone(c.getTimeZone())
                                .withInstitute(c.getInstitute())
                                .build()));
        AssertHelper.assertContains("for a non-existent instructor", ae.getMessage());
        verifyAbsentInDatabase(c);
        verifyAbsentInDatabase(i);

        ______TS("fails: account doesn't have instructor privileges");

        AccountAttributes a = AccountAttributes.builder(i.getGoogleId())
                .withName(i.getName())
                .withIsInstructor(false)
                .withEmail(i.getEmail())
                .withInstitute("TEAMMATES Test Institute 5")
                .build();

        accountsLogic.createAccount(a);
        ae = assertThrows(AssertionError.class,
                () -> coursesLogic.createCourseAndInstructor(i.getGoogleId(),
                        CourseAttributes.builder(c.getId())
                                .withName(c.getName())
                                .withTimezone(c.getTimeZone())
                                .withInstitute(c.getInstitute())
                                .build()));
        AssertHelper.assertContains("doesn't have instructor privileges", ae.getMessage());
        verifyAbsentInDatabase(c);
        verifyAbsentInDatabase(i);

        ______TS("fails: error during course creation");

        accountsLogic.makeAccountInstructor(a.getGoogleId());

        CourseAttributes invalidCourse = CourseAttributes
                .builder("invalid id")
                .withName("Fresh course for tccai")
                .withTimezone(ZoneId.of("UTC"))
                .withInstitute("Test institute")
                .build();

        String expectedError =
                "\"" + invalidCourse.getId() + "\" is not acceptable to TEAMMATES as a/an course ID because"
                + " it is not in the correct format. "
                + "A course ID can contain letters, numbers, fullstops, hyphens, underscores, and dollar signs. "
                + "It cannot be longer than 64 characters, cannot be empty and cannot contain spaces.";

        InvalidParametersException ipe = assertThrows(InvalidParametersException.class,
                () -> coursesLogic.createCourseAndInstructor(i.getGoogleId(),
                        CourseAttributes.builder(invalidCourse.getId())
                                .withName(invalidCourse.getName())
                                .withTimezone(invalidCourse.getTimeZone())
                                .withInstitute(invalidCourse.getInstitute())
                                .build()));
        assertEquals(expectedError, ipe.getMessage());
        verifyAbsentInDatabase(invalidCourse);
        verifyAbsentInDatabase(i);

        ______TS("fails: error during instructor creation due to duplicate instructor");

        CourseAttributes courseWithDuplicateInstructor = CourseAttributes
                .builder("fresh-course-tccai")
                .withName("Fresh course for tccai")
                .withTimezone(ZoneId.of("UTC"))
                .withInstitute("Test institute")
                .build();
        instructorsLogic.createInstructor(i); //create a duplicate instructor

        ae = assertThrows(AssertionError.class,
                () -> coursesLogic.createCourseAndInstructor(i.getGoogleId(),
                        CourseAttributes.builder(courseWithDuplicateInstructor.getId())
                                .withName(courseWithDuplicateInstructor.getName())
                                .withTimezone(courseWithDuplicateInstructor.getTimeZone())
                                .withInstitute(courseWithDuplicateInstructor.getInstitute())
                                .build()));
        AssertHelper.assertContains(
                "Unexpected exception while trying to create instructor for a new course",
                ae.getMessage());
        verifyAbsentInDatabase(courseWithDuplicateInstructor);

        ______TS("fails: error during instructor creation due to invalid parameters");

        i.setEmail("ins.for.iccai.gmail.tmt");

        ae = assertThrows(AssertionError.class,
                () -> coursesLogic.createCourseAndInstructor(i.getGoogleId(),
                        CourseAttributes.builder(courseWithDuplicateInstructor.getId())
                                .withName(courseWithDuplicateInstructor.getName())
                                .withTimezone(courseWithDuplicateInstructor.getTimeZone())
                                .withInstitute(courseWithDuplicateInstructor.getInstitute())
                                .build()));
        AssertHelper.assertContains(
                "Unexpected exception while trying to create instructor for a new course",
                ae.getMessage());
        verifyAbsentInDatabase(courseWithDuplicateInstructor);

        ______TS("success: typical case");

        i.setEmail("ins.for.iccai@gmail.tmt");

        //remove the duplicate instructor object from the database.
        instructorsLogic.deleteInstructorCascade(i.getCourseId(), i.getEmail());

        coursesLogic.createCourseAndInstructor(i.getGoogleId(),
                CourseAttributes.builder(courseWithDuplicateInstructor.getId())
                        .withName(courseWithDuplicateInstructor.getName())
                        .withTimezone(courseWithDuplicateInstructor.getTimeZone())
                        .withInstitute(courseWithDuplicateInstructor.getInstitute())
                        .build());
        verifyPresentInDatabase(courseWithDuplicateInstructor);
        verifyPresentInDatabase(i);

        ______TS("Null parameter");

        assertThrows(AssertionError.class,
                () -> coursesLogic.createCourseAndInstructor(null,
                        CourseAttributes.builder(courseWithDuplicateInstructor.getId())
                                .withName(courseWithDuplicateInstructor.getName())
                                .withTimezone(courseWithDuplicateInstructor.getTimeZone())
                                .withInstitute(courseWithDuplicateInstructor.getInstitute())
                                .build()));
    }

    private void testMoveCourseToRecycleBin() throws Exception {

        ______TS("typical case");

        CourseAttributes course1OfInstructor = dataBundle.courses.get("typicalCourse1");

        // Ensure there are entities in the database under this course
        verifyPresentInDatabase(course1OfInstructor);
        verifyPresentInDatabase(dataBundle.students.get("student1InCourse1"));
        verifyPresentInDatabase(dataBundle.students.get("student5InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session1InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session2InCourse1"));

        // Ensure the course is not in Recycle Bin
        assertFalse(course1OfInstructor.isCourseDeleted());

        Instant deletedAt = coursesLogic.moveCourseToRecycleBin(course1OfInstructor.getId());
        course1OfInstructor.setDeletedAt(deletedAt);

        // Ensure the course and related entities still exist in database
        verifyPresentInDatabase(course1OfInstructor);
        verifyPresentInDatabase(dataBundle.students.get("student1InCourse1"));
        verifyPresentInDatabase(dataBundle.students.get("student5InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session1InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session2InCourse1"));

        // Ensure the course is moved to Recycle Bin
        assertTrue(course1OfInstructor.isCourseDeleted());

        ______TS("null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.moveCourseToRecycleBin(null));
    }

    private void testRestoreCourseFromRecycleBin() throws Exception {

        ______TS("typical case");

        CourseAttributes course3OfInstructor = dataBundle.courses.get("typicalCourse3");

        // Ensure there are entities in the database under this course
        verifyPresentInDatabase(course3OfInstructor);
        verifyPresentInDatabase(dataBundle.students.get("student1InCourse3"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session1InCourse3"));

        // Ensure the course is currently in Recycle Bin
        assertTrue(course3OfInstructor.isCourseDeleted());

        coursesLogic.restoreCourseFromRecycleBin(course3OfInstructor.getId());
        course3OfInstructor.setDeletedAt(null);

        // Ensure the course and related entities still exist in database
        verifyPresentInDatabase(course3OfInstructor);
        verifyPresentInDatabase(dataBundle.students.get("student1InCourse3"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session1InCourse3"));

        // Ensure the course is restored from Recycle Bin
        assertFalse(course3OfInstructor.isCourseDeleted());

        // Move the course back to Recycle Bin for further testing
        coursesLogic.moveCourseToRecycleBin(course3OfInstructor.getId());

        ______TS("null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.restoreCourseFromRecycleBin(null));
    }

    @Test
    public void testDeleteCourseCascade() {

        ______TS("non-existent");

        coursesLogic.deleteCourseCascade("not_exist");

        ______TS("typical case");

        CourseAttributes course1OfInstructor = dataBundle.courses.get("typicalCourse1");
        StudentAttributes studentInCourse = dataBundle.students.get("student1InCourse1");

        // Ensure there are entities in the database under this course
        assertFalse(studentsLogic.getStudentsForCourse(course1OfInstructor.getId()).isEmpty());

        verifyPresentInDatabase(course1OfInstructor);
        verifyPresentInDatabase(studentInCourse);
        verifyPresentInDatabase(dataBundle.instructors.get("instructor1OfCourse1"));
        verifyPresentInDatabase(dataBundle.instructors.get("instructor3OfCourse1"));
        verifyPresentInDatabase(dataBundle.students.get("student1InCourse1"));
        verifyPresentInDatabase(dataBundle.students.get("student5InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session1InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackSessions.get("session2InCourse1"));
        verifyPresentInDatabase(dataBundle.feedbackQuestions.get("qn1InSession1InCourse1"));
        FeedbackResponseAttributes typicalResponse = dataBundle.feedbackResponses.get("response1ForQ1S1C1");
        FeedbackQuestionAttributes typicalQuestion =
                fqLogic.getFeedbackQuestion(typicalResponse.getFeedbackSessionName(), typicalResponse.getCourseId(),
                        Integer.parseInt(typicalResponse.getFeedbackQuestionId()));
        typicalResponse = frLogic
                .getFeedbackResponse(typicalQuestion.getId(), typicalResponse.getGiver(), typicalResponse.getRecipient());
        verifyPresentInDatabase(typicalResponse);
        FeedbackResponseCommentAttributes typicalComment =
                dataBundle.feedbackResponseComments.get("comment1FromT1C1ToR1Q1S1C1");
        typicalComment = frcLogic
                .getFeedbackResponseComment(typicalResponse.getId(),
                        typicalComment.getCommentGiver(), typicalComment.getCreatedAt());
        verifyPresentInDatabase(typicalComment);

        coursesLogic.deleteCourseCascade(course1OfInstructor.getId());

        // Ensure the course and related entities are deleted
        verifyAbsentInDatabase(course1OfInstructor);
        verifyAbsentInDatabase(studentInCourse);
        verifyAbsentInDatabase(dataBundle.instructors.get("instructor1OfCourse1"));
        verifyAbsentInDatabase(dataBundle.instructors.get("instructor3OfCourse1"));
        verifyAbsentInDatabase(dataBundle.students.get("student1InCourse1"));
        verifyAbsentInDatabase(dataBundle.students.get("student5InCourse1"));
        verifyAbsentInDatabase(dataBundle.feedbackSessions.get("session1InCourse1"));
        verifyAbsentInDatabase(dataBundle.feedbackSessions.get("session2InCourse1"));
        verifyAbsentInDatabase(dataBundle.feedbackQuestions.get("qn1InSession1InCourse1"));
        verifyAbsentInDatabase(typicalQuestion);
        verifyAbsentInDatabase(typicalResponse);
        verifyAbsentInDatabase(typicalComment);

        ______TS("null parameter");

        assertThrows(AssertionError.class, () -> coursesLogic.deleteCourseCascade(null));
    }

    private void testUpdateCourseCascade() throws Exception {
        CourseAttributes c = CourseAttributes
                .builder("Computing101-getthis")
                .withName("Basic Computing Getting")
                .withTimezone(ZoneId.of("UTC"))
                .withInstitute("Test institute")
                .build();
        coursesDb.createEntity(c);

        ______TS("Typical case");
        String newName = "New Course Name";
        String validTimeZone = "Asia/Singapore";
        CourseAttributes updateCourse = coursesLogic.updateCourseCascade(
                CourseAttributes.updateOptionsBuilder(c.getId())
                        .withName(newName)
                        .withTimezone(ZoneId.of(validTimeZone))
                        .build()
        );
        c.setName(newName);
        c.setTimeZone(ZoneId.of(validTimeZone));
        verifyPresentInDatabase(c);
        assertEquals(newName, updateCourse.getName());
        assertEquals(validTimeZone, updateCourse.getTimeZone().getId());

        ______TS("Invalid name (empty course name)");

        String emptyName = "";
        InvalidParametersException ipe = assertThrows(InvalidParametersException.class,
                () -> coursesLogic.updateCourseCascade(
                        CourseAttributes.updateOptionsBuilder(c.getId())
                                .withName(emptyName)
                                .build()
                ));
        String expectedErrorMessage =
                getPopulatedEmptyStringErrorMessage(
                        FieldValidator.SIZE_CAPPED_NON_EMPTY_STRING_ERROR_MESSAGE_EMPTY_STRING,
                        FieldValidator.COURSE_NAME_FIELD_NAME, FieldValidator.COURSE_NAME_MAX_LENGTH);
        assertEquals(expectedErrorMessage, ipe.getMessage());
        verifyPresentInDatabase(c);
    }

}
