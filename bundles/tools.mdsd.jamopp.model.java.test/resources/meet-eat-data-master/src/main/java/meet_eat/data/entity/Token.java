package meet_eat.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.user.User;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Objects;

/**
 * Represents an authentication key that can be assigned to a {@link User}.
 */
public class Token extends Entity<String> {

    private static final long serialVersionUID = -4894980787486819276L;

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_USER = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "user");
    private static final String ERROR_MESSAGE_NULL_VALUE = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "value");

    @DBRef
    @JsonProperty
    private final User user;
    @JsonProperty
    private final String value;

    /**
     * Creates a token.
     *
     * @param user  the user which the token will be assigned to
     * @param value the token value
     */
    public Token(User user, String value) {
        this.user = Objects.requireNonNull(user, ERROR_MESSAGE_NULL_USER);
        this.value = Objects.requireNonNull(value, ERROR_MESSAGE_NULL_VALUE);
    }

    /**
     * Creates a token.
     *
     * @param identifier the identifier
     * @param user       the user which the token will be assigned to
     * @param value      the token value
     */
    @JsonCreator
    @PersistenceConstructor
    public Token(@JsonProperty("identifier") String identifier,
                 @JsonProperty("user") User user,
                 @JsonProperty("value") String value) {
        super(identifier);
        this.user = Objects.requireNonNull(user, ERROR_MESSAGE_NULL_USER);
        this.value = Objects.requireNonNull(value, ERROR_MESSAGE_NULL_VALUE);
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    @JsonGetter
    public User getUser() {
        return user;
    }

    /**
     * Gets the token value.
     *
     * @return the token value
     */
    @JsonGetter
    public String getValue() {
        return value;
    }
}