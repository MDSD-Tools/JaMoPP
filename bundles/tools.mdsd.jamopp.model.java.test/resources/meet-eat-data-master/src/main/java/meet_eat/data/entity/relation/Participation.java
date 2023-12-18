package meet_eat.data.entity.relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Represents a relation between an {@link User} instance and an {@link Offer} instance.
 * This relation is used when an {@link User} wants to participate at a certain {@link Offer}.
 */
public class Participation extends EntityRelation<User, Offer, String> {

    private static final long serialVersionUID = 1514710555568888191L;

    /**
     * Constructs a new instance of {@link Participation}.
     *
     * @param source the {@link User participant} of the given {@link Offer offer}
     * @param target the {@link Offer offer} the {@link User user} is participating at
     */
    public Participation(User source, Offer target) {
        super(source, target);
    }

    /**
     * Constructs a new instance of {@link Participation}.
     *
     * @param identifier the identifier of the {@link Participation} relation
     * @param source     the {@link User participant} of the given {@link Offer offer}
     * @param target     the {@link Offer offer} the {@link User user} is participating at
     */
    @JsonCreator
    @PersistenceConstructor
    protected Participation(@JsonProperty("identifier") String identifier,
                            @JsonProperty("source") User source,
                            @JsonProperty("target") Offer target) {
        super(identifier, source, target);
    }
}
