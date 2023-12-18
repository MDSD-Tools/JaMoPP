package meet_eat.data.comparator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Implements the {@link Comparator} functionality for {@link Offer} objects.
 */
public class OfferComparator implements Serializable, Comparator<Offer> {

    private static final long serialVersionUID = 3846969499567330524L;
    private static final String ERROR_MESSAGE_NULL_FUNCTION = "A function must be set before using this operation type";

    @JsonProperty
    private final OfferComparableField field;
    @JsonProperty
    private final Localizable location;
    @JsonIgnore
    private Function<Offer, Double> hostRatingGetter;
    @JsonIgnore
    private Function<Offer, Integer> participantAmountGetter;

    /**
     * Creates a {@link Comparator} for {@link Offer} object comparison.
     *
     * @param field    the field to be compared
     * @param location the location needed for distance calculations
     */
    @JsonCreator
    public OfferComparator(@JsonProperty("field") OfferComparableField field,
                           @JsonProperty("location") Localizable location) {
        this.field = Objects.requireNonNull(field);
        this.location = Objects.requireNonNull(location);
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    @JsonGetter
    public OfferComparableField getField() {
        return field;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    @JsonGetter
    public Localizable getLocation() {
        return location;
    }

    @Override
    public int compare(Offer offerFst, Offer offerSnd) {
        switch (field) {
            case TIME:
                return offerFst.getDateTime().compareTo(offerSnd.getDateTime());
            case PRICE:
                return Double.compare(offerFst.getPrice(), offerSnd.getPrice());
            case RATING:
                if (Objects.nonNull(hostRatingGetter)) {
                    return Double.compare(hostRatingGetter.apply(offerFst), hostRatingGetter.apply(offerSnd));
                }
                throw new IllegalStateException(ERROR_MESSAGE_NULL_FUNCTION);
            case PARTICIPANTS:
                if (Objects.nonNull(participantAmountGetter)) {
                    return Integer.compare(participantAmountGetter.apply(offerFst), participantAmountGetter.apply(offerSnd));
                }
                throw new IllegalStateException(ERROR_MESSAGE_NULL_FUNCTION);
            case DISTANCE:
                double distanceFst = getDistance(offerFst.getLocation());
                double distanceSnd = getDistance(offerSnd.getLocation());
                return Double.compare(distanceFst, distanceSnd);
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Calculates the distance between the given {@link OfferComparator#location} and a {@link Localizable} location of an {@link Offer}.
     *
     * @param localizable the offer location
     * @return the distance between the given {@link OfferComparator#location} and the {@link Offer}
     */
    @JsonIgnore
    private double getDistance(Localizable localizable) {
        double distance;
        try {
            distance = location.getDistance(localizable);
        } catch (UnlocalizableException exception) {
            distance = Double.MAX_VALUE;
        }
        return distance;
    }

    /**
     * Sets the used {@link Function function} for getting the {@link RatingBasis host} {@link Rating rating} of an
     * {@link Offer offer's} {@link User creator}.
     *
     * @param hostRatingGetter the {@link Function function} to be used
     */
    @JsonIgnore
    public void setHostRatingGetter(Function<Offer, Double> hostRatingGetter) {
        this.hostRatingGetter = Objects.requireNonNull(hostRatingGetter);
    }

    /**
     * Sets the used {@link Function function} for getting the amount of {@link Participation participations} of an
     * {@link Offer offer}.
     *
     * @param participantAmountGetter the {@link Function function} to be used
     */
    @JsonIgnore
    public void setParticipantAmountGetter(Function<Offer, Integer> participantAmountGetter) {
        this.participantAmountGetter = Objects.requireNonNull(participantAmountGetter);
    }
}
