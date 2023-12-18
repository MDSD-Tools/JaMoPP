package meet_eat.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import meet_eat.data.entity.relation.EntityRelation;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an identifiable entity.
 *
 * @param <U> the type of the identifier
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Tag.class),
        @JsonSubTypes.Type(value = Token.class),
        @JsonSubTypes.Type(value = EntityRelation.class)
})
public abstract class Entity<U extends Serializable> implements Serializable {
    
    @JsonProperty
    @Id
    private final U identifier;

    /**
     * Creates an entity with a {@code null} identifier.
     */
    protected Entity() {
        this.identifier = null;
    }

    /**
     * Creates an entity.
     *
     * @param identifier the entity identifier
     */
    @JsonCreator
    protected Entity(@JsonProperty("identifier") U identifier) {
        this.identifier = identifier;
    }

    /**
     * Gets the identifier.
     *
     * @return the identifier
     */
    @JsonGetter
    public U getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(identifier, entity.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}