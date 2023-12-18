package meet_eat.data.predicate.collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.predicate.BooleanOperator;

import java.util.Collection;

/**
 * Represents an executor of a {@link CollectionOperation} for {@link Collection} values.
 */
public class CollectionOperator extends BooleanOperator<CollectionOperation, Collection<?>> {

    private static final long serialVersionUID = 5853113823441138515L;

    /**
     * Creates the collection operator.
     *
     * @param operation      the collection operation
     * @param referenceValue the collection reference value
     */
    @JsonCreator
    public CollectionOperator(@JsonProperty("operation") CollectionOperation operation,
                              @JsonProperty("referenceValue") Collection<?> referenceValue) {
        super(operation, referenceValue);
    }
}
