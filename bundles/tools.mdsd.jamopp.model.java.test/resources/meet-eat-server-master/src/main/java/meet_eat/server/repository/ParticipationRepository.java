package meet_eat.server.repository;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Participation} instances.
 */
@Repository
public interface ParticipationRepository extends EntityRelationRepository<Participation, User, Offer, String> {

}
