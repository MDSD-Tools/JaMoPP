package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TokenServiceTest extends EntityServiceTest<TokenService, Token, String> {

    private static int tokenCount = 0;

    //#region @Test createToken

    @Test
    public void testCreateToken() {
        // Test data
        User user = getBasicUserPersistent();
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), password);

        // Execution
        Token token = getEntityService().createToken(loginCredential);

        // Assertions
        assertNotNull(token);
        assertNotNull(token.getIdentifier());
        assertNotNull(token.getUser());
        assertNotNull(token.getValue());
        assertTrue(getEntityService().getRepository().existsById(token.getIdentifier()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTokenUnknownEmail() {
        // Test data
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        LoginCredential loginCredential = new LoginCredential(new Email("moritz@gstuer.com"), password);

        // Execution
        getEntityService().createToken(loginCredential);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTokenWrongPassword() {
        // Test data
        User user = getBasicUserPersistent();
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), Password.createHashedPassword("Invalid!PASSWORD123"));

        // Execution
        getEntityService().createToken(loginCredential);
    }


    @Test(expected = NullPointerException.class)
    public void testCreateTokenNull() {
        // Execution
        getEntityService().createToken(null);
    }

    //#endregion

    //#region @Test isValidLoginCredential

    @Test
    public void testIsValidLoginCredential() {
        // Test data
        User user = getBasicUserPersistent();
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), password);

        // Assertions
        assertTrue(getEntityService().isValidLoginCredential(loginCredential));
    }

    @Test
    public void testIsValidLoginCredentialWithWrongPassword() {
        // Test data
        User user = getBasicUserPersistent();
        Password wrongPassword = Password.createHashedPassword("TestHelloAb123!");
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), wrongPassword);

        // Assertions
        assertFalse(getEntityService().isValidLoginCredential(loginCredential));
    }

    @Test
    public void testIsValidLoginCredentialWithUnknownEmail() {
        // Test data
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        LoginCredential loginCredential = new LoginCredential(new Email("unknown@example.com"), password);

        // Assertions
        assertFalse(getEntityService().isValidLoginCredential(loginCredential));
    }

    @Test
    public void testIsValidLoginCredentialNull() {
        // Assertions
        assertFalse(getEntityService().isValidLoginCredential(null));
    }

    //#endregion

    //#region @Test isValidToken

    @Test
    public void testIsValidToken() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
    }

    @Test
    public void testIsValidTokenDeleted() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        getEntityService().delete(token);

        // Assertions
        assertFalse(getEntityService().isValidToken(token));
    }

    @Test
    public void testIsValidTokenNull() {
        // Assertions
        assertFalse(getEntityService().isValidToken(null));
    }

    @Test
    public void testIsValidTokenWithoutIdentifier() {
        // Execution
        Token token = createDistinctTestEntity();

        // Assertions
        assertFalse(getEntityService().isValidToken(token));
    }

    @Test
    public void testIsValidTokenModifiedValue() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        Token modifiedToken = new Token(token.getIdentifier(), token.getUser(), token.getValue() + "TestModify");

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
        assertFalse(getEntityService().isValidToken(modifiedToken));
    }

    @Test
    public void testIsValidTokenModifiedUser() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        User otherUser = getBasicUserPersistent();
        Token modifiedToken = new Token(token.getIdentifier(), otherUser, token.getValue());

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
        assertFalse(getEntityService().isValidToken(modifiedToken));
    }

    //#endregion

    //#region @Test deleteByUser

    @Test
    public void testDeleteByUserEntity() {
        // Test data
        User user = getBasicUserPersistent();
        Token tokenFst = new Token(user, "ABC");
        Token tokenSnd = new Token(user, "EFG");

        // Execution
        Token postedTokenFst = getEntityService().post(tokenFst);
        Token postedTokenSnd = getEntityService().post(tokenSnd);
        getEntityService().deleteByUser(user);

        // Assertions: Post-Deletion
        assertTrue(Iterables.isEmpty(getEntityService().getAll()));
        assertFalse(getEntityService().exists(postedTokenFst.getIdentifier()));
        assertFalse(getEntityService().exists(postedTokenSnd.getIdentifier()));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByUserEntityNull() {
        // Execution
        getEntityService().deleteByUser((User) null);
    }

    @Test
    public void testDeleteByUserIdentifier() {
        // Test data
        User user = getBasicUserPersistent();
        Token tokenFst = new Token(user, "ABC");
        Token tokenSnd = new Token(user, "EFG");

        // Execution
        Token postedTokenFst = getEntityService().post(tokenFst);
        Token postedTokenSnd = getEntityService().post(tokenSnd);
        getEntityService().deleteByUser(user.getIdentifier());

        // Assertions: Post-Deletion
        assertTrue(Iterables.isEmpty(getEntityService().getAll()));
        assertFalse(getEntityService().exists(postedTokenFst.getIdentifier()));
        assertFalse(getEntityService().exists(postedTokenSnd.getIdentifier()));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByUserIdentifierNull() {
        // Execution
        getEntityService().deleteByUser((String) null);
    }

    //#endregion

    @Override
    protected Token createDistinctTestEntity() {
        User user = getBasicUserPersistent();
        return new Token(user, String.valueOf(tokenCount++));
    }
}
