package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.relation.EntityRelation;
import meet_eat.server.repository.EntityRelationRepository;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a service class providing functionality to manage {@link EntityRelation entity relations} and their state
 * persistence.
 *
 * @param <K> the type of the {@link EntityRelation entity relation}
 * @param <T> the type of the {@link Entity source entity} within the relation
 * @param <S> the type of the {@link Entity target entity} within the relation
 * @param <U> the type of the {@link EntityRelation entity relation} identifier
 * @param <R> the type of the {@link EntityRelation entity relation} {@link EntityRelationRepository repository}
 */
public abstract class EntityRelationService<K extends EntityRelation<T, S, U>, T extends Entity<?>,
        S extends Entity<?>, U extends Serializable, R extends EntityRelationRepository<K, T, S, U>>
        extends EntityService<K, U, R> {

    /**
     * Constructs a new instance of {@link EntityRelationService}.
     *
     * @param repository the repository used for persistence operations
     */
    protected EntityRelationService(R repository) {
        super(repository);
    }

    /**
     * Gets all {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations to be returned
     * @return all relations containing a specific source entity
     */
    public Iterable<K> getBySource(T source) {
        return getRepository().findBySource(Objects.requireNonNull(source));
    }

    /**
     * Gets all {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations to be returned
     * @return all relations containing a specific target entity
     */
    public Iterable<K> getByTarget(S target) {
        return getRepository().findByTarget(Objects.requireNonNull(target));
    }

    /**
     * Gets the number of {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations to be counted
     * @return the number of relations containing a specific source entity
     */
    public long countBySource(T source) {
        return getRepository().countBySource(Objects.requireNonNull(source));
    }

    /**
     * Gets the number of {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations to be counted
     * @return the number of relations containing a specific target entity
     */
    public long countByTarget(S target) {
        return getRepository().countByTarget(Objects.requireNonNull(target));
    }

    /**
     * Deletes all {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations to be deleted
     */
    public void deleteBySource(T source) {
        getRepository().deleteBySource(Objects.requireNonNull(source));
    }

    /**
     * Deletes all {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations to be deleted
     */
    public void deleteByTarget(S target) {
        getRepository().deleteByTarget(Objects.requireNonNull(target));
    }

    /**
     * Deletes a {@link EntityRelation relation} containing a specific {@link Entity source entity} and
     * {@link Entity target entity}.
     *
     * @param source the source entity of the relation to be deleted
     * @param target the target entity of the relation to be deleted
     */
    public void deleteBySourceAndTarget(T source, S target) {
        getRepository().deleteBySourceAndTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    /**
     * Deletes a {@link EntityRelation relation} containing a specific {@link Entity source entity} or
     * {@link Entity target entity}.
     *
     * @param source the source entity of the relation to be deleted
     * @param target the target entity of the relation to be deleted
     */
    public void deleteBySourceOrTarget(T source, S target) {
        getRepository().deleteBySourceOrTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    /**
     * Returns whether a {@link EntityRelation relation} containing a specific {@link Entity source entity} and
     * {@link Entity target entity} exists.
     *
     * @param source the source entity of the relation
     * @param target the target entity of the relation
     * @return {@code true} if the relation exists, {@code false} otherwise.
     */
    public boolean existsBySourceAndTarget(T source, S target) {
        return getRepository().existsBySourceAndTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }
}
