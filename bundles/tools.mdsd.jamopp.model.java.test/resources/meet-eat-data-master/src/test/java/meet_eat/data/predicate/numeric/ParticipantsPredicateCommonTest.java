package meet_eat.data.predicate.numeric;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParticipantsPredicateCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;
        double reference = 1d;

        // Execution
        ParticipantsPredicate participantsPredicate = new ParticipantsPredicate(operation, reference);

        // Assertions
        assertNotNull(participantsPredicate);
        assertNotNull(participantsPredicate.getOperation());
        assertNotNull(participantsPredicate.getReferenceValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() {
        // Test data
        double reference = 1d;

        // Execution
        new ParticipantsPredicate(null, reference);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReferenceValue() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;

        // Execution
        new ParticipantsPredicate(operation, null);
    }

    @Test
    public void testTest() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;
        double reference = 2d;
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();

        Map<Offer, Integer> participantAmounts = new HashMap<>();
        participantAmounts.put(offerOne, 1);
        participantAmounts.put(offerTwo, 2);

        // Execution
        ParticipantsPredicate participantsPredicate = new ParticipantsPredicate(operation, reference);
        participantsPredicate.setParticipantAmountGetter(participantAmounts::get);

        // Assertions
        assertFalse(participantsPredicate.test(offerOne));
        assertTrue(participantsPredicate.test(offerTwo));
    }

    @Test(expected = IllegalStateException.class)
    public void testTestWithoutFunction() {
        // Test data
        DoubleOperation operation = DoubleOperation.EQUAL;
        double reference = 2d;
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();

        // Execution
        ParticipantsPredicate participantsPredicate = new ParticipantsPredicate(operation, reference);
        participantsPredicate.test(offerOne);
    }
}
