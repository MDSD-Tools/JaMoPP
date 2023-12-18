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
 * Represents an {@link OfferPredicate} for the number of {@link User}s participating in an {@link Offer}.
 */
public class ParticipantsPredicate extends DoubleOperator implements OfferPredicate {

    private static final long serialVersionUID = 3784910666230134079L;
    private static final String ERROR_MESSAGE_NULL_FUNCTION = "A function must be set before using this Operator.";

    @JsonIgnore
    private Function<Offer, Integer> participantAmountGetter;

    /**
     * Creates a participants predicate.
     *
     * @param operation      the operation
     * @param referenceValue the reference value
     */
    @JsonCreator
    public ParticipantsPredicate(@JsonProperty("operation") DoubleOperation operation,
                                 @JsonProperty("referenceValue") Double referenceValue) {
        super(operation, referenceValue);
    }

    @Override
    public boolean test(Offer offer) {
        if (Objects.nonNull(participantAmountGetter)) {
            return operate((double) participantAmountGetter.apply(offer));
        }
        throw new IllegalStateException(ERROR_MESSAGE_NULL_FUNCTION);
    }

    @JsonIgnore
    @Override
    public void setParticipantAmountGetter(Function<Offer, Integer> participantAmountGetter) {
        this.participantAmountGetter = participantAmountGetter;
    }
}
