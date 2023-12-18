package meet_eat.data.factory;

import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import java.util.concurrent.ThreadLocalRandom;

public class LocationFactory extends ObjectFactory<Localizable> {

    // Latitude range in degrees
    private static final double LAT_START = -90d;
    private static final double LAT_END = 90d;
    // Longitude range in degrees
    private static final double LON_START = -180d;
    private static final double LON_END = 180d;

    @Override
    protected Localizable createObject() {
        return new SphericalLocation(getRandomPosition());
    }

    private SphericalPosition getRandomPosition() {
        double randomLat = ThreadLocalRandom.current().nextDouble();
        double randomLon = ThreadLocalRandom.current().nextDouble();
        // Create random latitude between [-90,90)
        double latitude = LAT_START + (randomLat * (LAT_END - LAT_START));
        // Create random longitude between [-180, 180)
        double longitude = LON_START + (randomLon * (LON_END - LON_START));
        return new SphericalPosition(latitude, longitude);
    }
}
