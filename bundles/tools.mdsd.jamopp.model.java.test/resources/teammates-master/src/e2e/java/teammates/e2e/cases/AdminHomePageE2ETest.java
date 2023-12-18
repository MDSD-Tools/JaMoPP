package teammates.e2e.cases;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.util.AppUrl;
import teammates.common.util.Const;
import teammates.e2e.pageobjects.AdminHomePage;

/**
 * SUT: {@link Const.WebPageURIs#ADMIN_HOME_PAGE}.
 */
public class AdminHomePageE2ETest extends BaseE2ETestCase {

    @Override
    protected void prepareTestData() {
        // no test data used in this test
    }

    @Test
    @Override
    public void testAll() {
        AppUrl url = createUrl(Const.WebPageURIs.ADMIN_HOME_PAGE);
        AdminHomePage homePage = loginAdminToPage(url, AdminHomePage.class);

        String name = "AHPUiT Instrúctör WithPlusInEmail";
        String email = "AHPUiT+++_.instr1!@gmail.tmt";
        String institute = "TEAMMATES Test Institute 1";
        String demoCourseId = "AHPUiT____.instr1_.gma-demo";

        BACKDOOR.deleteCourse(demoCourseId);

        homePage.queueInstructorForAdding(name, email, institute);

        String singleLineDetails = "Instructor With Invalid Email | invalidemail | TEAMMATES Test Institute 1";

        homePage.queueInstructorForAdding(singleLineDetails);

        homePage.addAllInstructors();

        String successMessage = homePage.getMessageForInstructor(0);
        assertTrue(successMessage.contains(
                "Instructor \"AHPUiT Instrúctör WithPlusInEmail\" has been successfully created"));

        CourseAttributes demoCourse = getCourse(demoCourseId);
        assertNotNull(demoCourse);

        String failureMessage = homePage.getMessageForInstructor(1);
        assertTrue(failureMessage.contains(
                "\"invalidemail\" is not acceptable to TEAMMATES as a/an email because it is not in the correct format."));

        BACKDOOR.deleteCourse(demoCourseId);
    }

}
