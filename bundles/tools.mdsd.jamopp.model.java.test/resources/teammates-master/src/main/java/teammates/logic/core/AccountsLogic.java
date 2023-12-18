package teammates.logic.core;

import java.util.List;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InstructorUpdateException;
import teammates.common.exception.InvalidParametersException;
import teammates.storage.api.AccountsDb;

/**
 * Handles operations related to accounts.
 *
 * @see AccountAttributes
 * @see AccountsDb
 */
public final class AccountsLogic {

    private static final AccountsLogic instance = new AccountsLogic();

    private final AccountsDb accountsDb = AccountsDb.inst();

    private ProfilesLogic profilesLogic;
    private CoursesLogic coursesLogic;
    private InstructorsLogic instructorsLogic;
    private StudentsLogic studentsLogic;

    private AccountsLogic() {
        // prevent initialization
    }

    public static AccountsLogic inst() {
        return instance;
    }

    void initLogicDependencies() {
        profilesLogic = ProfilesLogic.inst();
        coursesLogic = CoursesLogic.inst();
        instructorsLogic = InstructorsLogic.inst();
        studentsLogic = StudentsLogic.inst();
    }

    /**
     * Creates an account.
     *
     * @return the created account
     * @throws InvalidParametersException if the account is not valid
     * @throws EntityAlreadyExistsException if the account already exists in the database.
     */
    AccountAttributes createAccount(AccountAttributes accountData)
            throws InvalidParametersException, EntityAlreadyExistsException {
        return accountsDb.createEntity(accountData);
    }

    /**
     * Gets an account.
     */
    public AccountAttributes getAccount(String googleId) {
        return accountsDb.getAccount(googleId);
    }

    /**
     * Returns true if the given account exists and is an instructor.
     */
    public boolean isAccountAnInstructor(String googleId) {
        AccountAttributes a = accountsDb.getAccount(googleId);
        return a != null && a.isInstructor();
    }

    /**
     * Joins the user as a student.
     */
    public StudentAttributes joinCourseForStudent(String registrationKey, String googleId)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {
        StudentAttributes student = validateStudentJoinRequest(registrationKey, googleId);

        // Register the student
        student.setGoogleId(googleId);
        try {
            studentsLogic.updateStudentCascade(
                    StudentAttributes.updateOptionsBuilder(student.getCourse(), student.getEmail())
                            .withGoogleId(student.getGoogleId())
                            .build());
        } catch (EntityDoesNotExistException e) {
            assert false : "Student disappeared while trying to register";
        }

        if (accountsDb.getAccount(googleId) == null) {
            createStudentAccount(student);
        }

        return student;
    }

    /**
     * Joins the user as an instructor and sets the institute if it is not null.
     * If the given institute is null, the instructor is given the institute of an existing instructor of the same course.
     */
    public InstructorAttributes joinCourseForInstructor(String key, String googleId)
            throws InvalidParametersException, EntityDoesNotExistException, EntityAlreadyExistsException {
        InstructorAttributes instructor = validateInstructorJoinRequest(key, googleId);

        // Register the instructor
        instructor.setGoogleId(googleId);
        try {
            instructorsLogic.updateInstructorByEmail(
                    InstructorAttributes.updateOptionsWithEmailBuilder(instructor.getCourseId(), instructor.getEmail())
                            .withGoogleId(instructor.getGoogleId())
                            .build());
        } catch (EntityDoesNotExistException e) {
            assert false : "Instructor disappeared while trying to register";
        } catch (InstructorUpdateException e) {
            assert false;
        }

        AccountAttributes account = accountsDb.getAccount(googleId);
        String instituteToSave = coursesLogic.getCourseInstitute(instructor.getCourseId());

        if (account == null) {
            try {
                createAccount(AccountAttributes.builder(googleId)
                        .withName(instructor.getName())
                        .withEmail(instructor.getEmail())
                        .withInstitute(instituteToSave)
                        .withIsInstructor(true)
                        .build());
            } catch (EntityAlreadyExistsException e) {
                assert false : "Account already exists.";
            }
        } else {
            makeAccountInstructor(googleId);
        }

        // Update the googleId of the student entity for the instructor which was created from sample data.
        StudentAttributes student = studentsLogic.getStudentForEmail(instructor.getCourseId(), instructor.getEmail());
        if (student != null) {
            student.setGoogleId(googleId);
            studentsLogic.updateStudentCascade(
                    StudentAttributes.updateOptionsBuilder(student.getCourse(), student.getEmail())
                            .withGoogleId(student.getGoogleId())
                            .build());
        }

        return instructor;
    }

    private InstructorAttributes validateInstructorJoinRequest(String registrationKey, String googleId)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {
        InstructorAttributes instructorForKey = instructorsLogic.getInstructorForRegistrationKey(registrationKey);

        if (instructorForKey == null) {
            throw new EntityDoesNotExistException("No instructor with given registration key: " + registrationKey);
        }

        if (instructorForKey.isRegistered()) {
            if (instructorForKey.getGoogleId().equals(googleId)) {
                AccountAttributes existingAccount = accountsDb.getAccount(googleId);
                if (existingAccount != null && existingAccount.isInstructor()) {
                    throw new EntityAlreadyExistsException("Instructor has already joined course");
                }
            } else {
                throw new EntityAlreadyExistsException("Instructor has already joined course");
            }
        } else {
            // Check if this Google ID has already joined this course
            InstructorAttributes existingInstructor =
                    instructorsLogic.getInstructorForGoogleId(instructorForKey.getCourseId(), googleId);

            if (existingInstructor != null) {
                throw new EntityAlreadyExistsException("Instructor has already joined course");
            }
        }

        return instructorForKey;
    }

    private StudentAttributes validateStudentJoinRequest(String registrationKey, String googleId)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {

        StudentAttributes studentRole = studentsLogic.getStudentForRegistrationKey(registrationKey);

        if (studentRole == null) {
            throw new EntityDoesNotExistException("No student with given registration key: " + registrationKey);
        }

        if (studentRole.isRegistered()) {
            throw new EntityAlreadyExistsException("Student has already joined course");
        }

        // Check if this Google ID has already joined this course
        StudentAttributes existingStudent =
                studentsLogic.getStudentForCourseIdAndGoogleId(studentRole.getCourse(), googleId);

        if (existingStudent != null) {
            throw new EntityAlreadyExistsException("Student has already joined course");
        }

        return studentRole;
    }

    /**
     * Downgrades an instructor account to student account.
     *
     * <p>Cascade deletes all instructors associated with the account.
     */
    public void downgradeInstructorToStudentCascade(String googleId) throws EntityDoesNotExistException {
        instructorsLogic.deleteInstructorsForGoogleIdCascade(googleId);

        try {
            accountsDb.updateAccount(
                    AccountAttributes.updateOptionsBuilder(googleId)
                            .withIsInstructor(false)
                            .build()
            );
        } catch (InvalidParametersException e) {
            assert false : "Invalid account data detected unexpectedly "
                    + "while removing instruction privileges from account " + googleId + ": " + e.getMessage();
        }
    }

    /**
     * Makes an account as an instructor account.
     */
    void makeAccountInstructor(String googleId) throws InvalidParametersException, EntityDoesNotExistException {
        accountsDb.updateAccount(
                AccountAttributes.updateOptionsBuilder(googleId)
                        .withIsInstructor(true)
                        .build()
        );
    }

    /**
     * Deletes both instructor and student privileges, as long as the account and associated student profile.
     *
     * <ul>
     * <li>Fails silently if no such account.</li>
     * </ul>
     */
    public void deleteAccountCascade(String googleId) {
        if (accountsDb.getAccount(googleId) == null) {
            return;
        }

        profilesLogic.deleteStudentProfile(googleId);

        // to prevent orphan course
        List<InstructorAttributes> instructorsToDelete =
                instructorsLogic.getInstructorsForGoogleId(googleId, false);
        for (InstructorAttributes instructorToDelete : instructorsToDelete) {
            if (instructorsLogic.getInstructorsForCourse(instructorToDelete.getCourseId()).size() <= 1) {
                // the instructor is the last instructor in the course
                coursesLogic.deleteCourseCascade(instructorToDelete.getCourseId());
            }
        }

        instructorsLogic.deleteInstructorsForGoogleIdCascade(googleId);
        studentsLogic.deleteStudentsForGoogleIdCascade(googleId);
        accountsDb.deleteAccount(googleId);
    }

    /**
     * Creates a student account.
     */
    private void createStudentAccount(StudentAttributes student)
            throws InvalidParametersException, EntityAlreadyExistsException {

        AccountAttributes account = AccountAttributes.builder(student.getGoogleId())
                .withEmail(student.getEmail())
                .withName(student.getName())
                .withIsInstructor(false)
                .withInstitute(coursesLogic.getCourseInstitute(student.getCourse()))
                .build();

        accountsDb.createEntity(account);
    }

}
