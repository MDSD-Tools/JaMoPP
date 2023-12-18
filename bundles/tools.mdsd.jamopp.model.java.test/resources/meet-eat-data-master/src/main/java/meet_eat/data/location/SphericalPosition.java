package meet_eat.data.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents the structure of a two-dimensional position by latitude and longitude.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SphericalPosition {

    private static final String ERROR_MESSAGE_ILLEGAL_COORDINATES = "Coordinate values out of bounds.";
    private static final int LAT_START = -90;
    private static final int LAT_END = 90;
    private static final int LON_START = -180;
    private static final int LON_END = 180;

    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lon")
    private double longitude;

    /**
     * Creates a spherical position.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    @JsonCreator
    public SphericalPosition(@JsonProperty("lat") double latitude,
                             @JsonProperty("lon") double longitude) {
        if (latitude < LAT_START || latitude > LAT_END || longitude < LON_START || longitude > LON_END) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_COORDINATES);
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the radian value of the latitude.
     *
     * @return the radian value of the latitude
     */
    @JsonIgnore
    public double getLatitudeAsRadians() {
        return Math.toRadians(latitude);
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the radian value of the longitude.
     *
     * @return the radian value of the longitude
     */
    @JsonIgnore
    public double getLongitudeAsRadians() {
        return Math.toRadians(longitude);
    }

    /**
     * Sets the latitude
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SphericalPosition that = (SphericalPosition) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}