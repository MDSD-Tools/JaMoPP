package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.OfferRepository;
import meet_eat.server.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Participation participations} and their state
 * persistence.
 */
@Service
public class ParticipationService extends EntityRelationService<Participation, User, Offer, String, ParticipationRepository> {

    private final OfferService offerService;

    /**
     * Constructs a new instance of {@link ParticipationService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public ParticipationService(ParticipationRepository repository, OfferService offerService) {
        super(repository);
        this.offerService = offerService;
    }

    /**
     * Gets all {@link Participation participations} containing a specific {@link Offer offer}.
     * If the given offer identifier cannot be found within the {@link OfferRepository}
     * an empty {@link Optional} is returned.
     *
     * @param offerIdentifier the offer's identifier
     * @return all relations containing a specific offer within an {@link Optional}
     */
    public Optional<Iterable<Participation>> getByOfferIdentifier(String offerIdentifier) {
        Optional<Offer> optionalOffer = offerService.get(offerIdentifier);
        return optionalOffer.map(this::getByTarget);
    }

    /**
     * Returns whether it is possible to participate at an identified {@link Offer offer}.
     * If the given offer identifier cannot be found within the {@link OfferRepository}
     * an empty {@link Optional} is returned.
     *
     * @param offerIdentifier the identifier of the offer
     * @return {@code true} if {@link Participation participation} at a certain {@link Offer offer} is possible,
     * {@code false} otherwise.
     */
    public Optional<Boolean> canParticipate(String offerIdentifier) {
        Optional<Offer> optionalOffer = offerService.get(offerIdentifier);
        if (optionalOffer.isPresent()) {
            Offer offer = optionalOffer.get();
            long participantAmount = getRepository().countByTarget(offer);
            return Optional.of(participantAmount < offer.getMaxParticipants());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsPostConflict(Participation entity) {
        return getRepository().existsBySourceAndTarget(entity.getSource(), entity.getTarget())
                || super.existsPostConflict(entity);
    }
}
