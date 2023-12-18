package meet_eat.data.location;

import meet_eat.data.factory.LocationFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class SphericalLocationCommonTest {

    @Test
    public void testConstructor() throws UnlocalizableException {
        // Test data
        LocationFactory locationFactory = new LocationFactory();
        SphericalPosition sphericalPosition = locationFactory.getValidObject().getSphericalPosition();

        // Execution
        SphericalLocation sphericalLocation = new SphericalLocation(sphericalPosition);

        // Assertions
        assertNotNull(sphericalLocation);
        assertEquals(sphericalPosition, sphericalLocation.getSphericalPosition());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullSphericalPosition() {
        // Execution
        new SphericalLocation(null);
    }

    @Test
    public void testSetSphericalPosition() throws UnlocalizableException {
        // Test data
        LocationFactory locationFactory = new LocationFactory();
        SphericalPosition sphericalPositionFst = locationFactory.getValidObject().getSphericalPosition();
        SphericalPosition sphericalPositionSnd = locationFactory.getValidObject().getSphericalPosition();

        // Execution
        SphericalLocation sphericalLocation = new SphericalLocation(sphericalPositionFst);
        sphericalLocation.setSphericalPosition(sphericalPositionSnd);

        // Assertions
        assertNotNull(sphericalLocation.getSphericalPosition());
        assertEquals(sphericalPositionSnd, sphericalLocation.getSphericalPosition());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullSphericalPosition() throws UnlocalizableException {
        // Test data
        LocationFactory locationFactory = new LocationFactory();
        SphericalPosition sphericalPosition = locationFactory.getValidObject().getSphericalPosition();

        // Execution
        SphericalLocation sphericalLocation = new SphericalLocation(sphericalPosition);
        sphericalLocation.setSphericalPosition(null);
    }

    @Test
    public void testEquals() throws UnlocalizableException {
        // Execution
        Localizable sphericalLocation = new LocationFactory().getValidObject();
        Localizable sphericalLocationCopy = new SphericalLocation(sphericalLocation.getSphericalPosition());

        // Assertions
        assertEquals(sphericalLocation, sphericalLocation);
        assertNotEquals(sphericalLocation, null);
        assertNotEquals(sphericalLocation, new Object());
        assertEquals(sphericalLocation, sphericalLocationCopy);
        assertEquals(sphericalLocation.hashCode(), sphericalLocationCopy.hashCode());
    }
}