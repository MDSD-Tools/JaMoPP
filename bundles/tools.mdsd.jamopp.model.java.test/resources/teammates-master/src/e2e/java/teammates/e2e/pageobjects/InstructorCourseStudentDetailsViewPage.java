package teammates.e2e.pageobjects;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.datatransfer.attributes.StudentProfileAttributes;

/**
 * Represents the instructor course student details view page of the website.
 */
public class InstructorCourseStudentDetailsViewPage extends AppPage {
    private static final String NOT_SPECIFIED_LABEL = "Not Specified";

    @FindBy (id = "student-name")
    private WebElement studentName;

    @FindBy (id = "name-with-gender")
    private WebElement studentNameWithGender;

    @FindBy (id = "personal-email")
    private WebElement studentPersonalEmail;

    @FindBy (id = "institution")
    private WebElement studentInstitution;

    @FindBy (id = "nationality")
    private WebElement studentNationality;

    @FindBy (id = "course-id")
    private WebElement courseId;

    @FindBy (id = "section-name")
    private WebElement studentSectionName;

    @FindBy (id = "team-name")
    private WebElement studentTeamName;

    @FindBy (id = "email")
    private WebElement studentOfficialEmail;

    @FindBy (id = "comments")
    private WebElement studentComments;

    @FindBy (id = "more-info")
    private WebElement moreInformation;

    public InstructorCourseStudentDetailsViewPage(Browser browser) {
        super(browser);
    }

    @Override
    protected boolean containsExpectedPageContents() {
        return getPageSource().contains("Enrollment Details");
    }

    public void verifyIsCorrectPage(String expectedCourseId, String expectedStudentEmail) {
        verifyDetail(expectedCourseId, courseId);
        verifyDetail(expectedStudentEmail, studentOfficialEmail);
    }

    public void verifyStudentDetails(StudentProfileAttributes studentProfile, StudentAttributes student) {
        verifyDetail(student.getName(), studentName);

        StudentProfileAttributes profileToTest = studentProfile;
        if (studentProfile == null) {
            profileToTest = StudentProfileAttributes.builder(student.getGoogleId()).build();
        }
        verifyDetail(getExpectedNameWithGender(profileToTest), studentNameWithGender);
        verifyDetail(profileToTest.getEmail(), studentPersonalEmail);
        verifyDetail(profileToTest.getInstitute(), studentInstitution);
        verifyDetail(profileToTest.getNationality(), studentNationality);

        verifyDetail(student.getCourse(), courseId);
        verifyDetail(student.getSection(), studentSectionName);
        verifyDetail(student.getTeam(), studentTeamName);
        verifyDetail(student.getEmail(), studentOfficialEmail);
        verifyDetail(student.getComments(), studentComments);

        verifyDetail(profileToTest.getMoreInfo(), moreInformation);
    }

    private void verifyDetail(String expected, WebElement detailField) {
        if (expected.isEmpty()) {
            assertEquals(NOT_SPECIFIED_LABEL, detailField.getText());
        } else {
            assertEquals(expected, detailField.getText());
        }
    }

    private String getExpectedNameWithGender(StudentProfileAttributes profile) {
        String name = profile.getShortName();
        StudentProfileAttributes.Gender gender = profile.getGender();
        String expectedName = name.isEmpty()
                ? NOT_SPECIFIED_LABEL
                : name;
        String expectedGender = gender.equals(StudentProfileAttributes.Gender.OTHER)
                ? NOT_SPECIFIED_LABEL
                : gender.toString();

        return expectedName + " (" + expectedGender + ")";
    }
}
