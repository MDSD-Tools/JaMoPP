package meet_eat.server;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ApplicationTest {

    @Test
    public void testConstructor() {
        // Assertions
        Application application = new Application();
        assertNotNull(application);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainNull() {
        // Assertions
        Application.main(null);
    }

    @Test
    public void testMainEmptyArgs() {
        // Test data
        String[] args = new String[0];

        // Assertions
        Application.main(args);
    }
}
