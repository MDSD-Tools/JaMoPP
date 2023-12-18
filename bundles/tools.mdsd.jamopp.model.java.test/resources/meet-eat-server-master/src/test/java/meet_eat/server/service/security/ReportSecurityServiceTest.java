package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Report;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ReportSecurityServiceTest extends SecurityServiceTest<ReportSecurityService, Report> {

    @Override
    public void testIsLegalGetNullToken() {
        // Assertions
        assertThrows(NullPointerException.class, () -> getSecurityService().isLegalGet(null));
    }

    @Override
    public void testIsLegalGetBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalGet(token));
    }

    @Test
    public void testIsLegalGetInvalidAdminToken() {
        // Test data
        Token invalidToken = new Token(getAdminUser(), "ABCDEF");

        // Assertions
        assertFalse(getSecurityService().isLegalGet(invalidToken));
    }

    @Override
    public void testIsLegalPostNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPostNotSourceToken() {
        // Test data
        Report report = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(report, token));
    }

    @Test
    public void testIsLegalPostSourceToken() {
        // Test data
        Report report = getTestEntity();
        Token token = getValidToken(report.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(report, token));
    }

    @Override
    public void testIsLegalPutNullEntity() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(null, token));
    }

    @Test
    public void testIsLegalPutBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(getTestEntity(), token));
    }

    @Test
    public void testIsLegalPutModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPut(getTestEntity(), token));
    }

    @Test
    public void testIsLegalPutAdminToken() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPut(getTestEntity(), token));
    }

    @Override
    public void testIsLegalDeleteNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(null, token));
    }

    @Test
    public void testIsLegalDeleteBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(getTestEntity(), token));
    }

    @Test
    public void testIsLegalDeleteModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(getTestEntity(), token));
    }

    @Test
    public void testIsLegalDeleteAdminToken() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(getTestEntity(), token));
    }

    @Test
    public void testIsLegalDeleteInvalidAdminToken() {
        // Test data
        Token invalidToken = new Token(getAdminUser(), "ABCDEF");

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(getTestEntity(), invalidToken));
    }

    @Override
    protected Report getTestEntity() {
        return new Report(getBasicUser(), getBasicUser(), "JUnit Test Report");
    }
}
