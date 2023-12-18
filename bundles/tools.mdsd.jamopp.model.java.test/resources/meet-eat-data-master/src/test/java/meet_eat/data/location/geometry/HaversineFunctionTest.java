package meet_eat.data.location.geometry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HaversineFunctionTest {

    private static final double DELTA = 0.000001;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0d, 0d},
                {1d, 0.229849},
                {-1d, 0.229849},
                {Math.PI, 1},
                {2 * Math.PI, 0d}
        });
    }

    private final double value;
    private final double expected;

    public HaversineFunctionTest(double value, double expected) {
        this.value = value;
        this.expected = expected;
    }

    @Test
    public void testApplyHaversineFunction() {
        assertEquals(expected, Haversine.applyHaversineFunction(value), DELTA);
    }
}
