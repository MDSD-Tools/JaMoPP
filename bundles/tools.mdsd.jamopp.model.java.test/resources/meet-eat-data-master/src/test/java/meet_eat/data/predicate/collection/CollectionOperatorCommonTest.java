package meet_eat.data.predicate.collection;

import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollectionOperatorCommonTest {

    private static class CollectionOperatorMock extends CollectionOperator {

        private static final long serialVersionUID = 7037968879667803046L;

        public CollectionOperatorMock(CollectionOperation operation, Collection<?> referenceValue) {
            super(operation, referenceValue);
        }
    }

    @Test
    public void testContainAll() {
        // Test data
        CollectionOperation containAll = CollectionOperation.CONTAIN_ALL;
        Collection<Integer> referenceEmpty = new LinkedList<>();
        Collection<Integer> reference = new LinkedList<>();
        reference.add(0);
        reference.add(10);
        reference.add(100);
        reference.add(1000);
        Collection<Integer> collectionOne = new HashSet<>();
        collectionOne.add(0);
        collectionOne.add(10);
        collectionOne.add(100);
        collectionOne.add(1000);
        Collection<Integer> collectionTwo = new LinkedList<>();
        collectionTwo.add(0);
        collectionTwo.add(10);
        collectionTwo.add(100);
        Collection<Integer> collectionThree = new LinkedList<>();
        collectionThree.add(0);
        collectionThree.add(10);
        collectionThree.add(-1);
        Collection<Integer> collectionFour = new LinkedList<>();

        // Execution
        CollectionOperatorMock operatorEmpty = new CollectionOperatorMock(containAll, referenceEmpty);
        CollectionOperatorMock operator = new CollectionOperatorMock(containAll, reference);

        // Assertions
        assertTrue(operatorEmpty.operate(collectionOne));
        assertTrue(operator.operate(reference));
        assertTrue(operator.operate(collectionOne));
        assertFalse(operator.operate(collectionTwo));
        assertFalse(operator.operate(collectionThree));
        assertFalse(operator.operate(collectionFour));
    }

    @Test
    public void testContainAny() {
        // Test data
        CollectionOperation containAny = CollectionOperation.CONTAIN_ANY;
        Collection<Integer> referenceEmpty = new LinkedList<>();
        Collection<Integer> reference = new LinkedList<>();
        reference.add(0);
        reference.add(10);
        reference.add(100);
        reference.add(1000);
        Collection<Integer> collectionOne = new HashSet<>();
        collectionOne.add(-10);
        collectionOne.add(10);
        collectionOne.add(101);
        collectionOne.add(1011);
        Collection<Integer> collectionTwo = new LinkedList<>();
        collectionTwo.add(0);
        collectionTwo.add(10);
        collectionTwo.add(100);
        Collection<Integer> collectionThree = new LinkedList<>();

        // Execution
        CollectionOperatorMock operatorEmpty = new CollectionOperatorMock(containAny, referenceEmpty);
        CollectionOperatorMock operator = new CollectionOperatorMock(containAny, reference);

        // Assertions
        assertTrue(operatorEmpty.operate(collectionOne));
        assertTrue(operator.operate(collectionOne));
        assertTrue(operator.operate(collectionTwo));
        assertFalse(operator.operate(collectionThree));
    }

    @Test
    public void testContainNone() {
        // Test data
        CollectionOperation containNone = CollectionOperation.CONTAIN_NONE;
        Collection<Integer> referenceEmpty = new LinkedList<>();
        Collection<Integer> reference = new LinkedList<>();
        reference.add(0);
        reference.add(10);
        reference.add(100);
        reference.add(1000);
        Collection<Integer> collectionOne = new HashSet<>();
        collectionOne.add(-10);
        collectionOne.add(10);
        collectionOne.add(101);
        collectionOne.add(1011);
        Collection<Integer> collectionTwo = new LinkedList<>();
        collectionTwo.add(-50);
        collectionTwo.add(5);
        collectionTwo.add(999);
        Collection<Integer> collectionThree = new LinkedList<>();

        // Execution
        CollectionOperatorMock operatorEmpty = new CollectionOperatorMock(containNone, referenceEmpty);
        CollectionOperatorMock operator = new CollectionOperatorMock(containNone, reference);

        // Assertions
        assertTrue(operatorEmpty.operate(collectionOne));
        assertTrue(operatorEmpty.operate(collectionThree));
        assertFalse(operator.operate(collectionOne));
        assertTrue(operator.operate(collectionTwo));
        assertTrue(operator.operate(collectionThree));
    }
}
