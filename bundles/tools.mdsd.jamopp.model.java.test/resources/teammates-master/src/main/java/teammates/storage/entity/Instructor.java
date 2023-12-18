package teammates.storage.entity;

import java.security.SecureRandom;
import java.time.Instant;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Translate;
import com.googlecode.objectify.annotation.Unindex;

import teammates.common.util.StringHelper;

/**
 * An association class that represents the association Account
 * --> [is an instructor for] --> Course.
 */
@Entity
@Index
public class Instructor extends BaseEntity {

    /**
     * The unique id of the entity.
     *
     * @see #generateId(String, String)
     */
    @Id
    private String id;

    /**
     * The Google id of the instructor, used as the foreign key to locate the Account object.
     */
    private String googleId;

    /** The foreign key to locate the Course object. */
    private String courseId;

    /** Whether the associated course is archived. */
    private boolean isArchived;

    /** The instructor's name used for this course. */
    private String name;

    /** The instructor's email used for this course. */
    private String email;

    /** The instructor's registration key used for joining. */
    private String registrationKey;

    @Unindex
    private String role;

    private Boolean isDisplayedToStudents;

    @Unindex
    private String displayedName;

    @Unindex
    private String instructorPrivilegesAsText;

    @Translate(InstantTranslatorFactory.class)
    private Instant createdAt;

    @Translate(InstantTranslatorFactory.class)
    private Instant updatedAt;

    @SuppressWarnings("unused")
    private Instructor() {
        // required by Objectify
    }

    public Instructor(String instructorGoogleId, String courseId, boolean isArchived, String instructorName,
                      String instructorEmail, String role, boolean isDisplayedToStudents, String displayedName,
                      String instructorPrivilegesAsText) {
        this.setGoogleId(instructorGoogleId);
        this.setCourseId(courseId);
        this.setIsArchived(isArchived);
        this.setName(instructorName);
        this.setEmail(instructorEmail);
        this.setRole(role);
        this.setIsDisplayedToStudents(isDisplayedToStudents);
        this.setDisplayedName(displayedName);
        this.setInstructorPrivilegeAsText(instructorPrivilegesAsText);
        // setId should be called after setting email and courseId
        this.setUniqueId(generateId(this.getEmail(), this.getCourseId()));
        this.setRegistrationKey(generateRegistrationKey());
        this.setCreatedAt(Instant.now());
    }

    /**
     * Generates an unique ID for the instructor.
     */
    public static String generateId(String email, String courseId) {
        // Format: email%courseId e.g., adam@gmail.com%cs1101
        return email + '%' + courseId;
    }

    /**
     * Returns the unique ID of the entity (format: email%courseId).
     */
    public String getUniqueId() {
        return id;
    }

    /**
     * Sets the unique ID for the instructor entity.
     *
     * @param uniqueId
     *          The unique ID of the entity (format: email%courseId).
     */
    public void setUniqueId(String uniqueId) {
        this.id = uniqueId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String instructorGoogleId) {
        this.googleId = instructorGoogleId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the archived status of the instructor.
     */
    public boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public String getName() {
        return name;
    }

    public void setName(String instructorName) {
        this.name = instructorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String instructorEmail) {
        this.email = instructorEmail;
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String key) {
        this.registrationKey = key;
    }

    /**
     * Generate unique registration key for the instructor.
     * The key contains random elements to avoid being guessed.
     */
    private String generateRegistrationKey() {
        String uniqueId = getUniqueId();
        SecureRandom prng = new SecureRandom();

        return StringHelper.encrypt(uniqueId + prng.nextInt());
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns whether the instructor is displayed to students.
     */
    public boolean isDisplayedToStudents() {
        if (this.isDisplayedToStudents == null) {
            return true;
        }
        return isDisplayedToStudents;
    }

    public void setIsDisplayedToStudents(boolean shouldDisplayToStudents) {
        this.isDisplayedToStudents = shouldDisplayToStudents;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    /**
     * Gets the instructor privileges stored in JSON format.
     */
    public String getInstructorPrivilegesAsText() {
        return instructorPrivilegesAsText;
    }

    public void setInstructorPrivilegeAsText(String instructorPrivilegesAsText) {
        this.instructorPrivilegesAsText = instructorPrivilegesAsText;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the createdAt timestamp.
     */
    public void setCreatedAt(Instant created) {
        this.createdAt = created;
        setLastUpdate(created);
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setLastUpdate(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Updates the updatedAt timestamp when saving.
     */
    @OnSave
    public void updateLastUpdateTimestamp() {
        this.setLastUpdate(Instant.now());
    }

}
