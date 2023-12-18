package meet_eat.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

import java.util.Objects;

/**
 * Represents the login credential of a {@link User}.
 */
public class LoginCredential {

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_EMAIL = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "email address");
    private static final String ERROR_MESSAGE_NULL_PASSWORD = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "password");

    @JsonProperty
    private final Email email;
    @JsonProperty
    private final Password password;

    /**
     * Creates the login credential.
     *
     * @param email    the email address
     * @param password the password
     */
    @JsonCreator
    public LoginCredential(@JsonProperty("email") Email email,
                           @JsonProperty("password") Password password) {
        this.email = Objects.requireNonNull(email, ERROR_MESSAGE_NULL_EMAIL);
        this.password = Objects.requireNonNull(password, ERROR_MESSAGE_NULL_PASSWORD);
    }

    /**
     * Get the email address.
     *
     * @return the email address
     */
    @JsonGetter
    public Email getEmail() {
        return email;
    }

    /**
     * Get the password.
     *
     * @return the password
     */
    @JsonGetter
    public Password getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginCredential that = (LoginCredential) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}