package meet_eat.data.predicate.numeric;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.predicate.BooleanOperator;

/**
 * Represents an executor of a {@link DoubleOperation} for {@link Double} values.
 */
public class DoubleOperator extends BooleanOperator<DoubleOperation, Double> {

    private static final long serialVersionUID = 5754645240999500816L;

    /**
     * Creates the double operator.
     *
     * @param operation      the double operation
     * @param referenceValue the double reference value
     */
    @JsonCreator
    public DoubleOperator(@JsonProperty("operation") DoubleOperation operation,
                          @JsonProperty("referenceValue") Double referenceValue) {
        super(operation, referenceValue);
    }
}
