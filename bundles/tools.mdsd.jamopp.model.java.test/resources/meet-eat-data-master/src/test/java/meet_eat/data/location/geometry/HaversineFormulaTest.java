package meet_eat.data.location.geometry;

import meet_eat.data.location.SphericalPosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HaversineFormulaTest {

    // Allow deviation of 1/1000 of a meter
    private static final double DELTA = 0.001;
    // Rounding factor for third decimal
    private static final int ROUNDING_FACTOR = 1000;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // London to Karlsruhe
                {new SphericalPosition(51.5073219, -0.1276474),
                        new SphericalPosition(49.0068705, 8.4034195),
                        666648.2655},
                // Karlsruhe to London
                {new SphericalPosition(49.0068705, 8.4034195),
                        new SphericalPosition(51.5073219, -0.1276474),
                        666648.2655},
                // Karlsruhe to Stuttgart
                {new SphericalPosition(49.0068705, 8.4034195),
                        new SphericalPosition(48.7784485, 9.1800132),
                        62196.95938},
                // Munich to Hamburg
                {new SphericalPosition(48.1371079, 11.5753822),
                        new SphericalPosition(53.550341, 10.000654),
                        611953.1483},
                // New York City to Chicago
                {new SphericalPosition(40.7127281, -74.0060152),
                        new SphericalPosition(41.8755616, -87.6244212),
                        1143836.85},
                // Madrid to Valencia
                {new SphericalPosition(40.4167047, -3.7035825),
                        new SphericalPosition(39.4697065, -0.3763353),
                        302541.6859}
        });
    }

    private final SphericalPosition origin;
    private final SphericalPosition destination;
    private final double expected;

    public HaversineFormulaTest(SphericalPosition origin, SphericalPosition destination, double expected) {
        this.origin = origin;
        this.destination = destination;
        this.expected = expected;
    }

    @Test
    public void testApplyHaversineFormula() {
        double result = roundToThirdDecimal(Haversine.applyHaversineFormula(origin, destination));
        assertEquals(expected, result, DELTA);
    }

    private double roundToThirdDecimal(double distance) {
        return (((double) Math.round(distance * ROUNDING_FACTOR)) / ROUNDING_FACTOR);
    }
}
