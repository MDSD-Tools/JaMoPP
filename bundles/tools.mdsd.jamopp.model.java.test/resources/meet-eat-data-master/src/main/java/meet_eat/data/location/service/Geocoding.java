package meet_eat.data.location.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import meet_eat.data.location.SphericalPosition;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * This class consists exclusively of static methods implementing geocoding functionality.
 */
public final class Geocoding {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/%s?format=json&limit=1%s";
    private static final String SEARCH_OPERATION = "search";
    private static final String PARAMETER_POSTCODE = "&postalcode=%s";
    private static final String PARAMETER_CITY = "&city=%s";

    private Geocoding() {
    }

    /**
     * Gets a {@link SphericalPosition} from a postcode.
     *
     * @param postcode the postcode
     * @return the spherical position
     */
    public static SphericalPosition getSphericalPositionFromPostcode(String postcode) {
        String params = String.format(PARAMETER_POSTCODE, postcode);
        String url = String.format(BASE_URL, SEARCH_OPERATION, params);
        HttpMessageConverter<?> messageConverter = getMessageConverterWithEnabledDeserializationFeatures(
                DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);

        return getForObject(url, SphericalPosition.class, messageConverter);
    }

    /**
     * Gets a {@link SphericalPosition} from a city name.
     *
     * @param cityName the city name
     * @return the spherical position
     */
    public static SphericalPosition getSphericalPositionFromCityName(String cityName) {
        String params = String.format(PARAMETER_CITY, cityName);
        String url = String.format(BASE_URL, SEARCH_OPERATION, params);
        HttpMessageConverter<?> messageConverter = getMessageConverterWithEnabledDeserializationFeatures(
                DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);

        return getForObject(url, SphericalPosition.class, messageConverter);
    }

    private static <T> T getForObject(String url, Class<T> responseType, HttpMessageConverter<?>... messageConverters) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Arrays.asList(messageConverters));
        return restTemplate.getForObject(url, responseType);
    }

    private static MappingJackson2HttpMessageConverter getMessageConverterWithEnabledDeserializationFeatures(
            DeserializationFeature... features) {

        ObjectMapper objectMapper = new ObjectMapper();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        for (DeserializationFeature feature : features) {
            objectMapper.enable(feature);
        }
        return messageConverter;
    }
}