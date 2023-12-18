package meet_eat.data.predicate.numeric;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleOperatorCommonTest {

    private static class DoubleOperatorMock extends DoubleOperator {

        private static final long serialVersionUID = 6716327545660906599L;

        public DoubleOperatorMock(DoubleOperation operation, Double referenceValue) {
            super(operation, referenceValue);
        }
    }

    @Test
    public void testGreater() {
        // Test data
        DoubleOperation greater = DoubleOperation.GREATER;
        double reference = 123.456;
        double valueOne = 123.455;
        double valueTwo = 123.457;
        double valueThree = 0d;

        // Execution
        DoubleOperatorMock operator = new DoubleOperatorMock(greater, reference);

        // Assertions
        assertFalse(operator.operate(reference));
        assertFalse(operator.operate(valueOne));
        assertTrue(operator.operate(valueTwo));
        assertFalse(operator.operate(valueThree));
    }

    @Test
    public void testEqual() {
        // Test data
        DoubleOperation equal = DoubleOperation.EQUAL;
        double reference = 123.456;
        double valueOne = 123.455;
        double valueTwo = 123.457;
        double valueThree = 123.4560;

        // Execution
        DoubleOperatorMock operator = new DoubleOperatorMock(equal, reference);

        // Assertions
        assertTrue(operator.operate(reference));
        assertFalse(operator.operate(valueOne));
        assertFalse(operator.operate(valueTwo));
        assertTrue(operator.operate(valueThree));
    }

    @Test
    public void testLess() {
        // Test data
        DoubleOperation less = DoubleOperation.LESS;
        double reference = 123.456;
        double valueOne = 123.455;
        double valueTwo = 123.457;
        double valueThree = 0d;

        // Execution
        DoubleOperatorMock operator = new DoubleOperatorMock(less, reference);

        // Assertions
        assertFalse(operator.operate(reference));
        assertTrue(operator.operate(valueOne));
        assertFalse(operator.operate(valueTwo));
        assertTrue(operator.operate(valueThree));
    }
}
