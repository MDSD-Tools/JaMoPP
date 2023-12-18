package meet_eat.data.predicate.string;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamePredicateCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        StringOperation operation = StringOperation.EQUAL;
        String reference = "Test";

        // Execution
        NamePredicate namePredicate = new NamePredicate(operation, reference);

        // Assertions
        assertNotNull(namePredicate);
        assertNotNull(namePredicate.getOperation());
        assertNotNull(namePredicate.getReferenceValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() {
        // Test data
        String reference = "Test";

        // Execution
        new NamePredicate(null, reference);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReferenceValue() {
        // Test data
        StringOperation operation = StringOperation.EQUAL;

        // Execution
        new NamePredicate(operation, null);
    }

    @Test
    public void testOperate() {
        // Test data
        StringOperation operation = StringOperation.EQUAL;
        String reference = "Test";
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.setName(reference);
        Offer offerTwo = offerFactory.getValidObject();

        // Execution
        NamePredicate namePredicate = new NamePredicate(operation, reference);

        // Assertions
        assertNotEquals(offerOne, offerTwo);
        assertTrue(namePredicate.test(offerOne));
        assertFalse(namePredicate.test(offerTwo));
    }
}
