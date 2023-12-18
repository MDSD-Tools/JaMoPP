package meet_eat.data.entity.relation;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Reportable;
import meet_eat.data.entity.user.User;
import meet_eat.data.factory.UserFactory;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportCommonTest {

    private static final class ConcreteSerializable implements Serializable {

        private static final long serialVersionUID = -9140807055984889146L;

        public ConcreteSerializable() {
            super();
        }
    }

    private static final class ConcreteEntity extends Entity<ConcreteSerializable> implements Reportable {

        private static final long serialVersionUID = 3971456058385893862L;

        public ConcreteEntity(ConcreteSerializable identifier) {
            super(identifier);
        }
    }

    @Test
    public void testConstructor() {
        // Test data
        User source = new UserFactory().getValidObject();
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());
        String message = "Report test message.";

        // Execution
        Report relation = new Report(source, target, message);

        // Assertions
        assertNotNull(relation);
        assertNotNull(relation.getSource());
        assertNotNull(relation.getTarget());
        assertNotNull(relation.getMessage());
        assertEquals(source, relation.getSource());
        assertEquals(target, relation.getTarget());
        assertEquals(message, relation.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullSource() {
        // Test data
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());
        String message = "Report test message.";

        // Execution
        new Report(null, target, message);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullTarget() {
        // Test data
        User source = new UserFactory().getValidObject();
        String message = "Report test message.";

        // Execution
        new Report(source, null, message);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullMessage() {
        // Test data
        User source = new UserFactory().getValidObject();
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());

        // Execution
        new Report(source, target, null);
    }

    @Test
    public void testJsonConstructor() {
        // Test data
        String identifier = "fuohau490t";
        User source = new UserFactory().getValidObject();
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());
        String message = "Report test message.";

        // Execution
        Report relation = new Report(identifier, source, target, message, true);

        // Assertions
        assertNotNull(relation);
        assertNotNull(relation.getIdentifier());
        assertNotNull(relation.getSource());
        assertNotNull(relation.getTarget());
        assertNotNull(relation.getMessage());
        assertEquals(identifier, relation.getIdentifier());
        assertEquals(source, relation.getSource());
        assertEquals(target, relation.getTarget());
        assertEquals(message, relation.getMessage());
        assertTrue(relation.isProcessed());
    }

    @Test
    public void testSetterProcessed() {
        // Test data
        User source = new UserFactory().getValidObject();
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());
        String message = "Report test message.";

        // Execution
        Report relation = new Report(source, target, message);
        relation.setProcessed(true);

        // Assertions
        assertNotNull(relation);
        assertNotNull(relation.getSource());
        assertNotNull(relation.getTarget());
        assertNotNull(relation.getMessage());
        assertEquals(source, relation.getSource());
        assertEquals(target, relation.getTarget());
        assertEquals(message, relation.getMessage());
        assertTrue(relation.isProcessed());
    }
}
