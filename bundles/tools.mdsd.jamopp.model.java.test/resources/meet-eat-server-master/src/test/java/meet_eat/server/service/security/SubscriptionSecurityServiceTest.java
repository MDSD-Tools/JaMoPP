package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Subscription;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubscriptionSecurityServiceTest extends SecurityServiceTest<SubscriptionSecurityService, Subscription> {

    @Override
    public void testIsLegalPostNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPostNotSourceToken() {
        // Test data
        Subscription subscription = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(subscription, token));
    }

    @Test
    public void testIsLegalPostSourceToken() {
        // Test data
        Subscription subscription = getTestEntity();
        Token token = getValidToken(subscription.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(subscription, token));
    }

    @Test
    public void testIsLegalPostTargetToken() {
        // Test data
        Subscription subscription = getTestEntity();
        Token token = getValidToken(subscription.getTarget());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(subscription, token));
    }

    @Override
    public void testIsLegalPutNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPut(getTestEntity(), null));
    }

    @Override
    public void testIsLegalPutNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(null, token));
    }

    @Override
    public void testIsLegalDeleteNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalDelete(getTestEntity(), null));
    }

    @Test
    public void testIsLegalDeleteNotSourceToken() {
        // Test data
        Subscription subscription = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(subscription, token));
    }

    @Test
    public void testIsLegalDeleteSourceToken() {
        // Test data
        Subscription subscription = getTestEntity();
        Token token = getValidToken(subscription.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(subscription, token));
    }

    @Test
    public void testIsLegalDeleteTargetToken() {
        // Test data
        Subscription subscription = getTestEntity();
        Token token = getValidToken(subscription.getTarget());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(subscription, token));
    }

    @Override
    protected Subscription getTestEntity() {
        return new Subscription(getBasicUser(), getBasicUser());
    }
}
