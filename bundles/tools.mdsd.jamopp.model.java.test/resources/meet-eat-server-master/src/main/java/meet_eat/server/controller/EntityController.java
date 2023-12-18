package meet_eat.server.controller;

import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.server.HeaderPropertyEditor;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an abstract controller class handling incoming RESTful CRUD requests by providing specific endpoints.
 *
 * @param <T> the type of the {@link Entity} mainly managed by this controller
 * @param <U> the type of identifier the mainly managed {@link Entity} uses
 * @param <K> the type of {@link EntityService} used for manipulating the managed {@link Entity}
 */
@RestController
public abstract class EntityController<T extends Entity<U>, U extends Serializable, K extends EntityService<T, U, ? extends MongoRepository<T, U>>> {

    /**
     * Represents an URI path variable for an identifier.
     */
    protected static final String PATH_VARIABLE_IDENTIFIER = "identifier";

    /**
     * Represents an URI path segment containing an identifier variable.
     */
    protected static final String URI_PATH_SEGMENT_IDENTIFIER = "/{" + PATH_VARIABLE_IDENTIFIER + "}";

    private final K entityService;
    private final SecurityService<T> securityService;

    /**
     * Constructs a new instance of {@link EntityController}.
     *
     * @param entityService   the {@link EntityService} used by this controller
     * @param securityService the {@link SecurityService} used by this controller
     */
    protected EntityController(K entityService, SecurityService<T> securityService) {
        this.entityService = entityService;
        this.securityService = securityService;
    }

    /**
     * Handles a basic incoming GET request at the {@link EntityController} endpoints.
     *
     * @param identifier the identifier of the entity to be got
     * @param token      the authentication token of the requester
     * @return a {@link ResponseEntity} containing the status of the request and the got entity on success
     */
    protected ResponseEntity<T> handleGet(U identifier, Token token) {
        if (Objects.isNull(identifier)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<T> optionalEntity = getEntityService().get(identifier);
        if (optionalEntity.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalEntity.get(), HttpStatus.OK);
    }

    /**
     * Handles a basic incoming GET(all) request at the {@link EntityController} endpoints.
     *
     * @param token the authentication token of the requester
     * @return a {@link ResponseEntity} containing the status of the request and the got entities on success
     */
    protected ResponseEntity<Iterable<T>> handleGetAll(Token token) {
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Iterable<T> entities = getEntityService().getAll();
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Handles a basic incoming POST request at the {@link EntityController} endpoints.
     *
     * @param entity the entity to be posted
     * @param token  the authentication token of the requester
     * @return a {@link ResponseEntity} containing the status of the request and the posted entity on success
     */
    protected ResponseEntity<T> handlePost(T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalPost(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (getEntityService().existsPostConflict(entity)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        T postedEntity = getEntityService().post(entity);
        return new ResponseEntity<>(postedEntity, HttpStatus.CREATED);
    }

    /**
     * Handles a basic incoming PUT request at the {@link EntityController} endpoints.
     *
     * @param identifier the identifier of the entity to be put
     * @param entity     the entity to be put
     * @param token      the authentication token of the requester
     * @return a {@link ResponseEntity} containing the status of the request and the put entity on success
     */
    protected ResponseEntity<T> handlePut(U identifier, T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if ((Objects.nonNull(identifier) && !identifier.equals(entity.getIdentifier()))
                || getEntityService().existsPutConflict(entity)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalPut(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        T puttedEntity = getEntityService().put(entity);
        return new ResponseEntity<>(puttedEntity, HttpStatus.OK);
    }

    /**
     * Handles a basic incoming DELETE request at the {@link EntityController} endpoints.
     *
     * @param entity the entity to be deleted
     * @param token  the authentication token of the requester
     * @return a bodiless {@link ResponseEntity} containing the status of the request
     */
    protected ResponseEntity<Void> handleDelete(T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalDelete(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        getEntityService().delete(entity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Handles a basic incoming DELETE request at the {@link EntityController} endpoints.
     *
     * @param identifier the identifier of the entity to be deleted
     * @param token      the authentication token of the requester
     * @return a bodiless {@link ResponseEntity} containing the status of the request
     */
    protected ResponseEntity<Void> handleDelete(U identifier, Token token) {
        if (Objects.isNull(identifier)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Get the optional entity from the service and return appropriately.
        Optional<T> optionalEntity = getEntityService().get(identifier);
        if (optionalEntity.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return handleDelete(optionalEntity.get(), token);
    }

    /**
     * Gets the {@link EntityService} of this {@link EntityController} instance.
     *
     * @return the entity service
     */
    public K getEntityService() {
        return entityService;
    }

    /**
     * Gets the {@link SecurityService} of this {@link EntityController} instance.
     *
     * @return the security service
     */
    public SecurityService<T> getSecurityService() {
        return securityService;
    }

    /**
     * Initializes custom {@link java.beans.PropertyEditor property editors} for serialization and deserialization of
     * specific message types at {@link EntityController} endpoints.
     *
     * @param binder the {@link WebDataBinder} used for editor registration
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Token.class, new HeaderPropertyEditor(Token.class));
        binder.registerCustomEditor(OfferComparator.class, new HeaderPropertyEditor(OfferComparator.class));
        binder.registerCustomEditor(OfferPredicate.class, new HeaderPropertyEditor(OfferPredicate.class));
        binder.registerCustomEditor(OfferPredicate[].class, new HeaderPropertyEditor(OfferPredicate[].class));
    }
}
