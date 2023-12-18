package meet_eat.data.entity.relation.rating;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Represents the amount of a given {@link Rating rating}.
 */
public enum RatingValue {

    /**
     * A one point rating.
     */
    POINTS_1(1),

    /**
     * A two points rating.
     */
    POINTS_2(2),

    /**
     * A three points rating.
     */
    POINTS_3(3),

    /**
     * A four points rating.
     */
    POINTS_4(4),

    /**
     * A five points rating.
     */
    POINTS_5(5);

    private final int integerValue;

    /**
     * Creates a new instance of a {@link RatingValue}.
     *
     * @param integerValue the represented integer value
     */
    RatingValue(int integerValue) {
        this.integerValue = integerValue;
    }

    /**
     * Gets the matching {@link RatingValue rating value} based by a given integer value.
     *
     * @param value the integer value
     * @return the rating basis
     */
    public static RatingValue getRatingValueByInteger(int value) {
        Collection<RatingValue> ratingValues = Arrays.asList(RatingValue.class.getEnumConstants());
        Optional<RatingValue> ratingValue = ratingValues.stream().filter(x -> x.getIntegerValue() == value).findFirst();
        if (!ratingValue.isPresent()) {
            throw new NoSuchElementException();
        }
        return ratingValue.get();
    }

    /**
     * Gets the integer value of a {@link RatingValue rating value}.
     *
     * @return the integer value
     */
    public int getIntegerValue() {
        return integerValue;
    }
}