package meet_eat.data.predicate.localizable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.location.UnlocalizableException;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.numeric.DoubleOperation;
import meet_eat.data.predicate.numeric.DoubleOperator;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Objects;

/**
 * Represents an {@link OfferPredicate} for the {@link Localizable} location of an {@link Offer}.
 */
public class LocalizablePredicate extends DoubleOperator implements OfferPredicate {

    private static final long serialVersionUID = -2939257528922805161L;

    @JsonProperty
    private final Localizable localizable;

    /**
     * Constructs a new instance of {@link LocalizablePredicate}.
     *
     * @param operation      the operation used for testing a certain object
     * @param referenceValue the distance used as reference value within the operation
     * @param localizable    the localizable from which the actual distance is going to be calculated from
     * @throws UnlocalizableException if the given localizable is not localizable
     */
    @JsonCreator
    @PersistenceConstructor
    public LocalizablePredicate(@JsonProperty("operation") DoubleOperation operation,
                                @JsonProperty("referenceValue") Double referenceValue,
                                @JsonProperty("localizable") Localizable localizable) throws UnlocalizableException {
        super(operation, referenceValue);

        // "Casting" the localizable to a spherical location decreases the number of needed geocoding operations.
        SphericalPosition sphericalPosition = Objects.requireNonNull(localizable).getSphericalPosition();
        this.localizable = new SphericalLocation(sphericalPosition);
    }

    @Override
    public boolean test(Offer offer) {
        try {
            return operate(localizable.getDistance(offer.getLocation()));
        } catch (UnlocalizableException exception) {
            return false;
        }
    }

    @JsonGetter
    public Localizable getLocalizable() {
        return localizable;
    }
}
