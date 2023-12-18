package meet_eat.server.controller;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserControllerTest extends EntityControllerTest<UserController, User, String> {

    //#region @Test @RequestMapping

    @Test
    public void testGetUser() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().getUser(user.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void testPostUserWithToken() {
        // Test data
        User user = getUserTransient(Role.MODERATOR);
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().postUser(user, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(user.getEmail(), responseEntity.getBody().getEmail());
    }

    @Test
    public void testPostUserWithoutToken() {
        // Test data
        User user = getUserTransient(Role.USER);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().postUser(user, null);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(user.getEmail(), responseEntity.getBody().getEmail());
    }

    @Test
    public void testPutUserWithoutIdentifier() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        String changedDescription = "CHANGED:Description!";
        user.setDescription(changedDescription);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().putUser(user, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(changedDescription, responseEntity.getBody().getDescription());
    }

    @Test
    public void testPutUserWithIdentifier() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        String changedDescription = "CHANGED:Description!";
        user.setDescription(changedDescription);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().putUser(user.getIdentifier(), user, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(changedDescription, responseEntity.getBody().getDescription());
    }

    @Test
    public void testDeleteUserByEntity() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteUser(user, token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteUserByIdentifier() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteUser(user.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testPostPasswordResetExistingUser() {
        // Test data
        User user = getUserTransient(Role.USER);
        user.setEmail(new Email("noreply.meet.eat@gmail.com"));

        // Execution
        user = getEntityController().getEntityService().post(user);
        ResponseEntity<Void> responseEntity = getEntityController().postPasswordReset(user.getEmail().toString());

        // Assertions
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void testPostPasswordResetUnknownEmail() {
        // Test data
        Email email = new Email("noreply.meet.eat@gmail.com");

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().postPasswordReset(email.toString());

        // Assertions
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void testPostPasswordResetIllegalEmail() {
        // Test data
        String illegalEmail = "noreply.meet.eat@";

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().postPasswordReset(illegalEmail);

        // Assertions
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostNullToken() {
        // Test data
        User user = getUserTransient(Role.USER);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePost(user, null);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostModeratorNullToken() {
        // Test data
        User user = getUserTransient(Role.MODERATOR);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePost(user, null);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostAdminNullToken() {
        // Test data
        User user = getUserTransient(Role.ADMIN);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePost(user, null);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Override
    public void testHandlePostInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        User user = getUserTransient(Role.USER);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePost(user, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostModeratorInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        User user = getUserTransient(Role.MODERATOR);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePost(user, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostAdminInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        User user = getUserTransient(Role.ADMIN);

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePost(user, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePut

    @Test
    public void testHandlePutEmailConflict() {
        // Test data
        User userFst = getUserPersistent(Role.USER);
        User userSnd = getUserPersistent(Role.USER);
        userSnd.setEmail(userFst.getEmail());

        // Execution
        ResponseEntity<User> responseEntity = getEntityController().handlePut(userSnd.getIdentifier(), userSnd, null);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected User getTestEntityTransient() {
        return getUserTransient(Role.USER);
    }

    @Override
    protected User getTestEntityPersistent() {
        return getUserPersistent(Role.USER);
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "UnknownTestIdentifier!";
    }
}
