package meet_eat.data.entity.relation;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.data.factory.OfferFactory;
import meet_eat.data.factory.UserFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParticipationCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        User source = new UserFactory().getValidObject();
        Offer target = new OfferFactory().getValidObject();

        // Execution
        Participation relation = new Participation(source, target);

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
        Offer target = new OfferFactory().getValidObject();

        // Execution
        new Participation(null, target);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullTarget() {
        // Test data
        User source = new UserFactory().getValidObject();

        // Execution
        new Participation(source, null);
    }

    @Test
    public void testJsonConstructor() {
        // Test data
        String identifier = "gn03gt402s";
        User source = new UserFactory().getValidObject();
        Offer target = new OfferFactory().getValidObject();

        // Execution
        Participation relation = new Participation(identifier, source, target);

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
