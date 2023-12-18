package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SubscriptionServiceTest extends EntityRelationServiceTest<SubscriptionService, Subscription, User, User, String> {

    private static final class SubscriptionMock extends Subscription {

        private static final long serialVersionUID = -2743021364446256409L;

        protected SubscriptionMock(String identifier, User source, User target) {
            super(identifier, source, target);
        }
    }

    //#region @Test getBySourceUserIdentifier

    @Test(expected = NullPointerException.class)
    public void testGetBySourceUserIdentifierNull() {
        // Assertions
        getEntityService().getBySourceUserIdentifier(null);
    }

    @Test
    public void testGetBySourceUserIdentifierNonExistingUserEmpty() {
        // Test data
        String identifier = "ABCDEFG123!";

        // Execution
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUserIdentifier(identifier);

        // Assertions
        assertTrue(optionalSubscriptions.isEmpty());
    }

    @Test
    public void testGetBySourceUserIdentifierExistingUserEmpty() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUserIdentifier(user.getIdentifier());

        // Assertions
        assertTrue(optionalSubscriptions.isPresent());
        assertNotNull(optionalSubscriptions.get());
        assertEquals(0, Iterables.size(optionalSubscriptions.get()));
    }

    @Test
    public void testGetBySourceUserIdentifierSingleSubscription() {
        // Test data
        User user = getBasicUserPersistent();
        Subscription subscription = getRelationEntityPersistent(user, getTargetEntity());

        // Execution
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUserIdentifier(user.getIdentifier());

        // Assertions
        assertTrue(optionalSubscriptions.isPresent());
        assertNotNull(optionalSubscriptions.get());
        assertEquals(1, Iterables.size(optionalSubscriptions.get()));
        assertTrue(Iterables.contains(optionalSubscriptions.get(), subscription));
    }

    @Test
    public void testGetBySourceUserIdentifierMultipleSubscriptions() {
        // Test data
        User user = getBasicUserPersistent();
        Subscription subscriptionFst = getRelationEntityPersistent(user, getTargetEntity());
        Subscription subscriptionSnd = getRelationEntityPersistent(user, getTargetEntity());
        Subscription subscriptionForeign = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());

        // Execution
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUserIdentifier(user.getIdentifier());

        // Assertions
        assertEquals(2, Iterables.size(optionalSubscriptions.orElseThrow()));
        assertTrue(Iterables.contains(optionalSubscriptions.get(), subscriptionFst));
        assertTrue(Iterables.contains(optionalSubscriptions.get(), subscriptionSnd));
        assertFalse(Iterables.contains(optionalSubscriptions.get(), subscriptionForeign));
    }

    //#endregion

    //#region @Test existsPostConflict

    @Test
    public void testExistsPostConflictSameIdentifier() {
        // Test data
        Subscription subscription = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Subscription conflictSubscription = new SubscriptionMock(subscription.getIdentifier(), getSourceEntity(), getTargetEntity());

        // Assertions
        assertTrue(getEntityService().existsPostConflict(conflictSubscription));
    }

    @Test
    public void testExistsPostConflictSameSourceAndSameTarget() {
        // Test data
        Subscription subscription = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Subscription conflictSubscription = new Subscription(subscription.getSource(), subscription.getTarget());

        // Assertions
        assertTrue(getEntityService().existsPostConflict(conflictSubscription));
    }

    @Test
    public void testExistsPostConflictDifferentSourceAndSameTarget() {
        // Test data
        Subscription subscription = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Subscription conflictSubscription = new Subscription(getSourceEntity(), subscription.getTarget());

        // Assertions
        assertFalse(getEntityService().existsPostConflict(conflictSubscription));
    }

    @Test
    public void testExistsPostConflictSameSourceAndDifferentTarget() {
        // Test data
        Subscription subscription = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Subscription conflictSubscription = new Subscription(subscription.getSource(), getTargetEntity());

        // Assertions
        assertFalse(getEntityService().existsPostConflict(conflictSubscription));
    }

    //#endregion

    @Override
    protected User getSourceEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected User getTargetEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected Subscription createDistinctTestEntity(User source, User target) {
        return new Subscription(source, target);
    }
}
