package meet_eat.data.predicate.numeric;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.data.predicate.OfferPredicate;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents an {@link OfferPredicate} for the host rating of the {@link User} who created the {@link Offer}.
 */
public class RatingPredicate extends DoubleOperator implements OfferPredicate {

    private static final long serialVersionUID = -836110461118942920L;
    private static final String ERROR_MESSAGE_NULL_FUNCTION = "A function must be set before using this Operator.";

    @JsonIgnore
    private Function<Offer, Double> numericRatingGetter;

    /**
     * Creates a rating predicate
     *
     * @param operation      the operation
     * @param referenceValue the reference value
     */
    @JsonCreator
    public RatingPredicate(@JsonProperty("operation") DoubleOperation operation,
                           @JsonProperty("referenceValue") Double referenceValue) {
        super(operation, referenceValue);
    }

    @Override
    public boolean test(Offer offer) {
        if (Objects.nonNull(numericRatingGetter)) {
            return operate(numericRatingGetter.apply(offer));
        }
        throw new IllegalStateException(ERROR_MESSAGE_NULL_FUNCTION);
    }

    @JsonIgnore
    @Override
    public void setNumericRatingGetter(Function<Offer, Double> numericRatingGetter) {
        this.numericRatingGetter = numericRatingGetter;
    }
}
