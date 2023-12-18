package meet_eat.server.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Represents a {@link Supplier} class for {@link meet_eat.data.entity.user.Password} compatible {@link String} values.
 */
public class PasswordValueSupplier implements Supplier<String> {

    /**
     * Represents the possible special characters used for password value creation.
     */
    private static final char[] SPECIAL_CHARACTERS = "!#$%&*_+,-./:;'<=>?@^|~(){}".toCharArray();

    /**
     * Represents the possible basic characters, letters respectively, used for password value creation.
     */
    private static final char[] BASIC_CHARACTERS = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ".toCharArray();

    /**
     * Represents the possible digits used for password value creation.
     */
    private static final char[] DIGITS = "1234567890".toCharArray();

    private int basicCharCount;
    private int specialCharCount;
    private int digitCount;

    /**
     * Constructs a new instance of {@link PasswordValueSupplier} with the given character and digit amounts.
     *
     * @param basicCharCount   the number of basic characters used for password value creation
     * @param specialCharCount the number of special characters used for password value creation
     * @param digitCount       the number of digits used for password value creation
     */
    public PasswordValueSupplier(int basicCharCount, int specialCharCount, int digitCount) {
        this.basicCharCount = basicCharCount;
        this.specialCharCount = specialCharCount;
        this.digitCount = digitCount;
    }

    @Override
    public String get() {
        SecureRandom random = new SecureRandom();
        List<Character> characters = new ArrayList<>();

        // Generate random special characters.
        for (int i = 0; i < specialCharCount; i++) {
            int randomIndex = random.nextInt(SPECIAL_CHARACTERS.length);
            characters.add(SPECIAL_CHARACTERS[randomIndex]);
        }

        // Generate random basic characters.
        for (int i = 0; i < basicCharCount; i++) {
            int randomIndex = random.nextInt(BASIC_CHARACTERS.length);
            characters.add(BASIC_CHARACTERS[randomIndex]);
        }

        // Generate random digits.
        for (int i = 0; i < digitCount; i++) {
            int randomIndex = random.nextInt(DIGITS.length);
            characters.add(DIGITS[randomIndex]);
        }

        // Shuffle the collection, join and return it.
        Collections.shuffle(characters, random);
        return characters.stream().map(String::valueOf).collect(Collectors.joining());
    }

    /**
     * Gets the numbers of basic characters used for password value creation.
     *
     * @return The number of basic characters.
     */
    public int getBasicCharCount() {
        return basicCharCount;
    }

    /**
     * Gets the numbers of special characters used for password value creation.
     *
     * @return The number of special characters.
     */
    public int getSpecialCharCount() {
        return specialCharCount;
    }

    /**
     * Gets the numbers of digits used for password value creation.
     *
     * @return The number of digits.
     */
    public int getDigitCount() {
        return digitCount;
    }

    /**
     * Sets the numbers of basic characters used for password value creation.
     *
     * @param basicCharCount the number of basic characters
     */
    public void setBasicCharCount(int basicCharCount) {
        this.basicCharCount = basicCharCount;
    }

    /**
     * Sets the numbers of special characters used for password value creation.
     *
     * @param specialCharCount the number of special characters
     */
    public void setSpecialCharCount(int specialCharCount) {
        this.specialCharCount = specialCharCount;
    }

    /**
     * Sets the numbers of digits used for password value creation.
     *
     * @param digitCount the number of digits
     */
    public void setDigitCount(int digitCount) {
        this.digitCount = digitCount;
    }
}
