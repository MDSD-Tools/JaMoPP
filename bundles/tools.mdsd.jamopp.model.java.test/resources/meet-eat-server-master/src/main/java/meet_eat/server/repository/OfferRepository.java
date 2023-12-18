package meet_eat.server.repository;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Offer} instances.
 */
@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    /**
     * Finds and returns an {@link Offer} containing a given {@link User creator}.
     *
     * @param creator the creator of the searched offer
     * @return an offer identified by its creator
     */
    public Iterable<Offer> findByCreator(User creator);

    /**
     * Deletes an {@link Offer} containing a given {@link User creator}.
     *
     * @param creator the creator of the offer to be deleted
     */
    public void deleteByCreator(User creator);
}
