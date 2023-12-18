package meet_eat.server.service.security;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingValue;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RatingSecurityServiceTest extends SecurityServiceTest<RatingSecurityService, Rating> {

    @Override
    public void testIsLegalPostNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPostNotSourceToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(rating, token));
    }

    @Test
    public void testIsLegalPostSourceToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(rating.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(rating, token));
    }

    @Test
    public void testIsLegalPostTargetToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(rating.getTarget());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(rating, token));
    }

    @Override
    public void testIsLegalPutNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(null, token));
    }

    @Override
    public void testIsLegalPutNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPut(getTestEntity(), null));
    }

    @Override
    public void testIsLegalDeleteNullEntity() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(null, token));
    }

    @Override
    public void testIsLegalDeleteNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalDelete(getTestEntity(), null));
    }

    @Test
    public void testIsLegalDeleteSourceToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(rating.getSource());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(rating, token));
    }

    @Test
    public void testIsLegalDeleteTargetToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(rating.getTarget());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(rating, token));
    }

    @Test
    public void testIsLegalDeleteModeratorToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(rating, token));
    }

    @Test
    public void testIsLegalDeleteAdminToken() {
        // Test data
        Rating rating = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(rating, token));
    }

    @Override
    protected Rating getTestEntity() {
        User guest = getBasicUser();
        Offer offer = getValidOffer(getBasicUser());
        return Rating.createGuestRating(guest, offer, RatingValue.POINTS_3);
    }
}
