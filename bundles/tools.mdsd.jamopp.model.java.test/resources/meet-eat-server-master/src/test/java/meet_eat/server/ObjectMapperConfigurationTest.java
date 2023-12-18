package meet_eat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import meet_eat.data.ObjectJsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectMapperConfigurationTest {

    @Test
    public void testObjectMapper() {
        // Test data
        ObjectMapperConfiguration objectMapperConfiguration = new ObjectMapperConfiguration();

        // Execution
        ObjectMapper objectMapper = objectMapperConfiguration.objectMapper();

        // Assertions
        assertNotNull(objectMapper);
        assertEquals(ObjectJsonParser.getDefaultObjectMapper().getClass(), objectMapper.getClass());
    }
}
