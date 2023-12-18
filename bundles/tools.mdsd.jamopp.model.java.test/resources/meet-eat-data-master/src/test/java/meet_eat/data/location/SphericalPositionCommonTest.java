package meet_eat.data.location;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class SphericalPositionCommonTest {

    private static final double DELTA = 0.000001;

    @Test
    public void testConstructor() {
        // Test data
        double lat = 33.333;
        double lon = 120.1212;

        // Execution
        SphericalPosition sphericalPosition = new SphericalPosition(lat, lon);

        // Assertions
        assertNotNull(sphericalPosition);
        assertEquals(lat, sphericalPosition.getLatitude(), DELTA);
        assertEquals(lon, sphericalPosition.getLongitude(), DELTA);
    }

    @Test
    public void testSetLatitude() {
        // Test data
        double lat = 33.333;
        double lon = 120.1212;
        double testLat = 80.7777;

        // Execution
        SphericalPosition sphericalPosition = new SphericalPosition(lat, lon);
        sphericalPosition.setLatitude(testLat);

        // Assertions
        assertEquals(testLat, sphericalPosition.getLatitude(), DELTA);
        assertNotEquals(lat, sphericalPosition.getLatitude(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLatitudeOutOfBoundsMin() {
        // Test data
        double lat = -90.00001;
        double lon = 120.1212;

        // Execution
        new SphericalPosition(lat, lon);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLatitudeOutOfBoundsMax() {
        // Test data
        double lat = 90.00001;
        double lon = 120.1212;

        // Execution
        new SphericalPosition(lat, lon);
    }

    @Test
    public void testSetLongitude() {
        // Test data
        double lat = 33.333;
        double lon = 120.1212;
        double testLon = 80.7777;

        // Execution
        SphericalPosition sphericalPosition = new SphericalPosition(lat, lon);
        sphericalPosition.setLongitude(testLon);

        // Assertions
        assertEquals(testLon, sphericalPosition.getLongitude(), DELTA);
        assertNotEquals(lon, sphericalPosition.getLongitude(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongitudeOutOfBoundsMin() {
        // Test data
        double lat = 37.111;
        double lon = -180.0001;

        // Execution
        new SphericalPosition(lat, lon);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongitudeOutOfBoundsMax() {
        // Test data
        double lat = 37.111;
        double lon = 180.0001;

        // Execution
        new SphericalPosition(lat, lon);
    }

    @Test
    public void testGetValueAsRadian() {
        // Test data
        double fourtyFive = 45d;
        double ninety = 90d;
        double hundredEighty = 180d;

        // Execution
        SphericalPosition sphericalPositionOne = new SphericalPosition(fourtyFive, fourtyFive);
        SphericalPosition sphericalPositionTwo = new SphericalPosition(ninety, ninety);
        SphericalPosition sphericalPositionThree = new SphericalPosition(ninety, hundredEighty);

        // Assertions
        assertEquals((Math.PI / 4), sphericalPositionOne.getLatitudeAsRadians(), DELTA);
        assertEquals((Math.PI / 4), sphericalPositionOne.getLongitudeAsRadians(), DELTA);

        assertEquals((Math.PI / 2), sphericalPositionTwo.getLatitudeAsRadians(), DELTA);
        assertEquals((Math.PI / 2), sphericalPositionTwo.getLongitudeAsRadians(), DELTA);

        assertEquals((Math.PI / 2), sphericalPositionThree.getLatitudeAsRadians(), DELTA);
        assertEquals(Math.PI, sphericalPositionThree.getLongitudeAsRadians(), DELTA);
    }

    @Test
    public void testEquals() {
        // Execution
        SphericalPosition sphericalPosition = new SphericalPosition(10d, 100d);
        SphericalPosition sphericalPositionCopy = new SphericalPosition(sphericalPosition.getLatitude(), sphericalPosition.getLongitude());
        SphericalPosition sphericalPositionFakeCopyLongitude = new SphericalPosition(sphericalPosition.getLatitude(), 50d);
        SphericalPosition sphericalPositionFakeCopyLatitude = new SphericalPosition(50d, sphericalPosition.getLongitude());

        // Assertions
        assertEquals(sphericalPosition, sphericalPosition);
        assertNotEquals(sphericalPosition, null);
        assertNotEquals(sphericalPosition, new Object());
        assertEquals(sphericalPosition, sphericalPositionCopy);
        assertNotEquals(sphericalPosition, sphericalPositionFakeCopyLongitude);
        assertNotEquals(sphericalPosition, sphericalPositionFakeCopyLatitude);
        assertEquals(sphericalPosition.hashCode(), sphericalPositionCopy.hashCode());
    }
}
