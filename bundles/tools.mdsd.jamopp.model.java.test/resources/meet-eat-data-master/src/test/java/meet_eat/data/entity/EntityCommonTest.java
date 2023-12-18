package meet_eat.data.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityCommonTest {

    private static class ConcreteEntity extends Entity<String> {

        private static final long serialVersionUID = -5159321112449258959L;

        public ConcreteEntity() {
            super();
        }

        public ConcreteEntity(String identifier) {
            super(identifier);
        }
    }

    @Test
    public void testConstructorEmpty() {   
        // Execution
        ConcreteEntity entity = new ConcreteEntity();

        // Assertions
        assertNotNull(entity);
    }

    @Test
    public void testConstructorIdentifier() {   
        // Test data
        String identifier = "IdentifierTest1234!";

        // Execution
        ConcreteEntity entity = new ConcreteEntity(identifier);

        // Assertions
        assertEquals(identifier, entity.getIdentifier());
    }

    @Test
    public void testEquals() {
        // Execution
        ConcreteEntity entity = new ConcreteEntity("Identifier");
        ConcreteEntity entityCopy = new ConcreteEntity(entity.getIdentifier());

        // Assertions
        assertEquals(entity, entity);
        assertNotEquals(entity, null);
        assertNotEquals(entity, new Object());
        assertEquals(entity, entityCopy);
        assertEquals(entity.hashCode(), entityCopy.hashCode());
    }
}