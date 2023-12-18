package meet_eat.data.predicate.chrono;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalDateTimeOperatorCommonTest {

    private static class LocalDateTimeOperatorMock extends LocalDateTimeOperator {

        private static final long serialVersionUID = -3634102972165883495L;

        public LocalDateTimeOperatorMock(LocalDateTimeOperation operation, LocalDateTime referenceValue) {
            super(operation, referenceValue);
        }
    }

    @Test
    public void testBefore() {
        // Test data
        LocalDateTimeOperation before = LocalDateTimeOperation.BEFORE;
        LocalDateTime reference = LocalDateTime.of(2020, Month.AUGUST, 6, 15, 30);
        LocalDateTime dateTimeOne = LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0);
        LocalDateTime dateTimeTwo = LocalDateTime.of(2020, Month.AUGUST, 6, 15, 30);
        LocalDateTime dateTimeThree = LocalDateTime.of(2100, Month.OCTOBER, 16, 12, 0);

        // Execution
        LocalDateTimeOperatorMock operator = new LocalDateTimeOperatorMock(before, reference);

        // Assertions
        assertTrue(operator.operate(dateTimeOne));
        assertFalse(operator.operate(dateTimeTwo));
        assertFalse(operator.operate(dateTimeThree));
    }

    @Test
    public void testEqual() {
        // Test data
        LocalDateTimeOperation equal = LocalDateTimeOperation.EQUAL;
        LocalDateTime reference = LocalDateTime.of(2020, Month.AUGUST, 6, 15, 30);
        LocalDateTime dateTimeOne = LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0);
        LocalDateTime dateTimeTwo = LocalDateTime.of(2020, Month.AUGUST, 6, 15, 30);
        LocalDateTime dateTimeThree = LocalDateTime.of(2100, Month.OCTOBER, 16, 12, 0);

        // Execution
        LocalDateTimeOperatorMock operator = new LocalDateTimeOperatorMock(equal, reference);

        // Assertions
        assertFalse(operator.operate(dateTimeOne));
        assertTrue(operator.operate(dateTimeTwo));
        assertFalse(operator.operate(dateTimeThree));
    }

    @Test
    public void testAfter() {
        // Test data
        LocalDateTimeOperation after = LocalDateTimeOperation.AFTER;
        LocalDateTime reference = LocalDateTime.of(2020, Month.AUGUST, 6, 15, 30);
        LocalDateTime dateTimeOne = LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0);
        LocalDateTime dateTimeTwo = LocalDateTime.of(2020, Month.AUGUST, 6, 15, 30);
        LocalDateTime dateTimeThree = LocalDateTime.of(2100, Month.OCTOBER, 16, 12, 0);

        // Execution
        LocalDateTimeOperatorMock operator = new LocalDateTimeOperatorMock(after, reference);

        // Assertions
        assertFalse(operator.operate(dateTimeOne));
        assertFalse(operator.operate(dateTimeTwo));
        assertTrue(operator.operate(dateTimeThree));
    }
}
