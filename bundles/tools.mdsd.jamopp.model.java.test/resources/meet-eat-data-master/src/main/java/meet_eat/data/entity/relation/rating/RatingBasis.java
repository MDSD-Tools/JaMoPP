package meet_eat.data.entity.relation.rating;

import meet_eat.data.entity.user.User;

/**
 * Represents a basis on which a {@link User user} receives a {@link Rating rating}.
 */
public enum RatingBasis {

    /**
     * The rating as a host.
     */
    HOST,

    /**
     * The rating as a guest.
     */
    GUEST
}