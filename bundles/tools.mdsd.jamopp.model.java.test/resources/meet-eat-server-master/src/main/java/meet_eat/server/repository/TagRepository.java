package meet_eat.server.repository;

import meet_eat.data.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Tag} instances.
 */
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

}
