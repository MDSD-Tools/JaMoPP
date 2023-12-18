package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Entity entities} and their state persistence.
 *
 * @param <T> the specific entity type this service provides functionality for
 * @param <U> the type of identifier the managed entity uses
 * @param <K> the repository type to manage entity persistence
 */
@Service
public abstract class EntityService<T extends Entity<U>, U extends Serializable, K extends MongoRepository<T, U>> {

    private final K repository;

    /**
     * Constructs a new instance of {@link EntityService}.
     *
     * @param repository the repository used for persistence operations
     */
    protected EntityService(K repository) {
        this.repository = repository;
    }

    /**
     * Signalizes whether an {@link Entity} exists for a certain identifier.
     *
     * @param identifier the identifier to check for an entity's existence
     * @return {@code true} if an entity with the given identifier exists, {@code false} otherwise.
     */
    public boolean exists(U identifier) {
        return Objects.nonNull(identifier) && repository.existsById(identifier);
    }

    /**
     * Gets all existing {@link Entity entities}.
     *
     * @return all existing entities.
     */
    public Iterable<T> getAll() {
        return repository.findAll();
    }

    /**
     * Gets a specific {@link Entity} identified by the identifier.
     *
     * @param identifier the identifier used for finding a certain entity
     * @return a certain identified entity.
     */
    public Optional<T> get(U identifier) {
        return repository.findById(Objects.requireNonNull(identifier));
    }

    /**
     * Adds an {@link Entity} to the repository.
     *
     * @param entity the entity to be added
     * @return the exact entity added to the repository.
     */
    public T post(T entity) {
        Objects.requireNonNull(entity);
        if (existsPostConflict(entity)) {
            throw new EntityConflictException();
        }
        return repository.insert(entity);
    }

    /**
     * Modifies an {@link Entity} within the repository.
     *
     * @param entity the entity to be modified
     * @return the exact entity modified within the repository.
     */
    public T put(T entity) {
        Objects.requireNonNull(entity);
        if (existsPutConflict(entity)) {
            throw new EntityConflictException();
        }
        return repository.save(entity);
    }

    /**
     * Deletes a specific {@link Entity} from the repository.
     *
     * @param entity the entity to be deleted
     */
    public void delete(T entity) {
        repository.delete(Objects.requireNonNull(entity));
    }

    /**
     * Deletes a specific {@link Entity} identified by identifier from the repository.
     *
     * @param identifier the identifier of the entity to be deleted
     */
    public void delete(U identifier) {
        repository.deleteById(Objects.requireNonNull(identifier));
    }

    /**
     * Gets the entity repository of this service.
     *
     * @return the entity repository.
     */
    public K getRepository() {
        return repository;
    }

    /**
     * Signalizes whether or not an existing {@link Entity} conflicts with a to be created one.
     *
     * @param entity the entity to be created
     * @return {@code true} if an conflict exists, {@code false} otherwise.
     */
    public boolean existsPostConflict(T entity) {
        return exists(entity.getIdentifier());
    }

    /**
     * Signalizes whether or not an existing {@link Entity} conflicts with a to be modified one.
     *
     * @param entity the entity to be modified
     * @return {@code true} if an conflict exists, {@code false} otherwise.
     */
    public boolean existsPutConflict(T entity) {
        return false;
    }
}
