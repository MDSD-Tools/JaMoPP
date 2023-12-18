package meet_eat.server.repository;

import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents a repository managing persistence of {@link User} instances.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds and returns an {@link User} by {@link Email}.
     *
     * @param email the email of the searched user
     * @return a user identified by email
     */
    Optional<User> findOneByEmail(Email email);
}
