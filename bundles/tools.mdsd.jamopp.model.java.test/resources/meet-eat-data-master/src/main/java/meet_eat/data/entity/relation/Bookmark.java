package meet_eat.data.entity.relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Represents a relation between an {@link User} instance and an {@link Offer} instance.
 * This relation is used when an {@link User} needs to find a certain {@link Offer} again afterwards.
 */
public class Bookmark extends EntityRelation<User, Offer, String> {

    private static final long serialVersionUID = 1404003165662154028L;

    /**
     * Constructs a new instance of {@link Bookmark}.
     *
     * @param source the {@link User user} bookmarking the given {@link Offer offer}
     * @param target the {@link Offer offer} to be bookmarked by the given {@link User user}
     */
    public Bookmark(User source, Offer target) {
        super(source, target);
    }

    /**
     * Constructs a new instance of {@link Bookmark}.
     *
     * @param identifier the identifier of the {@link Bookmark} relation
     * @param source     the {@link User user} bookmarking the given {@link Offer offer}
     * @param target     the {@link Offer offer} to be bookmarked by the given {@link User user}
     */
    @JsonCreator
    @PersistenceConstructor
    protected Bookmark(@JsonProperty("identifier") String identifier,
                       @JsonProperty("source") User source,
                       @JsonProperty("target") Offer target) {
        super(identifier, source, target);
    }
}
