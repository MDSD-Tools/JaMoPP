package meet_eat.data.predicate;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.function.BiFunction;

public class BooleanOperatorCommonTest {

    private static class ObjectOperation implements BiFunction<Object, Object, Boolean> {

        @Override
        public Boolean apply(Object o, Object o2) {
            return true;
        }
    }

    private static class BooleanOperatorMock extends BooleanOperator<ObjectOperation, Object> {

        private static final long serialVersionUID = -3438256332795454650L;

        public BooleanOperatorMock(ObjectOperation operation, Object referenceValue) {
            super(operation, referenceValue);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullOperation() {
        // Execution
        new BooleanOperatorMock(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullReferenceValue() {
        // Execution
        new BooleanOperatorMock(new ObjectOperation(), null);
    }

    @Test
    public void testConstructor() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();

        // Execution
        BooleanOperatorMock objectOperator = new BooleanOperatorMock(objectOperation, object);

        // Assertions
        assertNotNull(objectOperator);
        assertNotNull(objectOperator.getOperation());
        assertNotNull(objectOperator.getReferenceValue());
        assertEquals(objectOperation, objectOperator.getOperation());
        assertEquals(object, objectOperator.getReferenceValue());
    }

    @Test
    public void testOperate() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();
        Object operationObject = new Object();

        // Execution
        BooleanOperatorMock objectOperator = new BooleanOperatorMock(objectOperation, object);

        // Assertions
        assertEquals(objectOperation.apply(object, operationObject), objectOperator.operate(operationObject));
    }

    @Test(expected = NullPointerException.class)
    public void testOperateNull() {
        // Test data
        ObjectOperation objectOperation = new ObjectOperation();
        Object object = new Object();

        // Execution
        BooleanOperatorMock objectOperator = new BooleanOperatorMock(objectOperation, object);
        objectOperator.operate(null);
    }
}
