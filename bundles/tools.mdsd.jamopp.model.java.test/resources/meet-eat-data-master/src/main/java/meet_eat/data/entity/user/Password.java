package meet_eat.data.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a password belonging to a {@link User}.
 */
public class Password implements Serializable {

    private static final long serialVersionUID = -4013709321180976803L;

    private static final String ERROR_MESSAGE_ILLEGAL_PASSWORD = "The password must comply with password guidelines.";
    private static final String ERROR_MESSAGE_MATCH_NON_DERIVED = "Password to be matched must be derived.";

    /**
     * Password properties:
     * <p>
     * <ul>
     * <li> At least one lower-/uppercase letter
     * <li> At least one digit
     * <li> At least one special character
     * <li> Minimum|Maximum length of 8|32 characters
     * </ul>
     * <p>
     */
    private static final String REGEX_PASSWORD = "^(?=.*[a-z])"
            + "(?=.*[A-Z])"
            + "(?=.*\\d)"
            + "(?=.*[!#$%&*_+,-./:;'<=>?@^|~(){}])"
            + ".{8,32}$";

    /**
     * Encodes a string with SHA-512 algorithm and given charset.
     */
    private static final Function<String, String> HASH_FUNCTION = s -> Hashing.sha512().hashString(s, Charsets.UTF_16).toString();

    private static final int DERIVATION_WIDTH = 512;
    private static final int SALT_BYTE_LENGTH = 64;

    @JsonProperty
    private final String hash;
    @JsonProperty
    private final String salt;
    @JsonProperty
    private final Integer iterations;

    /**
     * Creates a password with given hash, salt and iterations.
     *
     * @param hash       the hash
     * @param salt       the salt
     * @param iterations the amount of iterations
     */
    @JsonCreator
    protected Password(@JsonProperty("hash") String hash,
                       @JsonProperty("salt") String salt,
                       @JsonProperty("iterations") Integer iterations) {
        this.hash = hash;
        this.salt = salt;
        this.iterations = iterations;
    }

    /**
     * Gets the hash.
     *
     * @return the hash
     */
    @JsonGetter
    public String getHash() {
        return hash;
    }

    /**
     * Gets the salt.
     *
     * @return the salt
     */
    @JsonGetter
    public String getSalt() {
        return salt;
    }

    /**
     * Gets the amount of iterations used for password encoding.
     *
     * @return the amount of iterations
     */
    @JsonGetter
    public Integer getIterations() {
        return iterations;
    }

    /**
     * Checks the password for correctness.
     *
     * @param password the password
     * @return {@code true} if the password is legal, {@code false} otherwise
     */
    public static boolean isLegalPassword(String password) {
        if (Objects.isNull(password)) {
            return false;
        }
        return password.matches(REGEX_PASSWORD);
    }

    /**
     * Creates a hashed password by applying {@link Password#HASH_FUNCTION}.
     *
     * @param passwordValue the password string value
     * @return the hashed password if, and only if, the password is legal
     */
    public static Password createHashedPassword(String passwordValue) {
        if (!isLegalPassword(passwordValue)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_PASSWORD);
        }
        String hash = HASH_FUNCTION.apply(passwordValue);
        return new Password(hash, null, null);
    }

    /**
     * Derives an encoded password with given salt and iterations.
     *
     * @param salt       the salt
     * @param iterations the amount of iterations
     * @return the derived password
     */
    public Password derive(String salt, int iterations) {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(salt, iterations, DERIVATION_WIDTH);
        encoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
        return new Password(encoder.encode(hash), salt, iterations);
    }

    /**
     * Checks if a {@link Password password} can be derived to a given derived password using the salt and iterations
     * of the derived password.
     *
     * @param derivedPassword the derived password
     * @return {@code true} if the password can be derived to the given derived password, {@code false} if not
     */
    public boolean matches(Password derivedPassword) {
        if (Objects.isNull(derivedPassword.getSalt()) || Objects.isNull(derivedPassword.getIterations())) {
            throw new IllegalStateException(ERROR_MESSAGE_MATCH_NON_DERIVED);
        }
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(derivedPassword.getSalt(), derivedPassword.getIterations(), DERIVATION_WIDTH);
        encoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
        return encoder.matches(hash, derivedPassword.getHash());
    }

    /**
     * Generates a random salt used for password encoding.
     *
     * @return the generated salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        random.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(hash, password.hash) &&
                Objects.equals(salt, password.salt) &&
                Objects.equals(iterations, password.iterations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, salt, iterations);
    }
}
