package meet_eat.data.predicate.string;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.predicate.OfferPredicate;

/**
 * Represents an {@link OfferPredicate} for the name of an {@link Offer}.
 */
public class NamePredicate extends StringOperator implements OfferPredicate {

    private static final long serialVersionUID = 2559024886121992811L;

    /**
     * Creates a name predicate.
     *
     * @param operation      the operation
     * @param referenceValue the reference value
     */
    @JsonCreator
    public NamePredicate(@JsonProperty("operation") StringOperation operation,
                         @JsonProperty("referenceValue") String referenceValue) {
        super(operation, referenceValue);
    }

    @Override
    public boolean test(Offer offer) {
        return operate(offer.getName());
    }
}
