package meet_eat.data.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.location.service.Geocoding;

import java.util.Objects;

/**
 * Represents a {@link Localizable} location by a postcode.
 */
public class PostcodeLocation implements Localizable {

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_POSTCODE = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "postcode");
    private static final String UNLOCALIZABLE_POSTCODE = "The given postcode is not localizable.";

    @JsonProperty
    private String postcode;

    /**
     * Creates a postcode location.
     *
     * @param postcode the postcode
     */
    @JsonCreator
    public PostcodeLocation(@JsonProperty("postcode") String postcode) {
        this.postcode = Objects.requireNonNull(postcode, ERROR_MESSAGE_NULL_POSTCODE);
    }

    /**
     * Gets the postcode.
     *
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Sets the postcode.
     *
     * @param postcode the postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = Objects.requireNonNull(postcode, ERROR_MESSAGE_NULL_POSTCODE);
    }

    @Override
    @JsonIgnore
    public SphericalPosition getSphericalPosition() throws UnlocalizableException {
        SphericalPosition sphericalPosition = Geocoding.getSphericalPositionFromPostcode(postcode);
        if (Objects.isNull(sphericalPosition)) {
            throw new UnlocalizableException(UNLOCALIZABLE_POSTCODE);
        }
        return sphericalPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostcodeLocation that = (PostcodeLocation) o;
        return Objects.equals(postcode, that.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postcode);
    }
}