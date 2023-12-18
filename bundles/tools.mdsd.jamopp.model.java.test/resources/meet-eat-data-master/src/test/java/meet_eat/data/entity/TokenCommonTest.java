package meet_eat.data.entity;

import meet_eat.data.entity.user.User;
import meet_eat.data.factory.TokenFactory;
import meet_eat.data.factory.UserFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokenCommonTest {

    @Test
    public void testConstructorWithUserAndValue() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User user = userFactory.getValidObject();
        String value = "ThisIsAValue";

        // Execution
        Token token = new Token(user, value);

        // Assertions
        assertNotNull(token);
    }

    @Test
    public void testConstructor() {
        // Execution
        Token token = new TokenFactory().getValidObject();

        // Assertion
        assertNotNull(token);
    }

    @Test
    public void testConstructorNullIdentifier() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User user = userFactory.getValidObject();
        String value = "ACrazyValue";

        // Execution
        Token token = new Token(null, user, value);

        // Assertion
        assertNotNull(token);
        assertNull(token.getIdentifier());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullUser() {
        // Test data
        String identifier = "AnIdentifier";
        String value = "ValueIsNotNull";

        // Execution
        new Token(identifier, null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        // Test data
        String identifier = "AnotherIdentifier";
        UserFactory userFactory = new UserFactory();
        User user = userFactory.getValidObject();

        // Execution
        new Token(identifier, user, null);
    }

    @Test
    public void testTokenNotEqual() {
        // Execution
        TokenFactory tokenFactory = new TokenFactory();
        Token token1 = tokenFactory.getValidObject();
        Token token2 = tokenFactory.getValidObject();

        // Assertion
        assertNotEquals(token1, token2);
    }

    @Test
    public void testEquals() {
        // Execution
        Token token = new TokenFactory().getValidObject();
        Token tokenCopy = new Token(token.getIdentifier(), token.getUser(), token.getValue());

        // Assertions
        assertEquals(token, token);
        assertNotEquals(null, token);
        assertNotEquals(token, new Object());
        assertEquals(token, tokenCopy);
        assertEquals(token.hashCode(), tokenCopy.hashCode());
    }
}
