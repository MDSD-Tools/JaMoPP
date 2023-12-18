package meet_eat.server.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.OfferService;
import meet_eat.server.service.security.OfferSecurityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link Offer} entities.
 */
@RestController
public class OfferController extends EntityController<Offer, String, OfferService> {

    /**
     * Represents a request parameter descriptor for an {@link Offer offer's} owner.
     */
    protected static final String REQUEST_PARAM_OWNER = "owner";

    /**
     * Represents a request parameter descriptor for a {@link User subscriber}.
     */
    protected static final String REQUEST_PARAM_SUBSCRIBER = "subscriber";

    /**
     * Constructs a new instance of {@link OfferController}.
     *
     * @param offerService         the {@link EntityService} used by this controller
     * @param offerSecurityService the {@link SecurityService} used by this controller
     */
    @Autowired
    public OfferController(OfferService offerService, OfferSecurityService offerSecurityService) {
        super(offerService, offerSecurityService);
    }

    // GET

    /**
     * Gets an specific persistent {@link Offer} from the persistence layer.
     *
     * @param identifier the identifier of the offer to be got
     * @param token      the authentication token of the requester
     * @return an specific identified offer entity within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Offer> getOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGet(identifier, token);
    }

    /**
     * Gets all persistent {@link Offer offers} from the persistence layer.
     *
     * @param creatorIdentifier    the identifier of an offer's {@link User creator}
     * @param subscriberIdentifier the identifier of an {@link User user} searching for offers of subscriptions
     * @param predicates           the {@link OfferPredicate predicates} used for filtering the returned offers
     * @param comparator           the {@link OfferComparator comparator} used for sorting the returned offers
     * @param token                the authentication token of the requester
     * @return all available offers filtered and sorted within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.OFFERS)
    public ResponseEntity<Iterable<Offer>> getAllOffers(
            @RequestParam(value = REQUEST_PARAM_OWNER, required = false) String creatorIdentifier,
            @RequestParam(value = REQUEST_PARAM_SUBSCRIBER, required = false) String subscriberIdentifier,
            @RequestHeader(value = RequestHeaderField.PREDICATES, required = false) OfferPredicate[] predicates,
            @RequestHeader(value = RequestHeaderField.COMPARATORS, required = false) OfferComparator comparator,
            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {

        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get all offers (by certain creator if given).
        Iterable<Offer> offers;
        if (Objects.nonNull(creatorIdentifier) || Objects.nonNull(subscriberIdentifier)) {
            // Avoid duplicates for creator and subscriber by using a set.
            Set<Offer> offerSet = new HashSet<>();

            // Get all offers of a certain identified creator
            if (Objects.nonNull(creatorIdentifier)) {
                Optional<Iterable<Offer>> optionalOffers = getEntityService().getByCreatorId(creatorIdentifier);
                if (optionalOffers.isEmpty()) {
                    // Indicating that the given creatorId does not exist in the user repository.
                    // Therefore, no resource could be found.
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                offerSet.addAll(Lists.newLinkedList(optionalOffers.get()));
            }

            // Get all offers of users subscribed by the identified "subscriber" user
            if (Objects.nonNull(subscriberIdentifier)) {
                Optional<Iterable<Offer>> optionalOffers = getEntityService().getBySubscriberIdentifier(subscriberIdentifier);
                if (optionalOffers.isEmpty()) {
                    // Analogous to the non-existence of a creator.
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                offerSet.addAll(Lists.newLinkedList(optionalOffers.get()));
            }

            // Write offers back to iterable
            offers = offerSet;
        } else {
            offers = getEntityService().getAll();
        }

        // Filter the offers with given predicates.
        if (Objects.nonNull(predicates)) {
            offers = filterOffers(offers, predicates);
        }

        // Sort the offers with a given comparator
        if (Objects.nonNull(comparator)) {
            comparator.setHostRatingGetter(getEntityService()::getNumericHostRating);
            comparator.setParticipantAmountGetter(getEntityService()::getParticipationAmount);
            List<Offer> offerList = Lists.newArrayList(offers);
            offerList.sort(comparator);
            offers = offerList;
        }

        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    // POST

    /**
     * Posts a new {@link Offer offer} into the persistence layer.
     *
     * @param offer the offer to be posted
     * @param token the authentication token of the requester
     * @return the posted offer within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.OFFERS)
    public ResponseEntity<Offer> postOffer(@RequestBody Offer offer,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(offer, token);
    }

    // PUT

    /**
     * Puts a modified {@link Offer offer} into the persistence layer.
     *
     * @param offer the offer to be put
     * @param token the authentication token of the requester
     * @return the put offer within a {@link ResponseEntity}
     */
    @PutMapping(EndpointPath.OFFERS)
    public ResponseEntity<Offer> putOffer(@RequestBody Offer offer,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(null, offer, token);
    }

    /**
     * Puts a modified {@link Offer offer} into the persistence layer.
     *
     * @param identifier the identifier of the offer to be put
     * @param offer      the offer to be put
     * @param token      the authentication token of the requester
     * @return the put offer within a {@link ResponseEntity}
     */
    @PutMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Offer> putOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                          @RequestBody Offer offer,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(identifier, offer, token);
    }

    // DELETE

    /**
     * Deletes an {@link Offer offer} from the persistence layer.
     *
     * @param offer the offer to be deleted
     * @param token the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.OFFERS)
    public ResponseEntity<Void> deleteOffer(@RequestBody Offer offer,
                                            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(offer, token);
    }

    /**
     * Deletes an {@link Offer offer} from the persistence layer.
     *
     * @param identifier the identifier of an offer to be deleted
     * @param token      the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Void> deleteOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(identifier, token);
    }

    /**
     * Filters a given {@link Iterable} of {@link Offer offers}.
     *
     * @param offers     the offers to be filtered
     * @param predicates the {@link OfferPredicate predicates} used for filtering
     * @return the filtered offers
     */
    private Iterable<Offer> filterOffers(Iterable<Offer> offers, OfferPredicate... predicates) {
        Stream<Offer> offerStream = Streams.stream(offers);
        for (OfferPredicate predicate : predicates) {
            predicate.setNumericRatingGetter(getEntityService()::getNumericHostRating);
            predicate.setParticipantAmountGetter(getEntityService()::getParticipationAmount);
            offerStream = offerStream.filter(predicate);
        }
        return offerStream.collect(Collectors.toList());
    }
}
