package meet_eat.server.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EntityConflictExceptionTest {

    @Test
    public void testConstructWithoutMessage() {
        // Test data
        EntityConflictException exception;

        // Execution
        exception = new EntityConflictException();

        // Assertions
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructWithMessage() {
        // Test data
        String message = "Exception message";
        EntityConflictException exception;

        // Execution
        exception = new EntityConflictException(message);

        // Assertions
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructNullMessage() {
        // Test data
        EntityConflictException exception;

        // Execution
        exception = new EntityConflictException(null);

        // Assertions
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
