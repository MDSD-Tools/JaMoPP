package meet_eat.data.entity.relation.rating;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class RatingValueCommonTest {

    @Test
    public void testGetValueByInteger() {
        // Test data
        int one = 1;
        int two = 2;
        int three = 3;
        int four = 4;
        int five = 5;

        // Assertions
        assertEquals(RatingValue.POINTS_1, RatingValue.getRatingValueByInteger(one));
        assertEquals(RatingValue.POINTS_2, RatingValue.getRatingValueByInteger(two));
        assertEquals(RatingValue.POINTS_3, RatingValue.getRatingValueByInteger(three));
        assertEquals(RatingValue.POINTS_4, RatingValue.getRatingValueByInteger(four));
        assertEquals(RatingValue.POINTS_5, RatingValue.getRatingValueByInteger(five));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetValueByIntegerOutOfBounds() {
        // Test data
        int value = 10;

        // Execution
        RatingValue.getRatingValueByInteger(value);
    }
}
