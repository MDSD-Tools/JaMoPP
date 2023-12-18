package meet_eat.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Objects;

/**
 * Represents tags that can be assigned to {@link Offer}.
 */
public class Tag extends Entity<String> {

    private static final long serialVersionUID = -8429053043596898672L;

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_NAME = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "name");

    @JsonProperty
    private String name;

    /**
     * Creates a tag.
     *
     * @param name the tag name
     */
    public Tag(String name) {
        this.name = Objects.requireNonNull(name, ERROR_MESSAGE_NULL_NAME);
    }

    /**
     * Creates a tag.
     *
     * @param identifier the identifier
     * @param name       the tag name
     */
    @JsonCreator
    @PersistenceConstructor
    public Tag(@JsonProperty("identifier") String identifier,
               @JsonProperty("name") String name) {
        super(identifier);
        this.name = Objects.requireNonNull(name, ERROR_MESSAGE_NULL_NAME);
    }

    /**
     * Gets the name.
     *
     * @return the tag name
     */
    @JsonGetter
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the tag name
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, ERROR_MESSAGE_NULL_NAME);
    }
}