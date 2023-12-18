package meet_eat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import meet_eat.data.ObjectJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Represents the configuration of the globally used {@link ObjectMapper}.
 */
@Configuration
public class ObjectMapperConfiguration {

    /**
     * Gets an {@link ObjectMapper}.
     *
     * @return An {@link ObjectMapper}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectJsonParser.getDefaultObjectMapper();
    }
}
