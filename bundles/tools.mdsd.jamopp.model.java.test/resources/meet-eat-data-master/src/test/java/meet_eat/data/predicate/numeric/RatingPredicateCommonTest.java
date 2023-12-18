package meet_eat.data.predicate.numeric;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RatingPredicateCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;
        double reference = 1d;

        // Execution
        RatingPredicate ratingPredicate = new RatingPredicate(operation, reference);

        // Assertions
        assertNotNull(ratingPredicate);
        assertNotNull(ratingPredicate.getOperation());
        assertNotNull(ratingPredicate.getReferenceValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() {
        // Test data
        double reference = 1d;

        // Execution
        new RatingPredicate(null, reference);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReferenceValue() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;

        // Execution
        new RatingPredicate(operation, null);
    }

    @Test
    public void testTest() {
        // Test data
        DoubleOperation operation = DoubleOperation.LESS;
        double reference = 3.5;

        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();

        Map<Offer, Double> hostRatings = new HashMap<>();
        hostRatings.put(offerOne, 3.6);
        hostRatings.put(offerTwo, 3.4);

        // Execution
        RatingPredicate ratingPredicate = new RatingPredicate(operation, reference);
        ratingPredicate.setNumericRatingGetter(hostRatings::get);

        // Assertions
        assertFalse(ratingPredicate.test(offerOne));
        assertTrue(ratingPredicate.test(offerTwo));
    }

    @Test(expected = IllegalStateException.class)
    public void testTestWithoutFunction() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;
        double reference = 2d;
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();

        // Execution
        RatingPredicate ratingPredicate = new RatingPredicate(operation, reference);
        ratingPredicate.test(offerOne);
    }
}
