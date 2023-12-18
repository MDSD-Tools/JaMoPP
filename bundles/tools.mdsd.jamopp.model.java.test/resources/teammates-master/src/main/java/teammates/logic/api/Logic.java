package teammates.logic.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import teammates.common.datatransfer.DataBundle;
import teammates.common.datatransfer.SessionResultsBundle;
import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseCommentAttributes;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.datatransfer.attributes.StudentProfileAttributes;
import teammates.common.exception.EnrollException;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InstructorUpdateException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.exception.SearchServiceException;
import teammates.logic.core.AccountsLogic;
import teammates.logic.core.CoursesLogic;
import teammates.logic.core.DataBundleLogic;
import teammates.logic.core.FeedbackQuestionsLogic;
import teammates.logic.core.FeedbackResponseCommentsLogic;
import teammates.logic.core.FeedbackResponsesLogic;
import teammates.logic.core.FeedbackSessionsLogic;
import teammates.logic.core.InstructorsLogic;
import teammates.logic.core.ProfilesLogic;
import teammates.logic.core.StudentsLogic;

/**
 * Provides the business logic for production usage of the system.
 *
 * <p>This is a Facade class which simply forwards the method to internal classes.
 */
public class Logic {

    private static final Logic instance = new Logic();

    final AccountsLogic accountsLogic = AccountsLogic.inst();
    final StudentsLogic studentsLogic = StudentsLogic.inst();
    final InstructorsLogic instructorsLogic = InstructorsLogic.inst();
    final CoursesLogic coursesLogic = CoursesLogic.inst();
    final FeedbackSessionsLogic feedbackSessionsLogic = FeedbackSessionsLogic.inst();
    final FeedbackQuestionsLogic feedbackQuestionsLogic = FeedbackQuestionsLogic.inst();
    final FeedbackResponsesLogic feedbackResponsesLogic = FeedbackResponsesLogic.inst();
    final FeedbackResponseCommentsLogic feedbackResponseCommentsLogic = FeedbackResponseCommentsLogic.inst();
    final ProfilesLogic profilesLogic = ProfilesLogic.inst();
    final DataBundleLogic dataBundleLogic = DataBundleLogic.inst();

    Logic() {
        // prevent initialization
    }

    public static Logic inst() {
        return instance;
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     */
    public AccountAttributes getAccount(String googleId) {
        assert googleId != null;

        return accountsLogic.getAccount(googleId);
    }

    public String getCourseInstitute(String courseId) {
        return coursesLogic.getCourseInstitute(courseId);
    }

    /**
     * Updates/Creates the profile using {@link StudentProfileAttributes.UpdateOptions}.
     *
     * <br/> Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated student profile
     * @throws InvalidParametersException if attributes to update are not valid
     */
    public StudentProfileAttributes updateOrCreateStudentProfile(StudentProfileAttributes.UpdateOptions updateOptions)
            throws InvalidParametersException {

        assert updateOptions != null;

        return profilesLogic.updateOrCreateStudentProfile(updateOptions);
    }

    /**
     * Deletes both instructor and student privileges, as long as the account and associated student profile.
     *
     * <ul>
     * <li>Fails silently if no such account.</li>
     * </ul>
     *
     * <p>Preconditions:</p>
     * * All parameters are non-null.
     */
    public void deleteAccountCascade(String googleId) {

        assert googleId != null;

        accountsLogic.deleteAccountCascade(googleId);
    }

    /**
     * Creates an instructor.
     *
     * <p>Preconditions:</p>
     * * All parameters are non-null.
     *
     * @return the created instructor
     * @throws InvalidParametersException if the instructor is not valid
     * @throws EntityAlreadyExistsException if the instructor already exists in the database
     */
    public InstructorAttributes createInstructor(InstructorAttributes instructor)
            throws InvalidParametersException, EntityAlreadyExistsException {
        assert instructor != null;

        return instructorsLogic.createInstructor(instructor);
    }

    /**
     * This method should be used by admin only since the searching does not restrict the
     * visibility according to the logged-in user's google ID. This is used by admin to
     * search instructors in the whole system.
     * @return Null if no match found.
     */
    public List<InstructorAttributes> searchInstructorsInWholeSystem(String queryString)
            throws SearchServiceException {
        assert queryString != null;

        return instructorsLogic.searchInstructorsInWholeSystem(queryString);
    }

    /**
     * Creates or updates search document for the given instructor.
     *
     * @see InstructorsLogic#putDocument(InstructorAttributes)
     */
    public void putInstructorDocument(InstructorAttributes instructor) throws SearchServiceException {
        instructorsLogic.putDocument(instructor);
    }

    /**
     * Update instructor being edited to ensure validity of instructors for the course.
     *
     * @see InstructorsLogic#updateToEnsureValidityOfInstructorsForTheCourse(String, InstructorAttributes)
     */
    public void updateToEnsureValidityOfInstructorsForTheCourse(String courseId, InstructorAttributes instructorToEdit) {

        assert courseId != null;
        assert instructorToEdit != null;

        instructorsLogic.updateToEnsureValidityOfInstructorsForTheCourse(courseId, instructorToEdit);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return null if not found.
     */
    public InstructorAttributes getInstructorForEmail(String courseId, String email) {

        assert courseId != null;
        assert email != null;

        return instructorsLogic.getInstructorForEmail(courseId, email);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return null if not found.
     */
    public InstructorAttributes getInstructorById(String courseId, String email) {

        assert courseId != null;
        assert email != null;

        return instructorsLogic.getInstructorById(courseId, email);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return null if not found.
     */
    public InstructorAttributes getInstructorForGoogleId(String courseId, String googleId) {

        assert googleId != null;
        assert courseId != null;

        return instructorsLogic.getInstructorForGoogleId(courseId, googleId);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return null if not found.
     */
    public InstructorAttributes getInstructorForRegistrationKey(String registrationKey) {

        assert registrationKey != null;

        return instructorsLogic.getInstructorForRegistrationKey(registrationKey);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return Empty list if none found.
     */
    public List<InstructorAttributes> getInstructorsForGoogleId(String googleId) {

        assert googleId != null;

        return instructorsLogic.getInstructorsForGoogleId(googleId);
    }

    public List<InstructorAttributes> getInstructorsForGoogleId(String googleId, boolean omitArchived) {

        assert googleId != null;

        return instructorsLogic.getInstructorsForGoogleId(googleId, omitArchived);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return Empty list if none found.
     */
    public List<InstructorAttributes> getInstructorsForCourse(String courseId) {

        assert courseId != null;

        return instructorsLogic.getInstructorsForCourse(courseId);
    }

    public List<FeedbackSessionAttributes> getAllOngoingSessions(Instant rangeStart, Instant rangeEnd) {

        return feedbackSessionsLogic.getAllOngoingSessions(rangeStart, rangeEnd);
    }

    /**
     * Updates an instructor by {@link InstructorAttributes.UpdateOptionsWithGoogleId}.
     *
     * <p>Cascade update the comments and responses given by the instructor.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated instructor
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the instructor cannot be found
     */
    public InstructorAttributes updateInstructorCascade(InstructorAttributes.UpdateOptionsWithGoogleId updateOptions)
            throws InstructorUpdateException, InvalidParametersException, EntityDoesNotExistException {
        assert updateOptions != null;

        return instructorsLogic.updateInstructorByGoogleIdCascade(updateOptions);
    }

    /**
     * Updates an instructor by {@link InstructorAttributes.UpdateOptionsWithEmail}.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated instructor
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the instructor cannot be found
     */
    public InstructorAttributes updateInstructor(InstructorAttributes.UpdateOptionsWithEmail updateOptions)
            throws InstructorUpdateException, InvalidParametersException, EntityDoesNotExistException {
        assert updateOptions != null;

        return instructorsLogic.updateInstructorByEmail(updateOptions);
    }

    /**
     * Make the instructor join the course, i.e. associate the Google ID to the instructor.<br>
     * Creates an account for the instructor if there is no existing account for him.
     * Preconditions: <br>
     * * Parameters regkey and googleId are non-null.
     */
    public InstructorAttributes joinCourseForInstructor(String regkey, String googleId)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {

        assert googleId != null;
        assert regkey != null;

        return accountsLogic.joinCourseForInstructor(regkey, googleId);
    }

    /**
     * Downgrades an instructor account to student account.
     *
     * <p>Cascade deletes all instructors associated with the account.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void downgradeInstructorToStudentCascade(String googleId) throws EntityDoesNotExistException {
        assert googleId != null;

        accountsLogic.downgradeInstructorToStudentCascade(googleId);
    }

    /**
     * Deletes an instructor cascade its associated feedback responses and comments.
     *
     * <p>Fails silently if the student does not exist.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void deleteInstructorCascade(String courseId, String email) {

        assert courseId != null;
        assert email != null;

        instructorsLogic.deleteInstructorCascade(courseId, email);
    }

    /**
     * Creates a course and an associated instructor for the course.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null. <br/>
     * * {@code instructorGoogleId} already has an account and instructor privileges.
     */
    public void createCourseAndInstructor(String instructorGoogleId, CourseAttributes courseAttributes)
            throws EntityAlreadyExistsException, InvalidParametersException {
        assert instructorGoogleId != null;
        assert courseAttributes != null;

        coursesLogic.createCourseAndInstructor(instructorGoogleId, courseAttributes);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return null if not found.
     */
    public CourseAttributes getCourse(String courseId) {

        assert courseId != null;

        return coursesLogic.getCourse(courseId);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     */
    public List<CourseAttributes> getCoursesForStudentAccount(String googleId) {
        assert googleId != null;
        return coursesLogic.getCoursesForStudentAccount(googleId);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     *
     * @return Courses the given instructors is in except for courses in Recycle Bin.
     */
    public List<CourseAttributes> getCoursesForInstructor(List<InstructorAttributes> instructorList) {

        assert instructorList != null;
        return coursesLogic.getCoursesForInstructor(instructorList);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     *
     * @return Courses in Recycle Bin that the given instructors is in.
     */
    public List<CourseAttributes> getSoftDeletedCoursesForInstructors(List<InstructorAttributes> instructorList) {

        assert instructorList != null;
        return coursesLogic.getSoftDeletedCoursesForInstructors(instructorList);
    }

    /**
     * Updates a course by {@link CourseAttributes.UpdateOptions}.
     *
     * <p>If the {@code timezone} of the course is changed, cascade the change to its corresponding feedback sessions.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated course
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the course cannot be found
     */
    public CourseAttributes updateCourseCascade(CourseAttributes.UpdateOptions updateOptions)
            throws InvalidParametersException, EntityDoesNotExistException {
        assert updateOptions != null;

        return coursesLogic.updateCourseCascade(updateOptions);
    }

    /**
     * Changes the archive status of a course for an instructor.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @param courseId The course of which the archive status is to be changed
     * @param archiveStatus The archive status to be set
     */

    public void setArchiveStatusOfInstructor(String googleId, String courseId, boolean archiveStatus)
            throws InvalidParametersException, EntityDoesNotExistException {

        assert googleId != null;
        assert courseId != null;

        instructorsLogic.setArchiveStatusOfInstructor(googleId, courseId, archiveStatus);
    }

    /**
     * Deletes a course cascade its students, instructors, sessions, responses and comments.
     *
     * <p>Fails silently if no such course.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void deleteCourseCascade(String courseId) {
        assert courseId != null;
        coursesLogic.deleteCourseCascade(courseId);
    }

    /**
     * Moves a course to Recycle Bin by its given corresponding ID.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return the deletion timestamp assigned to the course.
     */
    public Instant moveCourseToRecycleBin(String courseId) throws EntityDoesNotExistException {
        assert courseId != null;
        return coursesLogic.moveCourseToRecycleBin(courseId);
    }

    /**
     * Restores a course and all data related to the course from Recycle Bin by
     * its given corresponding ID.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void restoreCourseFromRecycleBin(String courseId)
            throws EntityDoesNotExistException {
        assert courseId != null;

        coursesLogic.restoreCourseFromRecycleBin(courseId);
    }

    /**
     * Search for students. Preconditions: all parameters are non-null.
     * @param instructors   a list of InstructorAttributes associated to a googleId,
     *                      used for filtering of search result
     * @return Null if no match found
     */
    public List<StudentAttributes> searchStudents(String queryString, List<InstructorAttributes> instructors)
            throws SearchServiceException {
        assert queryString != null;
        assert instructors != null;
        return studentsLogic.searchStudents(queryString, instructors);
    }

    /**
     * This method should be used by admin only since the searching does not restrict the
     * visibility according to the logged-in user's google ID. This is used by admin to
     * search students in the whole system.
     * @return Null if no match found.
     */
    public List<StudentAttributes> searchStudentsInWholeSystem(String queryString)
            throws SearchServiceException {
        assert queryString != null;

        return studentsLogic.searchStudentsInWholeSystem(queryString);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     *
     * @return Null if no match found.
     */
    public StudentAttributes getStudentForRegistrationKey(String registrationKey) {
        assert registrationKey != null;
        return studentsLogic.getStudentForRegistrationKey(registrationKey);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     *
     * @return Null if no match found.
     */
    public StudentAttributes getStudentForEmail(String courseId, String email) {
        assert courseId != null;
        assert email != null;

        return studentsLogic.getStudentForEmail(courseId, email);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     *
     * @return Null if no match found.
     */
    public StudentAttributes getStudentForGoogleId(String courseId, String googleId) {
        assert courseId != null;
        assert googleId != null;

        return studentsLogic.getStudentForCourseIdAndGoogleId(courseId, googleId);
    }

    /**
     * Gets student profile associated with the {@code googleId}.
     *
     * <br/> Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return null if no match found.
     */
    public StudentProfileAttributes getStudentProfile(String googleId) {
        assert googleId != null;
        return profilesLogic.getStudentProfile(googleId);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     *
     * @return Empty list if no match found.
     */
    public List<StudentAttributes> getStudentsForGoogleId(String googleId) {
        assert googleId != null;
        return studentsLogic.getStudentsForGoogleId(googleId);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     * @return Empty list if none found.
     */
    public List<StudentAttributes> getStudentsForCourse(String courseId) {
        assert courseId != null;
        return studentsLogic.getStudentsForCourse(courseId);
    }

    /**
     * Returns a list of section names for the course with ID courseId.
     *
     * <p>Preconditions: <br>
     * * All parameters are non-null.
     *
     * @see CoursesLogic#getSectionsNameForCourse(String)
     */
    public List<String> getSectionNamesForCourse(String courseId) throws EntityDoesNotExistException {
        assert courseId != null;
        return coursesLogic.getSectionsNameForCourse(courseId);
    }

    /**
     * Populates fields that need dynamic generation in a question.
     *
     * <p>Currently, only MCQ/MSQ needs to generate choices dynamically.</p>
     *
     * <br/> Preconditions: <br/>
     * * All parameters except <code>teamOfEntityDoingQuestion</code> are non-null.
     *
     * @param feedbackQuestionAttributes the question to populate
     * @param emailOfEntityDoingQuestion the email of the entity doing the question
     * @param teamOfEntityDoingQuestion the team of the entity doing the question. If the entity is an instructor,
     *                                  it can be {@code null}.
     */
    public void populateFieldsToGenerateInQuestion(FeedbackQuestionAttributes feedbackQuestionAttributes,
            String emailOfEntityDoingQuestion, String teamOfEntityDoingQuestion) {
        assert feedbackQuestionAttributes != null;
        assert emailOfEntityDoingQuestion != null;

        feedbackQuestionsLogic.populateFieldsToGenerateInQuestion(
                feedbackQuestionAttributes, emailOfEntityDoingQuestion, teamOfEntityDoingQuestion);
    }

    /**
     * Resets the googleId associated with the student.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void resetStudentGoogleId(String originalEmail, String courseId) throws EntityDoesNotExistException {
        assert originalEmail != null;
        assert courseId != null;

        studentsLogic.resetStudentGoogleId(originalEmail, courseId);
    }

    /**
     * Regenerates the registration key for the instructor with email address {@code email} in course {@code courseId}.
     *
     * @return the instructor attributes with the new registration key.
     * @throws EntityAlreadyExistsException if the newly generated instructor has the same registration key as the
     *          original one.
     * @throws EntityDoesNotExistException if the instructor does not exist.
     */
    public InstructorAttributes regenerateInstructorRegistrationKey(String courseId, String email)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {

        assert courseId != null;
        assert email != null;

        return instructorsLogic.regenerateInstructorRegistrationKey(courseId, email);
    }

    /**
     * Regenerates the registration key for the student with email address {@code email} in course {@code courseId}.
     *
     * @return the student attributes with the new registration key.
     * @throws EntityAlreadyExistsException if the newly generated course student has the same registration key as the
     *          original one.
     * @throws EntityDoesNotExistException if the student does not exist.
     */
    public StudentAttributes regenerateStudentRegistrationKey(String courseId, String email)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {

        assert courseId != null;
        assert email != null;

        return studentsLogic.regenerateStudentRegistrationKey(courseId, email);
    }

    /**
     * Resets the associated googleId of an instructor.
     */
    public void resetInstructorGoogleId(String originalEmail, String courseId) throws EntityDoesNotExistException {
        assert originalEmail != null;
        assert courseId != null;

        instructorsLogic.resetInstructorGoogleId(originalEmail, courseId);
    }

    /**
     * Creates a student.
     *
     * @return the created student.
     * @throws InvalidParametersException if the student is not valid.
     * @throws EntityAlreadyExistsException if the student already exists in the database.
     */
    public StudentAttributes createStudent(StudentAttributes student)
            throws InvalidParametersException, EntityAlreadyExistsException {
        assert student.getCourse() != null;
        assert student.getEmail() != null;

        return studentsLogic.createStudent(student);
    }

    /**
     * Updates a student by {@link StudentAttributes.UpdateOptions}.
     *
     * <p>If email changed, update by recreating the student and cascade update all responses the student gives/receives.
     *
     * <p>If team changed, cascade delete all responses the student gives/receives within that team.
     *
     * <p>If section changed, cascade update all responses the student gives/receives.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated student
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the student cannot be found
     * @throws EntityAlreadyExistsException if the student cannot be updated
     *         by recreation because of an existent student
     */
    public StudentAttributes updateStudentCascade(StudentAttributes.UpdateOptions updateOptions)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {

        assert updateOptions != null;

        return studentsLogic.updateStudentCascade(updateOptions);
    }

    /**
     * Make the student join the course, i.e. associate the Google ID to the student.<br>
     * Create an account for the student if there is no account exist for him.
     * Preconditions: <br>
     * * All parameters are non-null.
     * @param key the registration key
     */
    public StudentAttributes joinCourseForStudent(String key, String googleId)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {

        assert googleId != null;
        assert key != null;

        return accountsLogic.joinCourseForStudent(key, googleId);

    }

    public List<StudentAttributes> getUnregisteredStudentsForCourse(String courseId) {
        assert courseId != null;
        return studentsLogic.getUnregisteredStudentsForCourse(courseId);
    }

    /**
     * Checks whether an instructor has completed a feedback session.
     *
     * <p> If there is no question for instructors, the feedback session is completed</p>
     */
    public boolean isFeedbackSessionCompletedByInstructor(FeedbackSessionAttributes fsa, String userEmail)
            throws EntityDoesNotExistException {
        assert fsa != null;
        assert userEmail != null;
        return feedbackSessionsLogic.isFeedbackSessionCompletedByInstructor(fsa, userEmail);
    }

    /**
     * Checks whether a student has completed a feedback session.
     *
     * <p> If there is no question for students, the feedback session is completed</p>
     */
    public boolean isFeedbackSessionCompletedByStudent(FeedbackSessionAttributes fsa, String userEmail) {
        assert fsa != null;
        assert userEmail != null;
        return feedbackSessionsLogic.isFeedbackSessionCompletedByStudent(fsa, userEmail);
    }

    /**
     * Deletes a student cascade its associated feedback responses and comments.
     *
     * <p>Fails silently if the student does not exist.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void deleteStudentCascade(String courseId, String studentEmail) {
        assert courseId != null;
        assert studentEmail != null;

        studentsLogic.deleteStudentCascade(courseId, studentEmail);
    }

    /**
     * Deletes all the students in the course cascade their associated responses and comments.
     *
     * <br/>Preconditions: <br>
     * * All parameters are non-null.
     */
    public void deleteStudentsInCourseCascade(String courseId) {
        assert courseId != null;

        studentsLogic.deleteStudentsInCourseCascade(courseId);
    }

    /**
     * Validates sections for any limit violations and teams for any team name violations.
     *
     * <p>Preconditions: <br>
     * * All parameters are non-null.
     *
     * @see StudentsLogic#validateSectionsAndTeams(List, String)
     */
    public void validateSectionsAndTeams(List<StudentAttributes> studentList, String courseId) throws EnrollException {

        assert studentList != null;
        assert courseId != null;

        studentsLogic.validateSectionsAndTeams(studentList, courseId);
    }

    /**
     * Gets all students of a team.
     */
    public List<StudentAttributes> getStudentsForTeam(String teamName, String courseId) {
        assert teamName != null;
        assert courseId != null;

        return studentsLogic.getStudentsForTeam(teamName, courseId);
    }

    /**
     * Creates or updates search document for the given student.
     *
     * @see StudentsLogic#putDocument(StudentAttributes)
     */
    public void putStudentDocument(StudentAttributes student) throws SearchServiceException {
        studentsLogic.putDocument(student);
    }

    /**
     * Creates a feedback session.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return created feedback session
     * @throws InvalidParametersException if the session is not valid
     * @throws EntityAlreadyExistsException if the session already exist
     */
    public FeedbackSessionAttributes createFeedbackSession(FeedbackSessionAttributes feedbackSession)
            throws EntityAlreadyExistsException, InvalidParametersException {
        assert feedbackSession != null;

        return feedbackSessionsLogic.createFeedbackSession(feedbackSession);
    }

    /**
     * Gets a feedback session from the data storage.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return null if not found or in recycle bin.
     */
    public FeedbackSessionAttributes getFeedbackSession(String feedbackSessionName, String courseId) {

        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackSessionsLogic.getFeedbackSession(feedbackSessionName, courseId);
    }

    /**
     * Gets a feedback session from the recycle bin.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return null if not found.
     */
    public FeedbackSessionAttributes getFeedbackSessionFromRecycleBin(String feedbackSessionName, String courseId) {
        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackSessionsLogic.getFeedbackSessionFromRecycleBin(feedbackSessionName, courseId);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     */
    public List<FeedbackSessionAttributes> getFeedbackSessionsForCourse(String courseId) {
        assert courseId != null;
        return feedbackSessionsLogic.getFeedbackSessionsForCourse(courseId);
    }

    /**
     * Gets the expected number of submissions for a feedback session.
     *
     * <br>Preconditions: <br>
     * * All parameters are non-null.
     */
    public int getExpectedTotalSubmission(FeedbackSessionAttributes fsa) {
        assert fsa != null;
        return feedbackSessionsLogic.getExpectedTotalSubmission(fsa);
    }

    /**
     * Gets the actual number of submissions for a feedback session.
     *
     * <br>Preconditions: <br>
     * * All parameters are non-null.
     */
    public int getActualTotalSubmission(FeedbackSessionAttributes fsa) {
        assert fsa != null;
        return feedbackSessionsLogic.getActualTotalSubmission(fsa);
    }

    /**
     * Gets a list of feedback sessions for instructors.
     */
    public List<FeedbackSessionAttributes> getFeedbackSessionsListForInstructor(
            List<InstructorAttributes> instructorList) {
        assert instructorList != null;
        return feedbackSessionsLogic.getFeedbackSessionsListForInstructor(instructorList);
    }

    /**
     * Returns a {@code List} of feedback sessions in the Recycle Bin for the instructors.
     * <br>
     * Omits sessions if the corresponding courses are archived or in Recycle Bin
     */
    public List<FeedbackSessionAttributes> getSoftDeletedFeedbackSessionsListForInstructors(
            List<InstructorAttributes> instructorList) {
        assert instructorList != null;
        return feedbackSessionsLogic.getSoftDeletedFeedbackSessionsListForInstructors(instructorList);
    }

    /**
     * Gets the recipients of a feedback question for student.
     *
     * @see FeedbackQuestionsLogic#getRecipientsOfQuestion
     */
    public Map<String, String> getRecipientsOfQuestion(
            FeedbackQuestionAttributes question,
            @Nullable InstructorAttributes instructorGiver, @Nullable StudentAttributes studentGiver) {
        assert question != null;

        // we do not supply course roster here
        return feedbackQuestionsLogic.getRecipientsOfQuestion(question, instructorGiver, studentGiver, null);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null. <br>
     *
     */
    public FeedbackQuestionAttributes getFeedbackQuestion(String feedbackQuestionId) {
        assert feedbackQuestionId != null;
        return feedbackQuestionsLogic.getFeedbackQuestion(feedbackQuestionId);
    }

    /**
     * Gets a list of all questions for the given session that
     * students can view/submit.
     */
    public List<FeedbackQuestionAttributes> getFeedbackQuestionsForStudents(
            String feedbackSessionName, String courseId) {
        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackQuestionsLogic.getFeedbackQuestionsForStudents(feedbackSessionName, courseId);
    }

    /**
     * Gets a {@code List} of all questions for the given session that
     * instructor can view/submit.
     */
    public List<FeedbackQuestionAttributes> getFeedbackQuestionsForInstructors(
            String feedbackSessionName, String courseId, String instructorEmail) throws EntityDoesNotExistException {
        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackQuestionsLogic.getFeedbackQuestionsForInstructor(feedbackSessionName, courseId, instructorEmail);
    }

    /**
     * Preconditions: <br>
     * * All parameters are non-null.
     */
    public boolean hasStudentSubmittedFeedback(FeedbackSessionAttributes fsa, String studentEmail) {

        assert fsa != null;
        assert studentEmail != null;

        return feedbackSessionsLogic.isFeedbackSessionCompletedByStudent(fsa, studentEmail);
    }

    /**
     * Updates the details of a feedback session by {@link FeedbackSessionAttributes.UpdateOptions}.
     *
     * <p>Adjust email sending status if necessary.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated feedback session
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the feedback session cannot be found
     */
    public FeedbackSessionAttributes updateFeedbackSession(FeedbackSessionAttributes.UpdateOptions updateOptions)
            throws InvalidParametersException, EntityDoesNotExistException {
        assert updateOptions != null;

        return feedbackSessionsLogic.updateFeedbackSession(updateOptions);
    }

    /**
     * Publishes a feedback session.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return the published feedback session
     * @throws EntityDoesNotExistException if the feedback session cannot be found
     * @throws InvalidParametersException if session is already published
     */
    public FeedbackSessionAttributes publishFeedbackSession(String feedbackSessionName, String courseId)
            throws EntityDoesNotExistException, InvalidParametersException {

        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackSessionsLogic.publishFeedbackSession(feedbackSessionName, courseId);
    }

    /**
     * Unpublishes a feedback session.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return the unpublished feedback session
     * @throws EntityDoesNotExistException if the feedback session cannot be found
     * @throws InvalidParametersException
     *             if the feedback session is not ready to be unpublished.
     */
    public FeedbackSessionAttributes unpublishFeedbackSession(String feedbackSessionName, String courseId)
            throws EntityDoesNotExistException, InvalidParametersException {

        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackSessionsLogic.unpublishFeedbackSession(feedbackSessionName, courseId);
    }

    /**
     * Deletes a feedback session cascade to its associated questions, responses and comments.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void deleteFeedbackSessionCascade(String feedbackSessionName, String courseId) {

        assert feedbackSessionName != null;
        assert courseId != null;

        feedbackSessionsLogic.deleteFeedbackSessionCascade(feedbackSessionName, courseId);
    }

    /**
     * Soft-deletes a specific session to Recycle Bin.
     */
    public void moveFeedbackSessionToRecycleBin(String feedbackSessionName, String courseId)
            throws EntityDoesNotExistException {

        assert feedbackSessionName != null;
        assert courseId != null;

        feedbackSessionsLogic.moveFeedbackSessionToRecycleBin(feedbackSessionName, courseId);
    }

    /**
     * Restores a specific session from Recycle Bin to feedback sessions table.
     */
    public void restoreFeedbackSessionFromRecycleBin(String feedbackSessionName, String courseId)
            throws EntityDoesNotExistException {

        assert feedbackSessionName != null;
        assert courseId != null;

        feedbackSessionsLogic.restoreFeedbackSessionFromRecycleBin(feedbackSessionName, courseId);
    }

    /**
     * Creates a new feedback question.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return the created question
     * @throws InvalidParametersException if the question is invalid
     */
    public FeedbackQuestionAttributes createFeedbackQuestion(FeedbackQuestionAttributes feedbackQuestion)
            throws InvalidParametersException {
        assert feedbackQuestion != null;

        return feedbackQuestionsLogic.createFeedbackQuestion(feedbackQuestion);
    }

    /**
     * Updates a feedback question by {@code FeedbackQuestionAttributes.UpdateOptions}.
     *
     * <p>Cascade adjust the question number of questions in the same session.
     *
     * <p>Cascade adjust the existing response of the question.
     *
     * <br/> Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated feedback question
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the feedback question cannot be found
     */
    public FeedbackQuestionAttributes updateFeedbackQuestionCascade(FeedbackQuestionAttributes.UpdateOptions updateOptions)
            throws InvalidParametersException, EntityDoesNotExistException {
        assert updateOptions != null;

        return feedbackQuestionsLogic.updateFeedbackQuestionCascade(updateOptions);
    }

    /**
     * Deletes a feedback question cascade its responses and comments.
     *
     * <p>Silently fail if question does not exist.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void deleteFeedbackQuestionCascade(String questionId) {
        assert questionId != null;
        feedbackQuestionsLogic.deleteFeedbackQuestionCascade(questionId);
    }

    /**
     * Checks whether there are responses for a question.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public boolean areThereResponsesForQuestion(String feedbackQuestionId) {
        return feedbackResponsesLogic.areThereResponsesForQuestion(feedbackQuestionId);
    }

    /**
     * Gets all questions for a feedback session.<br>
     * Returns an empty list if they are no questions
     * for the session.
     * Preconditions: <br>
     * * All parameters are non-null.
     */
    public List<FeedbackQuestionAttributes> getFeedbackQuestionsForSession(String feedbackSessionName, String courseId) {
        assert feedbackSessionName != null;
        assert courseId != null;

        return feedbackQuestionsLogic.getFeedbackQuestionsForSession(feedbackSessionName, courseId);
    }

    /**
     * Gets a set of giver identifiers that has at least one response under a feedback session.
     */
    public Set<String> getGiverSetThatAnswerFeedbackSession(String courseId, String feedbackSessionName) {
        assert courseId != null;
        assert feedbackSessionName != null;

        return feedbackResponsesLogic.getGiverSetThatAnswerFeedbackSession(courseId, feedbackSessionName);
    }

    /**
     * Gets the session result for a feedback session.
     *
     * @see FeedbackResponsesLogic#getSessionResultsForCourse(String, String, String, String, String)
     */
    public SessionResultsBundle getSessionResultsForCourse(
            String feedbackSessionName, String courseId, String userEmail,
            @Nullable String questionId, @Nullable String section) {
        assert feedbackSessionName != null;
        assert courseId != null;
        assert userEmail != null;

        return feedbackResponsesLogic.getSessionResultsForCourse(
                feedbackSessionName, courseId, userEmail, questionId, section);
    }

    /**
     * Gets the session result for a feedback session for the given user.
     *
     * @see FeedbackResponsesLogic#getSessionResultsForUser(String, String, String, boolean, String)
     */
    public SessionResultsBundle getSessionResultsForUser(
            String feedbackSessionName, String courseId, String userEmail, boolean isInstructor,
            @Nullable String questionId) {
        assert feedbackSessionName != null;
        assert courseId != null;
        assert userEmail != null;

        return feedbackResponsesLogic.getSessionResultsForUser(
                feedbackSessionName, courseId, userEmail, isInstructor, questionId);
    }

    /**
     * Get existing feedback responses from student or his team for the given question.
     */
    public List<FeedbackResponseAttributes> getFeedbackResponsesFromStudentOrTeamForQuestion(
            FeedbackQuestionAttributes question, StudentAttributes student) {
        assert question != null;
        assert student != null;

        return feedbackResponsesLogic.getFeedbackResponsesFromStudentOrTeamForQuestion(question, student);
    }

    /**
     * Get existing feedback responses from instructor for the given question.
     */
    public List<FeedbackResponseAttributes> getFeedbackResponsesFromInstructorForQuestion(
            FeedbackQuestionAttributes question, InstructorAttributes instructorAttributes) {
        assert question != null;
        assert instructorAttributes != null;

        return feedbackResponsesLogic.getFeedbackResponsesFromGiverForQuestion(
                question.getFeedbackQuestionId(), instructorAttributes.getEmail());
    }

    public FeedbackResponseAttributes getFeedbackResponse(String feedbackResponseId) {
        assert feedbackResponseId != null;
        return feedbackResponsesLogic.getFeedbackResponse(feedbackResponseId);
    }

    /**
     * Creates a feedback response.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return created feedback response
     * @throws InvalidParametersException if the response is not valid
     * @throws EntityAlreadyExistsException if the response already exist
     */
    public FeedbackResponseAttributes createFeedbackResponse(FeedbackResponseAttributes feedbackResponse)
            throws InvalidParametersException, EntityAlreadyExistsException {
        assert feedbackResponse != null;

        return feedbackResponsesLogic.createFeedbackResponse(feedbackResponse);
    }

    public boolean hasResponsesForCourse(String courseId) {
        return feedbackResponsesLogic.hasResponsesForCourse(courseId);
    }

    /**
     * Updates a feedback response by {@link FeedbackResponseAttributes.UpdateOptions}.
     *
     * <p>Cascade updates its associated feedback response comment
     * (e.g. associated response ID, giverSection and recipientSection).
     *
     * <p>If the giver/recipient field is changed, the response is updated by recreating the response
     * as question-giver-recipient is the primary key.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated feedback response
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the comment cannot be found
     * @throws EntityAlreadyExistsException if the response cannot be updated
     *         by recreation because of an existent response
     */
    public FeedbackResponseAttributes updateFeedbackResponseCascade(FeedbackResponseAttributes.UpdateOptions updateOptions)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {
        assert updateOptions != null;

        return feedbackResponsesLogic.updateFeedbackResponseCascade(updateOptions);
    }

    /**
     * Deletes a feedback response cascade its associated comments.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public void deleteFeedbackResponseCascade(String responseId) {
        assert responseId != null;
        feedbackResponsesLogic.deleteFeedbackResponseCascade(responseId);
    }

    /**
     * Create a feedback response comment, and return the created comment.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     */
    public FeedbackResponseCommentAttributes createFeedbackResponseComment(
            FeedbackResponseCommentAttributes feedbackResponseComment)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {
        assert feedbackResponseComment != null;

        return feedbackResponseCommentsLogic.createFeedbackResponseComment(feedbackResponseComment);
    }

    public FeedbackResponseCommentAttributes getFeedbackResponseComment(Long feedbackResponseCommentId) {
        assert feedbackResponseCommentId != null;
        return feedbackResponseCommentsLogic.getFeedbackResponseComment(feedbackResponseCommentId);
    }

    /**
     * Gets comment associated with the response.
     *
     * <p>The comment is given by a feedback participant to explain the response</p>
     *
     * @param feedbackResponseId the response id
     */
    public FeedbackResponseCommentAttributes getFeedbackResponseCommentForResponseFromParticipant(
            String feedbackResponseId) {
        assert feedbackResponseId != null;

        return feedbackResponseCommentsLogic.getFeedbackResponseCommentForResponseFromParticipant(feedbackResponseId);
    }

    /**
     * Updates a feedback response comment by {@link FeedbackResponseCommentAttributes.UpdateOptions}.
     *
     * <br/>Preconditions: <br/>
     * * All parameters are non-null.
     *
     * @return updated comment
     * @throws InvalidParametersException if attributes to update are not valid
     * @throws EntityDoesNotExistException if the comment cannot be found
     */
    public FeedbackResponseCommentAttributes updateFeedbackResponseComment(
            FeedbackResponseCommentAttributes.UpdateOptions updateOptions)
            throws EntityDoesNotExistException, InvalidParametersException {
        assert updateOptions != null;

        return feedbackResponseCommentsLogic.updateFeedbackResponseComment(updateOptions);
    }

    /**
     * Deletes a comment.
     */
    public void deleteFeedbackResponseComment(long commentId) {
        feedbackResponseCommentsLogic.deleteFeedbackResponseComment(commentId);
    }

    /**
     * Returns returns a list of sessions that were closed within past hour.
     *
     * @see FeedbackSessionsLogic#getFeedbackSessionsClosedWithinThePastHour()
     */
    public List<FeedbackSessionAttributes> getFeedbackSessionsClosedWithinThePastHour() {
        return feedbackSessionsLogic.getFeedbackSessionsClosedWithinThePastHour();
    }

    public List<FeedbackSessionAttributes> getFeedbackSessionsClosingWithinTimeLimit() {
        return feedbackSessionsLogic.getFeedbackSessionsClosingWithinTimeLimit();
    }

    public List<FeedbackSessionAttributes> getFeedbackSessionsOpeningWithinTimeLimit() {
        return feedbackSessionsLogic.getFeedbackSessionsOpeningWithinTimeLimit();
    }

    /**
     * Returns a list of sessions that require automated emails to be sent as they are published.
     *
     * @see FeedbackSessionsLogic#getFeedbackSessionsWhichNeedAutomatedPublishedEmailsToBeSent()
     */
    public List<FeedbackSessionAttributes> getFeedbackSessionsWhichNeedAutomatedPublishedEmailsToBeSent() {
        return feedbackSessionsLogic.getFeedbackSessionsWhichNeedAutomatedPublishedEmailsToBeSent();
    }

    public List<FeedbackSessionAttributes> getFeedbackSessionsWhichNeedOpenEmailsToBeSent() {
        return feedbackSessionsLogic.getFeedbackSessionsWhichNeedOpenEmailsToBeSent();
    }

    public String getSectionForTeam(String courseId, String teamName) {
        assert courseId != null;
        assert teamName != null;
        return studentsLogic.getSectionForTeam(courseId, teamName);
    }

    /**
     * Persists the given data bundle to the database.
     *
     * @see DataBundleLogic#persistDataBundle(DataBundle)
     */
    public DataBundle persistDataBundle(DataBundle dataBundle) throws InvalidParametersException {
        return dataBundleLogic.persistDataBundle(dataBundle);
    }

    /**
     * Removes the given data bundle from the database.
     *
     * @see DataBundleLogic#removeDataBundle(DataBundle)
     */
    public void removeDataBundle(DataBundle dataBundle) {
        dataBundleLogic.removeDataBundle(dataBundle);
    }

    /**
     * Puts searchable documents from the data bundle to the database.
     *
     * @see DataBundleLogic#putDocuments(DataBundle)
     */
    public void putDocuments(DataBundle dataBundle) throws SearchServiceException {
        dataBundleLogic.putDocuments(dataBundle);
    }

    public boolean isStudentsInSameTeam(String courseId, String student1Email, String student2Email) {
        assert courseId != null;
        assert student1Email != null;
        assert student2Email != null;
        return studentsLogic.isStudentsInSameTeam(courseId, student1Email, student2Email);
    }
}
