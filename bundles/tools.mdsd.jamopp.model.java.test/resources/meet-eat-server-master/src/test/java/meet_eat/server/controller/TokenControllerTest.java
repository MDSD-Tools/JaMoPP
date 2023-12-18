package meet_eat.server.controller;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TokenControllerTest extends EntityControllerTest<TokenController, Token, String> {

    //#region @Test @RequestMapping

    @Test
    public void testIsValidTokenNull() {
        // Test data
        Token token = null;

        // Execution
        ResponseEntity<Boolean> responseEntity = getEntityController().isValidToken(token);

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody());
    }

    @Test
    public void testIsValidTokenPersistentToken() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Boolean> responseEntity = getEntityController().isValidToken(token);

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody());
    }

    @Test
    public void testIsValidTokenInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Boolean> responseEntity = getEntityController().isValidToken(token);

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody());
    }

    @Test
    public void testLogoutPersistentToken() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().logout(token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testLogoutUnknownToken() {
        // Test data
        Token token = new Token(getTestIdentifierInvalid(), getUserPersistent(Role.USER), "Test");

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().logout(token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testLoginCorrectCredentials() {
        // Test data
        User user = getUserPersistent(Role.USER);
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), Password.createHashedPassword(PASSWORD_VALID_VALUE));

        // Execution
        ResponseEntity<Token> responseEntity = getEntityController().login(loginCredential);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testLoginWrongCredentials() {
        // Test data
        User user = getUserPersistent(Role.USER);
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), Password.createHashedPassword("WrongCredentials123!?"));

        // Execution
        ResponseEntity<Token> responseEntity = getEntityController().login(loginCredential);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handleDelete

    @Override
    public void testHandleDeleteEntityInvalidToken() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteEntityInvalidToken);
    }

    @Override
    public void testHandleDeleteExistingIdentifier() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteExistingIdentifier);
    }

    @Override
    public void testHandleDeleteIdentifierInvalidToken() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteIdentifierInvalidToken);
    }

    @Override
    public void testHandleDeleteMultipleEntities() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteMultipleEntities);
    }

    @Override
    public void testHandleDeleteMultipleIdentifiers() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteMultipleIdentifiers);
    }

    @Override
    public void testHandleDeleteUnknownEntity() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteUnknownEntity);
    }

    @Override
    public void testHandleDeleteSingleEntity() {
        assertThrows(UnsupportedOperationException.class, super::testHandleDeleteSingleEntity);
    }

    //#endregion

    //#region @Test handleGetAll

    @Override
    public void testHandleGetAllInvalidToken() {
        assertThrows(UnsupportedOperationException.class, super::testHandleGetAllInvalidToken);
    }

    @Override
    public void testHandleGetAllMultipleElements() {
        assertThrows(UnsupportedOperationException.class, super::testHandleGetAllMultipleElements);
    }

    //#endregion

    //#region @Test handleGet

    @Override
    public void testHandleGetExistingIdentifier() {
        assertThrows(UnsupportedOperationException.class, super::testHandleGetExistingIdentifier);
    }

    @Override
    public void testHandleGetInvalidToken() {
        assertThrows(UnsupportedOperationException.class, super::testHandleGetInvalidToken);
    }

    @Override
    public void testHandleGetUnknownIdentifier() {
        assertThrows(UnsupportedOperationException.class, super::testHandleGetUnknownIdentifier);
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePostSingleEntity);
    }

    @Override
    public void testHandlePostEntityConflict() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePostEntityConflict);
    }

    @Override
    public void testHandlePostInvalidToken() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePostInvalidToken);
    }

    @Override
    public void testHandlePostMultipleEntities() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePostMultipleEntities);
    }

    //#endregion

    //#region @Test handlePut

    @Override
    public void testHandlePutNullIdentifier() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePutNullIdentifier);
    }

    @Override
    public void testHandlePutInvalidToken() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePutInvalidToken);
    }

    @Override
    public void testHandlePutUnknownEntity() {
        assertThrows(UnsupportedOperationException.class, super::testHandlePutUnknownEntity);
    }

    //#endregion

    @Override
    protected Token getTestEntityTransient() {
        return new Token(getUserPersistent(Role.USER), "ABCDEFG1234");
    }

    @Override
    protected Token getTestEntityPersistent() {
        return getTokenPersistent(getUserPersistent(Role.USER));
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidTokenIdentifier_123!";
    }
}
