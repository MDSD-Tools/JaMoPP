package meet_eat.server.controller;

import com.google.common.collect.Streams;
import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.SubscriptionService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * especially for {@link Subscription} entities.
 */
@RestController
public class SubscriptionController extends EntityController<Subscription, String, SubscriptionService> {

    /**
     * Constructs a new instance of {@link SubscriptionController}.
     *
     * @param entityService   the {@link EntityService} used by this controller
     * @param securityService the {@link SecurityService} used by this controller
     */
    @Lazy
    @Autowired
    public SubscriptionController(SubscriptionService entityService, SecurityService<Subscription> securityService) {
        super(entityService, securityService);
    }

    // GET

    /**
     * Gets all persistent {@link Subscription subscriptions} of an identified user from the persistence layer.
     *
     * @param userIdentifier the identifier of the {@link User} whose subscriptions are requested
     * @param token          the authentication token of the requester
     * @return all requested subscriptions within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.SUBSCRIPTIONS)
    public ResponseEntity<Iterable<Subscription>> getSubscriptionsByUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                                         @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Check if authentication is valid
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isValidAuthentication(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get the subscriptions of the user
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUserIdentifier(userIdentifier);
        if (optionalSubscriptions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalSubscriptions.get(), HttpStatus.OK);
    }

    // Post

    /**
     * Posts a new {@link Subscription subscription} into the persistence layer.
     *
     * @param userIdentifier the identifier of the {@link User source user} of the subscription
     * @param subscription   the subscription to be posted
     * @param token          the authentication token of the requester
     * @return the posted subscription within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.SUBSCRIPTIONS)
    public ResponseEntity<Subscription> postSubscription(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                         @RequestBody Subscription subscription,
                                                         @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!userIdentifier.equals(subscription.getSource().getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return handlePost(subscription, token);
    }

    // Delete

    /**
     * Deletes a {@link Subscription subscription} from the persistence layer.
     *
     * @param userIdentifier the {@link User source user} of the subscription
     * @param subscribedUser the {@link User target user} of the subscription
     * @param token          the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.SUBSCRIPTIONS)
    public ResponseEntity<Void> deleteSubscriptionBySubscribedUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                                   @RequestBody User subscribedUser,
                                                                   @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Get subscriptions of identified user
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUserIdentifier(userIdentifier);
        if (optionalSubscriptions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Get the subscription to be deleted
        Optional<Subscription> optionalSubscription = Streams.stream(optionalSubscriptions.get())
                .filter(x -> x.getTarget().equals(subscribedUser))
                .findFirst();

        // Delete if user's subscriptions found and return
        if (optionalSubscription.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return handleDelete(optionalSubscription.get(), token);
    }
}
