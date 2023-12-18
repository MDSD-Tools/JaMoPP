package meet_eat.data.predicate.numeric;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class PricePredicateCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;
        double reference = 1d;

        // Execution
        PricePredicate pricePredicate = new PricePredicate(operation, reference);

        // Assertions
        assertNotNull(pricePredicate);
        assertNotNull(pricePredicate.getOperation());
        assertNotNull(pricePredicate.getReferenceValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() {
        // Test data
        double reference = 1d;

        // Execution
        new PricePredicate(null, reference);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReferenceValue() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;

        // Execution
        new PricePredicate(operation, null);
    }

    @Test
    public void testOperate() {
        // Test data
        DoubleOperation operation = DoubleOperation.LESS;
        double reference = 3.5;
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.setPrice(3.4);
        Offer offerTwo = offerFactory.getValidObject();
        offerTwo.setPrice(3.6);

        // Execution
        PricePredicate pricePredicate = new PricePredicate(operation, reference);

        // Assertions
        assertTrue(pricePredicate.test(offerOne));
        assertFalse(pricePredicate.test(offerTwo));
    }
}
