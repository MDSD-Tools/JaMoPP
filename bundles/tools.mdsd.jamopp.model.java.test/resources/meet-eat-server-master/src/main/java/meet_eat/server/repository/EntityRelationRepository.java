package meet_eat.server.repository;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.relation.EntityRelation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents a repository managing persistence of {@link EntityRelation} instances.
 *
 * @param <K> the type of the {@link EntityRelation} to be managed
 * @param <T> the type of the {@link Entity source entity} of the {@link EntityRelation}
 * @param <S> the type of the {@link Entity target entity} of the {@link EntityRelation}
 * @param <U> the type of the identifier of the {@link EntityRelation}
 */
public interface EntityRelationRepository<K extends EntityRelation<T, S, U>, T extends Entity<?>, S extends Entity<?>, U extends Serializable> extends MongoRepository<K, U> {

    /**
     * Finds and returns all {@link EntityRelation relations} by their {@link Entity source entity}.
     *
     * @param source the source entity of the relation
     * @return all relations containing a specific source entity
     */
    public Iterable<K> findBySource(T source);

    /**
     * Finds and returns all {@link EntityRelation relations} by their {@link Entity target entity}.
     *
     * @param target the target entity of the relation
     * @return all relations containing a specific target entity
     */
    public Iterable<K> findByTarget(S target);

    /**
     * Finds and returns a {@link EntityRelation relation} by its {@link Entity source entity} and {@link Entity target entity}.
     *
     * @param source the source entity of the relation
     * @param target the target entity of the relation
     * @return the relation containing a specific source and target entity
     */
    public Optional<K> findBySourceAndTarget(T source, S target);

    /**
     * Returns whether a {@link EntityRelation relation} with a specific {@link Entity source entity} and
     * {@link Entity target entity} exists.
     *
     * @param source the source entity of the relation
     * @param target the target entity of the relation
     * @return {@code true} if the relation exists, {@code false} otherwise.
     */
    public boolean existsBySourceAndTarget(T source, S target);

    /**
     * Gets the number of {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations to be counted
     * @return the number of relations containing a specific source entity
     */
    public long countBySource(T source);

    /**
     * Gets the number of {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations to be counted
     * @return the number of relations containing a specific target entity
     */
    public long countByTarget(S target);

    /**
     * Deletes all {@link EntityRelation relations} by a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations
     */
    public void deleteBySource(T source);

    /**
     * Deletes all {@link EntityRelation relations} by a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations
     */
    public void deleteByTarget(S target);

    /**
     * Deletes all {@link EntityRelation relations} by a specific {@link Entity source entity} and
     * {@link Entity target entity}.
     *
     * @param source the source entity of the relations
     * @param target the target entity of the relations
     */
    public void deleteBySourceAndTarget(T source, S target);

    /**
     * Deletes all {@link EntityRelation relations} by a specific {@link Entity source entity} or a specific
     * {@link Entity target entity}.
     *
     * @param source the source entity of the relations
     * @param target the target entity of the relations
     */
    public void deleteBySourceOrTarget(T source, S target);
}
