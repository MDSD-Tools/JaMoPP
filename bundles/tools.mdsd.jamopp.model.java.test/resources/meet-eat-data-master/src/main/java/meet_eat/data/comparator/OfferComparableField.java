package meet_eat.data.comparator;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;

/**
 * Represents comparable {@link Offer} properties.
 */
public enum OfferComparableField {

    /**
     * The offer price.
     */
    PRICE,

    /**
     * The date and time at which the {@link Offer} takes place.
     */
    TIME,

    /**
     * The distance between {@link User} and {@link Offer} location.
     */
    DISTANCE,

    /**
     * The number of {@link User}s already participating in an {@link Offer}.
     */
    PARTICIPANTS,

    /**
     * The host rating of the {@link Offer} creator.
     */
    RATING
}
