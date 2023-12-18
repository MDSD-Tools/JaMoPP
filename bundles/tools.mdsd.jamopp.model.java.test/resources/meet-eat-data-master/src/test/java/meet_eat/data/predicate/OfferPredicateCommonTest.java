package meet_eat.data.predicate;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class OfferPredicateCommonTest {

    private static class ObjectOperation implements BiFunction<Object, Object, Boolean> {

        @Override
        public Boolean apply(Object o, Object o2) {
            return true;
        }
    }

    private static class ConcreteOperator extends BooleanOperator<ObjectOperation, Object> {

        private static final long serialVersionUID = -5089642069838520263L;

        protected ConcreteOperator(ObjectOperation operation, Object referenceValue) {
            super(operation, referenceValue);
        }
    }

    private static class ConcretePredicateRating extends ConcreteOperator implements OfferPredicate {

        private static final long serialVersionUID = -5016386823510937362L;

        private Function<Offer, Double> function;

        protected ConcretePredicateRating(ObjectOperation operation, Object referenceValue) {
            super(operation, referenceValue);
        }

        @Override
        public boolean test(Offer offer) {
            return operate(function.apply(offer));
        }

        public void setNumericRatingGetter(Function<Offer, Double> numericRatingGetter) {
            function = numericRatingGetter;
        }
    }

    private static class ConcretePredicateParticipants extends ConcreteOperator implements OfferPredicate {

        private static final long serialVersionUID = 5649016716449183776L;

        private Function<Offer, Integer> function;

        protected ConcretePredicateParticipants(ObjectOperation operation, Object referenceValue) {
            super(operation, referenceValue);
        }

        @Override
        public boolean test(Offer offer) {
            return operate(function.apply(offer));
        }

        public void setParticipantAmountGetter(Function<Offer, Integer> participantAmountGetter) {
            function = participantAmountGetter;
        }
    }

    @Test
    public void testSetNumericRatingGetter() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();

        // Execution
        ConcretePredicateRating concretePredicate = new ConcretePredicateRating(objectOperation, object);
        concretePredicate.setNumericRatingGetter(Offer::getPrice);

        // Assertions
        assertTrue(concretePredicate.test(new OfferFactory().getValidObject()));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullNumericRatingGetter() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();

        // Execution
        ConcretePredicateRating concretePredicate = new ConcretePredicateRating(objectOperation, object);
        concretePredicate.setNumericRatingGetter(null);
        concretePredicate.test(new OfferFactory().getValidObject());
    }

    @Test
    public void testSetParticipantAmountGetter() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();

        // Execution
        ConcretePredicateParticipants concretePredicate = new ConcretePredicateParticipants(objectOperation, object);
        concretePredicate.setParticipantAmountGetter(Offer::getMaxParticipants);

        // Assertions
        assertTrue(concretePredicate.test(new OfferFactory().getValidObject()));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullParticipantAmountGetter() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();

        // Execution
        ConcretePredicateParticipants concretePredicate = new ConcretePredicateParticipants(objectOperation, object);
        concretePredicate.setParticipantAmountGetter(null);
        concretePredicate.test(new OfferFactory().getValidObject());
    }
}
