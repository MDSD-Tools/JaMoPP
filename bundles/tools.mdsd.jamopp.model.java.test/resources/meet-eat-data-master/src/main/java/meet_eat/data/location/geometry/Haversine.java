package meet_eat.data.location.geometry;

import meet_eat.data.location.SphericalPosition;

import java.util.function.Function;

/**
 * This class consists exclusively of static methods used to calculate the distance of two different points on a sphere
 * based on the Haversine formula.
 */
public final class Haversine {

    private static final double EARTH_RADIUS = 6371000d;

    private Haversine() {
    }

    /**
     * Calculates sinus(x / 2) to the power of 2 with x as given value.
     */
    private static final Function<Double, Double> HAVERSINE_FUNCTION = x -> Math.pow(Math.sin(x / 2.0), 2.0);

    /**
     * Applies the Haversine function with a given value.
     *
     * @param value the value
     * @return the result of the Haversine function
     */
    public static Double applyHaversineFunction(Double value) {
        return HAVERSINE_FUNCTION.apply(value);
    }

    /**
     * Applies the Haversine formula and calculates the distance between an origin {@link SphericalPosition} and a
     * destination {@link SphericalPosition}.
     *
     * @param origin      the origin position
     * @param destination the destination position
     * @return the distance between origin and destination
     */
    public static Double applyHaversineFormula(SphericalPosition origin, SphericalPosition destination) {
        double havLat = HAVERSINE_FUNCTION.apply(destination.getLatitudeAsRadians() - origin.getLatitudeAsRadians());
        double havLon = HAVERSINE_FUNCTION.apply(destination.getLongitudeAsRadians() - origin.getLongitudeAsRadians());

        double cosLatOrigin = Math.cos(origin.getLatitudeAsRadians());
        double cosLatDestination = Math.cos(destination.getLatitudeAsRadians());

        return 2.0 * EARTH_RADIUS * Math.asin(Math.sqrt(havLat + cosLatOrigin * cosLatDestination * havLon));
    }
}