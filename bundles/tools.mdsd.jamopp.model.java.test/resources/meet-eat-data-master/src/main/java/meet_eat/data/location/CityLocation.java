package meet_eat.data.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.location.service.Geocoding;

import java.util.Objects;

/**
 * Represents a {@link Localizable} location by a city.
 */
public class CityLocation implements Localizable {

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_CITY_NAME = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "cityName");
    private static final String UNLOCALIZABLE_CITY = "The given city is not localizable.";

    @JsonProperty
    private String cityName;

    /**
     * Creates a city location.
     *
     * @param cityName the city name
     */
    @JsonCreator
    public CityLocation(@JsonProperty("cityName") String cityName) {
        Objects.requireNonNull(cityName, ERROR_MESSAGE_NULL_CITY_NAME);
        this.cityName = convertCityName(cityName);
    }

    /**
     * Gets the city name
     *
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the city name
     *
     * @param cityName the city name
     */
    public void setCityName(String cityName) {
        Objects.requireNonNull(cityName, ERROR_MESSAGE_NULL_CITY_NAME);
        this.cityName = convertCityName(cityName);
    }

    @Override
    @JsonIgnore
    public SphericalPosition getSphericalPosition() throws UnlocalizableException {
        SphericalPosition sphericalPosition = Geocoding.getSphericalPositionFromCityName(cityName);
        if (Objects.isNull(sphericalPosition)) {
            throw new UnlocalizableException(UNLOCALIZABLE_CITY);
        }
        return sphericalPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityLocation that = (CityLocation) o;
        return Objects.equals(cityName, that.cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName);
    }

    private String convertCityName(String cityName) {
        return cityName
                .replaceAll("ü", "ue")
                .replaceAll("ö", "oe")
                .replaceAll("ä", "ae")
                .replaceAll("Ü", "Ue")
                .replaceAll("Ö", "Oe")
                .replaceAll("Ä", "Ae")
                .replaceAll("ß", "ss");
    }
}