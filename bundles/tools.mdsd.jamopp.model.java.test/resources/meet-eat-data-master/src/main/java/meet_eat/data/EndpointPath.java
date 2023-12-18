package meet_eat.data;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.user.User;

/**
 * Represents descriptor of URI entity endpoint paths.
 */
public final class EndpointPath {

    /**
     * The URI endpoint path for: login
     */
    public static final String LOGIN = "/login";

    /**
     * The URI endpoint path for: logout
     */
    public static final String LOGOUT = "/logout";

    /**
     * The URI endpoint path for: {@link Tag}s
     */
    public static final String TAGS = "/tags";

    /**
     * The URI endpoint path for: {@link Offer}s
     */
    public static final String OFFERS = "/offers";

    /**
     * The URI endpoint path for: {@link User}s
     */
    public static final String USERS = "/users";

    /**
     * The URI endpoint path for: {@link Subscription Subscriptions}
     */
    public static final String SUBSCRIPTIONS = "/subscriptions";

    /**
     * The URI endpoint path for: {@link Bookmark Bookmarks}
     */
    public static final String BOOKMARKS = "/bookmarks";

    /**
     * The URI endpoint path for: {@link Participation Participations}
     */
    public static final String PARTICIPATIONS = "/participations";

    /**
     * The URI endpoint path for: {@link Report Reports}
     */
    public static final String REPORTS = "/reports";

    /**
     * The URI endpoint path for: {@link Rating Ratings}
     */
    public static final String RATINGS = "/ratings";

    /**
     * The URI endpoint path for: {@link RatingBasis#GUEST}
     */
    public static final String GUEST = "/guest";

    /**
     * The URI endpoint path for: {@link RatingBasis#HOST}
     */
    public static final String HOST = "/host";

    /**
     * The URI endpoint path for: {@link Token tokens}
     */
    public static final String TOKENS = "/tokens";

    /**
     * The URI endpoint path for: Validity of a resource
     */
    public static final String VALIDITY = "/validity";

    private EndpointPath() {
    }
}
