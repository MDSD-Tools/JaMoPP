package meet_eat.data.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents a {@link Localizable} location by a {@link SphericalPosition}.
 */
public class SphericalLocation implements Localizable {

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_SPHERICAL_POSITION = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "sphericalPosition");

    @JsonProperty
    private SphericalPosition sphericalPosition;

    /**
     * Creates a spherical location.
     *
     * @param sphericalPosition the spherical position
     */
    @JsonCreator
    public SphericalLocation(@JsonProperty("sphericalPosition") SphericalPosition sphericalPosition) {
        this.sphericalPosition = Objects.requireNonNull(sphericalPosition, ERROR_MESSAGE_NULL_SPHERICAL_POSITION);
    }

    @Override
    public SphericalPosition getSphericalPosition() {
        return sphericalPosition;
    }

    /**
     * Sets the spherical position.
     *
     * @param sphericalPosition the spherical position
     */
    public void setSphericalPosition(SphericalPosition sphericalPosition) {
        this.sphericalPosition = Objects.requireNonNull(sphericalPosition, ERROR_MESSAGE_NULL_SPHERICAL_POSITION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SphericalLocation that = (SphericalLocation) o;
        return Objects.equals(sphericalPosition, that.sphericalPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sphericalPosition);
    }
}