package meet_eat.data.location;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnlocalizableExceptionCommonTest {

    @Test
    public void testConstructor() {
        // Execution
        UnlocalizableException exceptionOne = new UnlocalizableException();
        UnlocalizableException exceptionTwo = new UnlocalizableException();

        // Assertions
        assertNotNull(exceptionOne);
        assertNotNull(exceptionTwo);
        assertNotEquals(exceptionOne, exceptionTwo);
    }

    @Test
    public void testConstructorMessage() {
        // Execution
        String message = "Evil message";
        UnlocalizableException exception = new UnlocalizableException(message);

        // Assertions
        assertEquals(message, exception.getMessage());
    }
}
