package meet_eat.server.service.security;

import static org.junit.Assert.*;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;

public class TokenSecurityServiceTest extends SecurityServiceTest<TokenSecurityService, Token> {

    @Override
    public void testIsLegalGetBasicToken() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalGet(token));
    }

    @Override
    public void testIsLegalGetModeratorToken() {
        // Test data
        User user = getModeratorUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalGet(token));
    }

    @Override
    public void testIsLegalGetAdminToken() {
        // Test data
        User user = getAdminUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalGet(token));
    }

    @Override
    public void testIsLegalGetNullToken() {
        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalGet(null));
    }

    @Override
    public void testIsLegalPostNullToken() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalPost(token, null));
    }

    @Override
    public void testIsLegalPostNullEntity() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalPost(null, token));
    }

    @Override
    public void testIsLegalPutNullToken() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalPut(token, null));
    }

    @Override
    public void testIsLegalPutNullEntity() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalPut(null, token));
    }

    @Override
    public void testIsLegalDeleteNullToken() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalDelete(token, null));
    }

    @Override
    public void testIsLegalDeleteNullEntity() {
        // Test data
        User user = getBasicUser();
        Token token = getValidToken(user);

        // Assertions
        assertThrows(UnsupportedOperationException.class, () -> getSecurityService().isLegalDelete(null, token));
    }

    @Override
    protected Token getTestEntity() {
        return getValidToken(getBasicUser());
    }
}
