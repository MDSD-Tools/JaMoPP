package meet_eat.data.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an email address belonging to a {@link User}.
 */
public class Email implements Serializable {

    private static final long serialVersionUID = -4013709321180976803L;

    private static final String ERROR_MESSAGE_ILLEGAL_EMAIL = "The email address must comply with RFC 5322.";

    /**
     * The official RFC 5322 email pattern.
     */
    private static final String REGEX_EMAIL_ADDRESS = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)"
            + "*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
            + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])"
            + "|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])"
            + "|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    @JsonProperty
    private final String address;

    /**
     * Creates an email address.
     *
     * @param address the email address
     */
    @JsonCreator
    public Email(@JsonProperty("address") String address) {
        if (!isLegalEmailAddress(address)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_EMAIL);
        }
        this.address = address;
    }

    /**
     * Checks the email address for correctness.
     *
     * @param address the email address
     * @return {@code true} if the email address is legal, {@code false} otherwise
     */
    public static boolean isLegalEmailAddress(String address) {
        if (Objects.isNull(address)) {
            return false;
        }
        return address.matches(REGEX_EMAIL_ADDRESS);
    }

    @Override
    @JsonGetter("address")
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
