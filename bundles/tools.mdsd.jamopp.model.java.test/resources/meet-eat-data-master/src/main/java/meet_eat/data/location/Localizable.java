package meet_eat.data.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import meet_eat.data.location.geometry.Haversine;

/**
 * Represents the localizability of locations.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CityLocation.class),
        @JsonSubTypes.Type(value = SphericalLocation.class),
        @JsonSubTypes.Type(value = PostcodeLocation.class)
})
public interface Localizable {

    /**
     * Gets the {@link SphericalPosition} of a location.
     *
     * @return the spherical position
     * @throws UnlocalizableException if the location is unlocalizable
     */
    @JsonIgnore
    SphericalPosition getSphericalPosition() throws UnlocalizableException;

    /**
     * Calculates the distance between two locations.
     *
     * @param localizable the location to which the distance is calculated
     * @return the distance between the user location and the entered location information.
     * @throws UnlocalizableException if the location is unlocalizable
     */
    @JsonIgnore
    default double getDistance(Localizable localizable) throws UnlocalizableException {
        return Haversine.applyHaversineFormula(this.getSphericalPosition(), localizable.getSphericalPosition());
    }
}