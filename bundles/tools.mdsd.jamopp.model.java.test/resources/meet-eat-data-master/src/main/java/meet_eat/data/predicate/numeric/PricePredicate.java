package meet_eat.data.predicate.numeric;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.predicate.OfferPredicate;

/**
 * Represents an {@link OfferPredicate} for the price of an {@link Offer}.
 */
public class PricePredicate extends DoubleOperator implements OfferPredicate {

    private static final long serialVersionUID = -9147038172467909868L;

    /**
     * Creates a price predicate.
     *
     * @param operation      the operation
     * @param referenceValue the reference value
     */
    @JsonCreator
    public PricePredicate(@JsonProperty("operation") DoubleOperation operation,
                          @JsonProperty("referenceValue") Double referenceValue) {
        super(operation, referenceValue);
    }

    @Override
    public boolean test(Offer offer) {
        return operate(offer.getPrice());
    }
}
