package meet_eat.data.predicate.chrono;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.predicate.OfferPredicate;

import java.time.LocalDateTime;

/**
 * Represents an {@link OfferPredicate} for the date and time of an {@link Offer}.
 */
public class LocalDateTimePredicate extends LocalDateTimeOperator implements OfferPredicate {

    private static final long serialVersionUID = 7062600211366077108L;

    /**
     * Creates a local date time predicate.
     *
     * @param operation      the operation
     * @param referenceValue the reference value
     */
    @JsonCreator
    public LocalDateTimePredicate(@JsonProperty("operation") LocalDateTimeOperation operation,
                                  @JsonProperty("referenceValue") LocalDateTime referenceValue) {
        super(operation, referenceValue);
    }

    @Override
    public boolean test(Offer offer) {
        return operate(offer.getDateTime());
    }
}
