package meet_eat.data.entity.relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.user.User;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Represents a relation between a {@link User subscriber} and a {@link User subscribed user}.
 */
public class Subscription extends EntityRelation<User, User, String> {

    private static final long serialVersionUID = 6994036473663319965L;

    /**
     * Constructs a new instance of {@link Subscription}.
     *
     * @param source the {@link User subscriber}
     * @param target the {@link User subscribed user}
     */
    public Subscription(User source, User target) {
        super(source, target);
    }

    /**
     * Constructs a new instance of {@link Subscription}.
     *
     * @param identifier the identifier of the {@link Subscription} relation
     * @param source     the {@link User subscriber}
     * @param target     the {@link User subscribed user}
     */
    @JsonCreator
    @PersistenceConstructor
    protected Subscription(@JsonProperty("identifier") String identifier,
                           @JsonProperty("source") User source,
                           @JsonProperty("target") User target) {
        super(identifier, source, target);
    }
}
