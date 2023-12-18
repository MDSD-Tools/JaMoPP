package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserServiceTest extends EntityServiceTest<UserService, User, String> {

    @Autowired
    private OfferService offerService;
    @Autowired
    private TokenService tokenService;

    @Before
    public void prepareForeignRepositories() {
        tokenService.getRepository().deleteAll();
    }

    //#region @Test existsPutConflict

    @Test(expected = NullPointerException.class)
    public void testExistsPutConflictNull() {
        // Execution
        getEntityService().existsPutConflict(null);
    }

    @Test
    public void testExistsPutConflictSameUser() {
        // Test data
        User user = createDistinctTestEntity();

        // Execution
        User postedUser = getEntityService().post(user);

        // Assertions
        assertFalse(getEntityService().existsPutConflict(postedUser));
    }

    @Test
    public void testExistsPutConflictSameEmail() {
        // Test data
        User userFst = getBasicUserPersistent();
        User userSnd = getBasicUserPersistent();

        // Execution
        userSnd.setEmail(userFst.getEmail());

        // Assertions
        assertTrue(getEntityService().existsPutConflict(userSnd));
    }

    @Test
    public void testExistsPutConflictNoConflict() {
        // Test data
        User userFst = getBasicUserPersistent();
        User userSnd = getBasicUserPersistent();

        // Assertions
        assertFalse(getEntityService().existsPutConflict(userSnd));
    }

    //#endregion

    //#region @Test put

    @Test(expected = EntityConflictException.class)
    public void testPutWithEmailConflict() {
        // Test data
        User userFst = getBasicUserPersistent();
        User userSnd = getBasicUserPersistent();

        // Execution
        userSnd.setEmail(userFst.getEmail());
        getEntityService().put(userSnd);
    }

    @Test
    public void testPutDeletedUser() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        getEntityService().delete(user);
        getEntityService().put(user);

        // Assertions
        assertTrue(getEntityService().exists(user.getIdentifier()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutTransientUser() {
        // Test data
        User user = getUserTransient(Role.USER);

        // Execution
        getEntityService().put(user);

        // Assertions
        assertTrue(getEntityService().exists(user.getIdentifier()));
    }

    @Test
    public void testPutUnmodifiedPassword() {
        // Test data
        User user = createDistinctTestEntity();

        // Execution
        User postedUser = getEntityService().post(user);
        Password postedPassword = postedUser.getPassword();
        User putUser = getEntityService().put(postedUser);

        // Assertions
        assertEquals(postedPassword, putUser.getPassword());
    }

    @Test
    public void testPutModifiedPassword() {
        // Test data
        User user = createDistinctTestEntity();
        Password newPassword = Password.createHashedPassword("New:ABCDEFGhijklmn123!");

        // Execution
        User postedUser = getEntityService().post(user);
        postedUser.setPassword(newPassword);
        User putUser = getEntityService().put(postedUser);

        // Assertions
        assertNotEquals(newPassword, putUser.getPassword());
        assertNotNull(putUser.getPassword().getIterations());
        assertNotNull(putUser.getPassword().getSalt());
        assertNotNull(putUser.getPassword().getHash());
    }

    //#endregion

    //#region @Test post

    @Test(expected = EntityConflictException.class)
    public void testPostWithEmailConflict() {
        // Test data
        User userFst = getUserTransient(Role.USER);
        User userSnd = getUserTransient(Role.USER);
        userSnd.setEmail(userFst.getEmail());

        // Execution
        getEntityService().post(userFst);
        getEntityService().post(userSnd);
    }

    @Test
    public void testPostPasswordDerivation() {
        // Test data
        User user = createDistinctTestEntity();
        Password password = user.getPassword();

        // Execution
        User postedUser = getEntityService().post(user);

        // Assertions
        assertNotEquals(password, postedUser.getPassword());
        assertNotNull(postedUser.getPassword().getIterations());
        assertNotNull(postedUser.getPassword().getSalt());
        assertNotNull(postedUser.getPassword().getHash());
    }

    //#endregion

    //#region @Test delete

    @Test
    public void testDeleteByEntityCascadingOffers() {
        // Test data
        User userFst = createDistinctTestEntity();
        User userSnd = createDistinctTestEntity();

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Offer offerFst = getOfferPersistent(postedUserFst);
        Offer offerSnd = getOfferPersistent(postedUserFst);
        Offer offerTrd = getOfferPersistent(postedUserSnd);
        Offer offerFth = getOfferPersistent(postedUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(offerService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst);

        // Assertions
        Iterable<Offer> offers = offerService.getAll();
        assertEquals(2, Iterables.size(offers));
        assertFalse(Iterables.contains(offers, offerFst));
        assertFalse(Iterables.contains(offers, offerSnd));
        assertTrue(Iterables.contains(offers, offerTrd));
        assertTrue(Iterables.contains(offers, offerFth));
    }

    @Test
    public void testDeleteByIdentifierCascadingOffers() {
        // Test data
        User userFst = createDistinctTestEntity();
        User userSnd = createDistinctTestEntity();

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Offer offerFst = getOfferPersistent(postedUserFst);
        Offer offerSnd = getOfferPersistent(postedUserFst);
        Offer offerTrd = getOfferPersistent(postedUserSnd);
        Offer offerFth = getOfferPersistent(postedUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(offerService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst.getIdentifier());

        // Assertions
        Iterable<Offer> offers = offerService.getAll();
        assertEquals(2, Iterables.size(offers));
        assertFalse(Iterables.contains(offers, offerFst));
        assertFalse(Iterables.contains(offers, offerSnd));
        assertTrue(Iterables.contains(offers, offerTrd));
        assertTrue(Iterables.contains(offers, offerFth));
    }

    @Test
    public void testDeleteByEntityCascadingTokens() {
        // Test data
        User userFst = createDistinctTestEntity();
        LoginCredential loginCredentialUserFst = new LoginCredential(userFst.getEmail(), userFst.getPassword());
        User userSnd = createDistinctTestEntity();
        LoginCredential loginCredentialUserSnd = new LoginCredential(userSnd.getEmail(), userSnd.getPassword());

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Token tokenFst = tokenService.createToken(loginCredentialUserFst);
        Token tokenSnd = tokenService.createToken(loginCredentialUserFst);
        Token tokenTrd = tokenService.createToken(loginCredentialUserSnd);
        Token tokenFth = tokenService.createToken(loginCredentialUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(tokenService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst);

        // Assertions
        Iterable<Token> tokens = tokenService.getAll();
        assertEquals(2, Iterables.size(tokens));
        assertFalse(Iterables.contains(tokens, tokenFst));
        assertFalse(Iterables.contains(tokens, tokenSnd));
        assertTrue(Iterables.contains(tokens, tokenTrd));
        assertTrue(Iterables.contains(tokens, tokenFth));
    }

    @Test
    public void testDeleteByIdentifierCascadingTokens() {
        // Test data
        User userFst = createDistinctTestEntity();
        LoginCredential loginCredentialUserFst = new LoginCredential(userFst.getEmail(), userFst.getPassword());
        User userSnd = createDistinctTestEntity();
        LoginCredential loginCredentialUserSnd = new LoginCredential(userSnd.getEmail(), userSnd.getPassword());

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Token tokenFst = tokenService.createToken(loginCredentialUserFst);
        Token tokenSnd = tokenService.createToken(loginCredentialUserFst);
        Token tokenTrd = tokenService.createToken(loginCredentialUserSnd);
        Token tokenFth = tokenService.createToken(loginCredentialUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(tokenService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst.getIdentifier());

        // Assertions
        Iterable<Token> tokens = tokenService.getAll();
        assertEquals(2, Iterables.size(tokens));
        assertFalse(Iterables.contains(tokens, tokenFst));
        assertFalse(Iterables.contains(tokens, tokenSnd));
        assertTrue(Iterables.contains(tokens, tokenTrd));
        assertTrue(Iterables.contains(tokens, tokenFth));
    }

    //#endregion

    //#region @Test getByEmail

    @Test(expected = NullPointerException.class)
    public void testGetByEmailNull() {
        // Execution
        getEntityService().getByEmail(null);
    }

    @Test
    public void testGetByEmail() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        User gotUser = getEntityService().getByEmail(user.getEmail()).orElseThrow();

        // Assertions
        assertEquals(user, gotUser);
    }

    //#endregion

    //#region @Test resetPassword

    @Test
    public void testResetPasswordValidEmail() {
        // Test data
        User userTransient = getUserTransient(Role.USER);
        Email email = new Email("noreply.meet.eat@gmail.com");
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        userTransient.setEmail(email);
        userTransient.setPassword(password);

        // Execution
        User user = getEntityService().post(userTransient);
        getEntityService().resetPassword(user.getEmail().toString());
        User resetUser = getEntityService().get(user.getIdentifier()).orElseThrow();

        // Assertions
        assertNotEquals(user.getPassword(), resetUser.getPassword());
    }

    @Test
    public void testResetPasswordUnknownEmail() {
        // Test data
        Email email = new Email("noreply.meet.eat@gmail.com");

        // Execution
        getEntityService().resetPassword(email.toString());

        // Assertions
        assertTrue(getEntityService().getByEmail(email).isEmpty());
    }

    //#endregion

    @Override
    protected User createDistinctTestEntity() {
        return getUserTransient(Role.USER);
    }
}
