package meet_eat.server.repository;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Token} instances.
 */
@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    /**
     * Deletes all {@link Token tokens} containing a given {@link User user}.
     *
     * @param user whose tokens should be deleted
     */
    public void deleteByUser(User user);
}
