package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.RatingService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link Rating} entities.
 */
@RestController
public class RatingController extends EntityController<Rating, String, RatingService> {

    /**
     * Constructs a new instance of {@link RatingController}.
     *
     * @param entityService   the {@link RatingService} used by this controller
     * @param securityService the {@link SecurityService} used by this controller
     */
    @Lazy
    @Autowired
    public RatingController(RatingService entityService, SecurityService<Rating> securityService) {
        super(entityService, securityService);
    }

    // GET

    /**
     * Gets the averaged value of the {@link RatingBasis#HOST host} {@link Rating ratings} of a given {@link User user}.
     *
     * @param userIdentifier the identifier of the rated {@link User user}
     * @param token          the authentication token of the requester
     * @return the averaged host rating of a user
     */
    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.RATINGS + EndpointPath.HOST)
    public ResponseEntity<Double> getHostRatingValue(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                     @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGetRatingValue(userIdentifier, token, RatingBasis.HOST);
    }

    /**
     * Gets the averaged value of the {@link RatingBasis#GUEST guest} {@link Rating ratings} of a given
     * {@link User user}.
     *
     * @param userIdentifier the identifier of the rated {@link User user}
     * @param token          the authentication token of the requester
     * @return the averaged guest rating of a user
     */
    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.RATINGS + EndpointPath.GUEST)
    public ResponseEntity<Double> getGuestRatingValue(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGetRatingValue(userIdentifier, token, RatingBasis.GUEST);
    }

    /**
     * Handles an incoming GET request for a rating value at the {@link RatingController} endpoints.
     *
     * @param userIdentifier the identifier of the rated {@link User user}
     * @param token          the {@link Token authentication token} of the requester
     * @param ratingBasis    the {@link RatingBasis rating basis} of the ratings to get the average from
     * @return a {@link ResponseEntity} containing the status of the request and the rating value on success
     */
    private ResponseEntity<Double> handleGetRatingValue(String userIdentifier, Token token, RatingBasis ratingBasis) {
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Double> optionalRatingValue = getEntityService().getRatingValue(userIdentifier, ratingBasis);
        if (optionalRatingValue.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalRatingValue.get(), HttpStatus.OK);
    }

    // POST

    /**
     * Posts a new {@link Rating rating} into the persistence layer.
     *
     * @param userIdentifier the identifier of the rated {@link User user}
     * @param rating         the rating to be posted
     * @param token          the authentication token of the requester
     * @return the posted rating within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.RATINGS)
    public ResponseEntity<Rating> postRating(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                             @RequestBody Rating rating,
                                             @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!Objects.equals(rating.getTarget().getIdentifier(), userIdentifier)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return handlePost(rating, token);
    }
}
