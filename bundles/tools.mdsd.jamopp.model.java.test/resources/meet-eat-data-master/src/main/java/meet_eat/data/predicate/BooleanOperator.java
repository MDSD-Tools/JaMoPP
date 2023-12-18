package meet_eat.data.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Represents an executor of a specific {@link BiFunction} returning {@code boolean}.
 *
 * @param <T> the type of the operation representing a {@link BiFunction}
 * @param <S> the type of the reference value used for given inputs
 */
public abstract class BooleanOperator<T extends BiFunction<S, S, Boolean>, S> implements Serializable {

    @JsonProperty
    private final T operation;
    @JsonProperty
    private final S referenceValue;

    /**
     * Creates a specific operator.
     *
     * @param operation      the specific {@link BiFunction} operation used for testing a certain object
     * @param referenceValue the specific object used as reference value within the operation
     */
    @JsonCreator
    protected BooleanOperator(@JsonProperty("operation") T operation,
                              @JsonProperty("referenceValue") S referenceValue) {
        this.operation = Objects.requireNonNull(operation);
        this.referenceValue = Objects.requireNonNull(referenceValue);
    }

    /**
     * Gets the operation.
     *
     * @return the operation
     */
    @JsonGetter
    public T getOperation() {
        return operation;
    }

    /**
     * Gets the reference value.
     *
     * @return the reference value
     */
    @JsonGetter
    public S getReferenceValue() {
        return referenceValue;
    }

    /**
     * Applies the given {@link BooleanOperator#operation} on the given {@link BooleanOperator#referenceValue}.
     *
     * @param value the value to be compared with the reference value
     * @return {@code true} if the {@link BiFunction} operation returns {@code true}, {@code false} otherwise
     */
    public Boolean operate(S value) {
        return operation.apply(referenceValue, Objects.requireNonNull(value));
    }
}
