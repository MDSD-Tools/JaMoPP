package meet_eat.data.entity.relation;

import meet_eat.data.entity.user.User;
import meet_eat.data.factory.UserFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SubscriptionCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User source = userFactory.getValidObject();
        User target = userFactory.getValidObject();

        // Execution
        Subscription relation = new Subscription(source, target);

        // Assertions
        assertNotNull(relation);
        assertNotNull(relation.getSource());
        assertNotNull(relation.getTarget());
        assertEquals(source, relation.getSource());
        assertEquals(target, relation.getTarget());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullSource() {
        // Test data
        User target = new UserFactory().getValidObject();

        // Execution
        new Subscription(null, target);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullTarget() {
        // Test data
        User source = new UserFactory().getValidObject();

        // Execution
        new Subscription(source, null);
    }

    @Test
    public void testJsonConstructor() {
        // Test data
        String identifier = "gn03gt402s";
        UserFactory userFactory = new UserFactory();
        User source = userFactory.getValidObject();
        User target = userFactory.getValidObject();

        // Execution
        Subscription relation = new Subscription(identifier, source, target);

        // Assertions
        assertNotNull(relation);
        assertNotNull(relation.getIdentifier());
        assertNotNull(relation.getSource());
        assertNotNull(relation.getTarget());
        assertEquals(identifier, relation.getIdentifier());
        assertEquals(source, relation.getSource());
        assertEquals(target, relation.getTarget());
    }
}
