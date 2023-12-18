package meet_eat.data.predicate.string;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DescriptionPredicateCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        StringOperation operation = StringOperation.EQUAL;
        String reference = "Test";

        // Execution
        DescriptionPredicate descriptionPredicate = new DescriptionPredicate(operation, reference);

        // Assertions
        assertNotNull(descriptionPredicate);
        assertNotNull(descriptionPredicate.getOperation());
        assertNotNull(descriptionPredicate.getReferenceValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() {
        // Test data
        String reference = "Test";

        // Execution
        new DescriptionPredicate(null, reference);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReferenceValue() {
        // Test data
        StringOperation operation = StringOperation.EQUAL;

        // Execution
        new DescriptionPredicate(operation, null);
    }

    @Test
    public void testOperate() {
        // Test data
        StringOperation operation = StringOperation.EQUAL;
        String reference = "Test";
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.setDescription(reference);
        Offer offerTwo = offerFactory.getValidObject();

        // Execution
        DescriptionPredicate descriptionPredicate = new DescriptionPredicate(operation, reference);

        // Assertions
        assertNotEquals(offerOne, offerTwo);
        assertTrue(descriptionPredicate.test(offerOne));
        assertFalse(descriptionPredicate.test(offerTwo));
    }
}
