package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserSecurityServiceTest extends SecurityServiceTest<UserSecurityService, User> {

    @Override
    public void testIsLegalPostNullToken() {
        // Test data
        User user = getBasicUser();

        // Assertions
        assertTrue(getSecurityService().isLegalPost(user, null));
    }

    @Test
    public void testIsLegalPostBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), token));
    }

    @Test
    public void testIsLegalPostModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), token));
    }

    @Test
    public void testIsLegalPostAdminToken() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(getTestEntity(), token));
    }

    @Test
    public void testIsLegalPutEntityCreatorToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(user);

        // Assertions
        assertTrue(getSecurityService().isLegalPut(user, token));
    }

    @Test
    public void testIsLegalPutBasicToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(user, token));
    }

    @Test
    public void testIsLegalPutModeratorToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(user, token));
    }

    @Test
    public void testIsLegalPutAdminToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPut(user, token));
    }

    @Test
    public void testIsLegalPutInvalidEntityCreatorToken() {
        // Test data
        User user = getTestEntity();
        Token token = new Token(user, "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalPut(user, token));
    }

    @Test
    public void testIsLegalPutInvalidAdminToken() {
        // Test data
        User user = getTestEntity();
        Token token = new Token(getAdminUser(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalPut(user, token));
    }

    @Test
    public void testIsLegalDeleteEntityCreatorToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(user);

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(user, token));
    }

    @Test
    public void testIsLegalDeleteBasicToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(user, token));
    }

    @Test
    public void testIsLegalDeleteModeratorToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(user, token));
    }

    @Test
    public void testIsLegalDeleteAdminToken() {
        // Test data
        User user = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(user, token));
    }

    @Test
    public void testIsLegalDeleteInvalidEntityCreatorToken() {
        // Test data
        User user = getTestEntity();
        Token token = new Token(user, "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(user, token));
    }

    @Test
    public void testIsLegalDeleteInvalidAdminToken() {
        // Test data
        User user = getTestEntity();
        Token token = new Token(getAdminUser(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(user, token));
    }

    @Override
    protected User getTestEntity() {
        return getModeratorUser();
    }
}
