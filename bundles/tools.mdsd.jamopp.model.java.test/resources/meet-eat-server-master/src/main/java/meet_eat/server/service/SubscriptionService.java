package meet_eat.server.service;

import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Subscription subscriptions} and their state
 * persistence.
 */
@Service
public class SubscriptionService extends EntityRelationService<Subscription, User, User, String, SubscriptionRepository> {

    private final UserService userService;

    /**
     * Constructs a new instance of {@link SubscriptionService}.
     *
     * @param repository  the repository used for persistence operations
     * @param userService the service used for operations on and with {@link User} entities
     */
    @Lazy
    @Autowired
    public SubscriptionService(SubscriptionRepository repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    /**
     * Gets all {@link Subscription subscriptions} containing a specific {@link User source user} identified by
     * identifier.
     *
     * @param sourceUserIdentifier the source user's identifier
     * @return subscriptions containing a specific {@link User source user}
     */
    public Optional<Iterable<Subscription>> getBySourceUserIdentifier(String sourceUserIdentifier) {
        Optional<User> optionalUser = userService.get(sourceUserIdentifier);
        return optionalUser.map(this::getBySource);
    }

    @Override
    public boolean existsPostConflict(Subscription entity) {
        return existsBySourceAndTarget(entity.getSource(), entity.getTarget()) || super.existsPostConflict(entity);
    }
}
