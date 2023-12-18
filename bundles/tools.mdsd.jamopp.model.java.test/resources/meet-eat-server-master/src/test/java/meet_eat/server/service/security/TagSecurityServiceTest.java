package meet_eat.server.service.security;

import static org.junit.Assert.*;

import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import org.junit.Test;

public class TagSecurityServiceTest extends SecurityServiceTest<TagSecurityService, Tag> {

    @Override
    public void testIsLegalPostNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), null));
    }

    @Override
    public void testIsLegalPostNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(null, token));
    }

    @Test
    public void testIsLegalPostNullEntityBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(null, token));
    }

    @Test
    public void testIsLegalPostNullEntityModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(null, token));
    }

    @Override
    public void testIsLegalPutNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPut(getTestEntity(), null));
    }

    @Override
    public void testIsLegalPutNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPut(null, token));
    }

    @Test
    public void testIsLegalPutNullEntityBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(null, token));
    }

    @Test
    public void testIsLegalPutNullEntityModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(null, token));
    }

    @Override
    public void testIsLegalDeleteNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalDelete(getTestEntity(), null));
    }

    @Override
    public void testIsLegalDeleteNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(null, token));
    }

    @Test
    public void testIsLegalDeleteNullEntityBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(null, token));
    }

    @Test
    public void testIsLegalDeleteNullEntityModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(null, token));
    }

    @Override
    protected Tag getTestEntity() {
        return new Tag(String.valueOf(System.currentTimeMillis()));
    }
}
