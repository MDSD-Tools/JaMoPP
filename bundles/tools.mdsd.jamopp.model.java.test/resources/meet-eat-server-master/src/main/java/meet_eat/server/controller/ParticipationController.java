package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Participation;
import meet_eat.server.service.ParticipationService;
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
 * especially for {@link Participation} entities.
 */
@RestController
public class ParticipationController extends EntityController<Participation, String, ParticipationService> {

    /**
     * Constructs a new instance of {@link ParticipationController}.
     *
     * @param entityService   the {@link ParticipationService} used by this controller
     * @param securityService the {@link SecurityService} used by this controller
     */
    @Lazy
    @Autowired
    public ParticipationController(ParticipationService entityService, SecurityService<Participation> securityService) {
        super(entityService, securityService);
    }

    // GET

    /**
     * Gets all persistent {@link Participation participations} of an identified {@link Offer} from the persistence layer.
     *
     * @param offerIdentifier the identifier of the {@link Offer offer} of the participations
     * @param token           the authentication token of the requester
     * @return all requested participations within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.PARTICIPATIONS)
    public ResponseEntity<Iterable<Participation>> getParticipationsByOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String offerIdentifier,
                                                                            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Iterable<Participation>> optionalParticipations = getEntityService().getByOfferIdentifier(offerIdentifier);
        if (optionalParticipations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalParticipations.get(), HttpStatus.OK);
    }

    // POST

    /**
     * Posts a new {@link Participation participation} into the persistence layer.
     *
     * @param offerIdentifier the identifier of the {@link Offer offer} of the participation
     * @param participation   the participation to be posted
     * @param token           the authentication token of the requester
     * @return the posted participation within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.PARTICIPATIONS)
    public ResponseEntity<Participation> postParticipation(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String offerIdentifier,
                                                           @RequestBody Participation participation,
                                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!Objects.equals(participation.getTarget().getIdentifier(), offerIdentifier)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Optional<Boolean> optionalCanParticipate = getEntityService().canParticipate(offerIdentifier);
        if (optionalCanParticipate.isEmpty()) {
            // Indicates that no offer with the given offer identifier was found.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!optionalCanParticipate.get()) {
            // Indicates that the offer is already full and participating is not possible.
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return handlePost(participation, token);
    }

    // DELETE

    /**
     * Deletes a {@link Participation participation} from the persistence layer.
     *
     * @param offerIdentifier the identifier of the {@link Offer offer} of the participation
     * @param participation   the participation to be deleted
     * @param token           the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.PARTICIPATIONS)
    public ResponseEntity<Void> deleteParticipation(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String offerIdentifier,
                                                    @RequestBody Participation participation,
                                                    @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!Objects.equals(participation.getTarget().getIdentifier(), offerIdentifier)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return handleDelete(participation, token);
    }
}
