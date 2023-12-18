package meet_eat.data.location.service;

import static org.junit.Assert.*;

import meet_eat.data.location.CityLocation;
import meet_eat.data.location.PostcodeLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.location.UnlocalizableException;
import org.junit.Test;

public class GeocodingGetSphericalPositionTest {

    // One arcminute equals to 1/60 (arc) degrees and approximately 1852 meters on earth's great circle.
    private static final double ARC_MINUTE = 1d/60d;
    // One arcsecond equals to 1/3600 (arc) degrees and approximately 30 meters on earth's great circle.
    private static final double ARC_SECOND = 1d/3600d;

    @Test
    public void testGetSphericalPositionOfKarlsruhe() throws UnlocalizableException {
        // Test data
        double expectedLatitude = 49 + 1 * ARC_MINUTE;
        double expectedLongitude = 8 + 24 * ARC_MINUTE;
        CityLocation cityLocation = new CityLocation("Karlsruhe");

        // Execution
        SphericalPosition sphericalPosition = cityLocation.getSphericalPosition();

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(expectedLatitude, sphericalPosition.getLatitude(), ARC_MINUTE);
        assertEquals(expectedLongitude, sphericalPosition.getLongitude(), ARC_MINUTE);
    }

    @Test
    public void testGetSphericalPositionOfStuttgart() throws UnlocalizableException {
        // Test data
        double expectedLatitude = 48 + 47 * ARC_MINUTE;
        double expectedLongitude = 9 + 11 * ARC_MINUTE;
        CityLocation cityLocation = new CityLocation("Stuttgart");

        // Execution
        SphericalPosition sphericalPosition = cityLocation.getSphericalPosition();

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(expectedLatitude, sphericalPosition.getLatitude(), ARC_MINUTE);
        assertEquals(expectedLongitude, sphericalPosition.getLongitude(), ARC_MINUTE);
    }

    @Test
    public void testGetSphericalPositionOfMunich() throws UnlocalizableException {
        // Test data
        double expectedLatitude = 48 + 8 * ARC_MINUTE;
        double expectedLongitude = 11 + 35 * ARC_MINUTE;
        CityLocation cityLocation = new CityLocation("Munich");

        // Execution
        SphericalPosition sphericalPosition = cityLocation.getSphericalPosition();

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(expectedLatitude, sphericalPosition.getLatitude(), ARC_MINUTE);
        assertEquals(expectedLongitude, sphericalPosition.getLongitude(), ARC_MINUTE);
    }

    @Test
    public void testGetSphericalPositionOfMuenchen() throws UnlocalizableException {
        // Test data
        double expectedLatitude = 48 + 8 * ARC_MINUTE;
        double expectedLongitude = 11 + 35 * ARC_MINUTE;
        CityLocation cityLocation = new CityLocation("Muenchen");

        // Execution
        SphericalPosition sphericalPosition = cityLocation.getSphericalPosition();

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(expectedLatitude, sphericalPosition.getLatitude(), ARC_MINUTE);
        assertEquals(expectedLongitude, sphericalPosition.getLongitude(), ARC_MINUTE);
    }

    @Test
    public void testGetSphericalPositionOfSalzburg() throws UnlocalizableException {
        // Test data
        double expectedLatitude = 47 + 48 * ARC_MINUTE;
        double expectedLongitude = 13 + 2 * ARC_MINUTE;
        CityLocation cityLocation = new CityLocation("Salzburg");

        // Execution
        SphericalPosition sphericalPosition = cityLocation.getSphericalPosition();

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(expectedLatitude, sphericalPosition.getLatitude(), ARC_MINUTE);
        assertEquals(expectedLongitude, sphericalPosition.getLongitude(), ARC_MINUTE);
    }

    @Test(expected = UnlocalizableException.class)
    public void testGetSphericalPositionOfUnknownCity() throws UnlocalizableException {
        // Test data
        CityLocation cityLocation = new CityLocation("KarlsruheABCDEFGHIJKLMNOPCITYunknown");

        // Execution
        cityLocation.getSphericalPosition();
    }

    @Test
    public void testGetSphericalPositionOfPostcode76187() throws UnlocalizableException {
        // Test data
        double expectedLatitude = 49 + 2 * ARC_MINUTE + 36 * ARC_SECOND;
        double expectedLongitude = 8 + 20 * ARC_MINUTE + 1 * ARC_SECOND;
        PostcodeLocation postcodeLocation = new PostcodeLocation("76187");

        // Execution
        SphericalPosition sphericalPosition = postcodeLocation.getSphericalPosition();

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(expectedLatitude, sphericalPosition.getLatitude(), 2 * ARC_MINUTE);
        assertEquals(expectedLongitude, sphericalPosition.getLongitude(), 2 * ARC_MINUTE);
    }

    @Test(expected = UnlocalizableException.class)
    public void testGetSphericalPositionOfUnknownPostcode() throws UnlocalizableException {
        // Test data
        PostcodeLocation postcodeLocation = new PostcodeLocation("76187ABCD84922365829102");

        // Execution
        postcodeLocation.getSphericalPosition();
    }
}
