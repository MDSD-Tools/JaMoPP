package meet_eat.data.entity.relation;

import meet_eat.data.entity.Entity;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntityRelationCommonTest {

    private static final class ConcreteSerializable implements Serializable {

        private static final long serialVersionUID = -9140807055984889146L;

        public ConcreteSerializable() {
            super();
        }
    }

    private static final class ConcreteEntity extends Entity<ConcreteSerializable> {

        private static final long serialVersionUID = 3971456058385893862L;

        public ConcreteEntity(ConcreteSerializable identifier) {
            super(identifier);
        }
    }

    private static final class ConcreteEntityRelation extends EntityRelation<ConcreteEntity, ConcreteEntity, ConcreteSerializable> {

        private static final long serialVersionUID = -8583181573254223233L;

        public ConcreteEntityRelation(ConcreteEntity source, ConcreteEntity target) {
            super(source, target);
        }

        public ConcreteEntityRelation(ConcreteSerializable identifier, ConcreteEntity source, ConcreteEntity target) {
            super(identifier, source, target);
        }
    }

    @Test
    public void testConstructor() {
        // Test data
        ConcreteEntity source = new ConcreteEntity(new ConcreteSerializable());
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());

        // Execution
        ConcreteEntityRelation entityRelation = new ConcreteEntityRelation(source, target);

        // Assertions
        assertNotNull(entityRelation);
        assertNotNull(entityRelation.getSource());
        assertNotNull(entityRelation.getTarget());
        assertEquals(source, entityRelation.getSource());
        assertEquals(target, entityRelation.getTarget());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullSource() {
        // Test data
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());

        // Execution
        new ConcreteEntityRelation(null, target);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullTarget() {
        // Test data
        ConcreteEntity source = new ConcreteEntity(new ConcreteSerializable());

        // Execution
        new ConcreteEntityRelation(source, null);
    }

    @Test
    public void testJsonConstructor() {
        // Test data
        ConcreteSerializable identifier = new ConcreteSerializable();
        ConcreteEntity source = new ConcreteEntity(new ConcreteSerializable());
        ConcreteEntity target = new ConcreteEntity(new ConcreteSerializable());

        // Execution
        ConcreteEntityRelation entityRelation = new ConcreteEntityRelation(identifier, source, target);

        // Assertions
        assertNotNull(entityRelation);
        assertNotNull(entityRelation.getIdentifier());
        assertNotNull(entityRelation.getSource());
        assertNotNull(entityRelation.getTarget());
        assertEquals(identifier, entityRelation.getIdentifier());
        assertEquals(source, entityRelation.getSource());
        assertEquals(target, entityRelation.getTarget());
    }
}
