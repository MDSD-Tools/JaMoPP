package meet_eat.data.predicate.collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.predicate.OfferPredicate;

import java.util.Collection;

/**
 * Represents an {@link OfferPredicate} for the {@link Tag}s of an {@link Offer}.
 */
public class TagPredicate extends CollectionOperator implements OfferPredicate {

    private static final long serialVersionUID = 5677783409137877370L;

    /**
     * Creates a tag predicate.
     *
     * @param operation      the operation
     * @param referenceValue the reference value
     */
    @JsonCreator
    public TagPredicate(@JsonProperty("operation") CollectionOperation operation,
                        @JsonProperty("referenceValue") Collection<Tag> referenceValue) {
        super(operation, referenceValue);
    }

    @Override
    public boolean test(Offer offer) {
        return operate(offer.getTags());
    }
}
