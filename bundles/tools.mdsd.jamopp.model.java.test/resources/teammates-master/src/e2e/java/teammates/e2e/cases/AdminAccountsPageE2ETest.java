package teammates.e2e.cases;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.util.AppUrl;
import teammates.common.util.Const;
import teammates.e2e.pageobjects.AdminAccountsPage;

/**
 * SUT: {@link Const.WebPageURIs#ADMIN_ACCOUNTS_PAGE}.
 */
public class AdminAccountsPageE2ETest extends BaseE2ETestCase {

    @Override
    protected void prepareTestData() {
        testData = loadDataBundle("/AdminAccountsPageE2ETest.json");
        removeAndRestoreDataBundle(testData);
    }

    @Test
    @Override
    public void testAll() {

        String googleId = "tm.e2e.AAccounts.instr2";

        ______TS("verify loaded data");

        AppUrl accountsPageUrl = createUrl(Const.WebPageURIs.ADMIN_ACCOUNTS_PAGE)
                .withParam(Const.ParamsNames.INSTRUCTOR_ID, googleId);
        AdminAccountsPage accountsPage = loginAdminToPage(accountsPageUrl, AdminAccountsPage.class);

        AccountAttributes account = getAccount(googleId);
        accountsPage.verifyAccountDetails(account);

        ______TS("action: remove instructor from course");

        InstructorAttributes instructor = testData.instructors.get("AAccounts.instr2-AAccounts.CS2103");
        String courseId = instructor.getCourseId();

        verifyPresentInDatabase(instructor);
        accountsPage.clickRemoveInstructorFromCourse(courseId);
        accountsPage.verifyStatusMessage("Instructor is successfully deleted from course \"" + courseId + "\"");
        verifyAbsentInDatabase(instructor);

        ______TS("action: remove student from course");

        StudentAttributes student = testData.students.get("AAccounts.instr2-student-CS2103");
        courseId = student.getCourse();

        verifyPresentInDatabase(student);
        accountsPage.clickRemoveStudentFromCourse(courseId);
        accountsPage.verifyStatusMessage("Student is successfully deleted from course \"" + courseId + "\"");
        verifyAbsentInDatabase(student);

        ______TS("action: downgrade instructor account");

        InstructorAttributes instructor2 = testData.instructors.get("AAccounts.instr2-AAccounts.CS2104");
        InstructorAttributes instructor3 = testData.instructors.get("AAccounts.instr2-AAccounts.CS1101");
        verifyPresentInDatabase(instructor2);
        verifyPresentInDatabase(instructor3);

        accountsPage.clickDowngradeAccount();
        accountsPage.verifyStatusMessage("Instructor account is successfully downgraded to student.");
        accountsPage.waitForPageToLoad();

        account = getAccount(googleId);
        assertFalse(account.isInstructor());
        accountsPage.verifyAccountDetails(account);

        // instructor entities should also be deleted
        verifyAbsentInDatabase(instructor2);
        verifyAbsentInDatabase(instructor3);

        ______TS("action: delete account entirely");

        StudentAttributes student2 = testData.students.get("AAccounts.instr2-student-CS2104");
        StudentAttributes student3 = testData.students.get("AAccounts.instr2-student-CS1101");
        verifyPresentInDatabase(student2);
        verifyPresentInDatabase(student3);

        accountsPage.clickDeleteAccount();
        accountsPage.verifyStatusMessage("Account \"" + googleId + "\" is successfully deleted.");

        verifyAbsentInDatabase(account);

        // student entities should be deleted
        verifyAbsentInDatabase(student2);
        verifyAbsentInDatabase(student3);

    }

}
