package meet_eat.data.predicate.chrono;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.predicate.BooleanOperator;

import java.time.LocalDateTime;

/**
 * Represents an executor of a {@link LocalDateTimeOperation} for {@link LocalDateTime} values.
 */
public class LocalDateTimeOperator extends BooleanOperator<LocalDateTimeOperation, LocalDateTime> {

    private static final long serialVersionUID = -1337704230400134159L;

    /**
     * Creates a new instance of {@link LocalDateTimeOperator}.
     *
     * @param operation      the operation used by the operator
     * @param referenceValue the reference value used within the operation
     */
    @JsonCreator
    public LocalDateTimeOperator(@JsonProperty("operation") LocalDateTimeOperation operation,
                                 @JsonProperty("referenceValue") LocalDateTime referenceValue) {
        super(operation, referenceValue);
    }
}

