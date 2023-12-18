package meet_eat.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Objects;

/**
 * Represents a class to serialize objects to its JSON representation and to deserialize JSON representations of objects
 * to objects.
 */
public class ObjectJsonParser {

    private static final String ERROR_MESSAGE_STRING_NOT_PARSABLE = "Given json string must be parsable, but was not.";
    private static final String ERROR_MESSAGE_OBJECT_NOT_PARSABLE = "Given object must be parsable, but was not.";

    private ObjectMapper objectMapper;

    /**
     * Creates a default object JSON parser.
     */
    public ObjectJsonParser() {
        this.objectMapper = getDefaultObjectMapper();
    }

    /**
     * Creates an objects JSON parser by given {@link ObjectMapper}.
     *
     * @param objectMapper the object mapper
     */
    public ObjectJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    /**
     * Parses a JSON representation to an {@link Object}.
     *
     * @param jsonString the JSON representation
     * @param type       the class name
     * @param <T>        the class type
     * @return the object represented by the JSON string if, and only if, the string is parsable
     */
    public <T> T parseJsonStringToObject(String jsonString, Class<T> type) {
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException(ERROR_MESSAGE_STRING_NOT_PARSABLE, exception);
        }
    }

    /**
     * Parses a JSON representation to an {@link Object}.
     *
     * @param jsonString the jsonString
     * @param type       the java type
     * @param <T>        the class type
     * @return the object represented by the JSON string if, and only if, the string is parsable
     */
    public <T> T parseJsonStringToObject(String jsonString, JavaType type) {
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException(ERROR_MESSAGE_STRING_NOT_PARSABLE, exception);
        }
    }

    /**
     * Parses an {@link Object} to its JSON representation.
     *
     * @param object the object
     * @return the JSON representation of the object if, and only if, the object is parsable
     */
    public String parseObjectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException(ERROR_MESSAGE_OBJECT_NOT_PARSABLE, exception);
        }
    }

    /**
     * Gets the current {@link ObjectMapper}.
     *
     * @return the object mapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Sets the {@link ObjectMapper}.
     *
     * @param objectMapper the object mapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    /**
     * Gets the default {@link ObjectMapper}.
     *
     * @return the default object mapper
     */
    public static ObjectMapper getDefaultObjectMapper() {
        // Add JDK8 and java.time.* specific functionality to mapper using module registration.
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        // Activate type/class meta-properties of elements generically typed within collections for example.
        objectMapper.activateDefaultTypingAsProperty(new DefaultBaseTypeLimitingValidator(),
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT,
                JsonTypeInfo.Id.CLASS.getDefaultPropertyName());

        return objectMapper;
    }
}
