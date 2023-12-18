package meet_eat.server.repository;

import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Subscription} instances.
 */
@Repository
public interface SubscriptionRepository extends EntityRelationRepository<Subscription, User, User, String> {

}
