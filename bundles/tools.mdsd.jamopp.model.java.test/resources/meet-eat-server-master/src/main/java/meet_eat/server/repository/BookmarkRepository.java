package meet_eat.server.repository;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.user.User;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Bookmark} instances.
 */
@Repository
public interface BookmarkRepository extends EntityRelationRepository<Bookmark, User, Offer, String> {

}
