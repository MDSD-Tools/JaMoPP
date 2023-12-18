package meet_eat.server.service.security;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OfferSecurityServiceTest extends SecurityServiceTest<OfferSecurityService, Offer> {

    @Test
    public void testIsLegalPostEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(offer.getCreator());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(offer, token));
    }

    @Test
    public void testIsLegalPostInvalidEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = new Token(offer.getCreator(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalPost(offer, token));
    }

    @Test
    public void testIsLegalPostNonEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(offer, token));
    }

    @Test
    public void testIsLegalPutEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(offer.getCreator());

        // Assertions
        assertTrue(getSecurityService().isLegalPut(offer, token));
    }

    @Test
    public void testIsLegalPutBasicToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(offer, token));
    }

    @Test
    public void testIsLegalPutModeratorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPut(offer, token));
    }

    @Test
    public void testIsLegalPutAdminToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalPut(offer, token));
    }

    @Test
    public void testIsLegalPutInvalidEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = new Token(offer.getCreator(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalPut(offer, token));
    }

    @Test
    public void testIsLegalPutInvalidAdminToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = new Token(getAdminUser(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalPut(offer, token));
    }

    @Test
    public void testIsLegalDeleteEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(offer.getCreator());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(offer, token));
    }

    @Test
    public void testIsLegalDeleteBasicToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(offer, token));
    }

    @Test
    public void testIsLegalDeleteModeratorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(offer, token));
    }

    @Test
    public void testIsLegalDeleteAdminToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(offer, token));
    }

    @Test
    public void testIsLegalDeleteInvalidEntityCreatorToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = new Token(offer.getCreator(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(offer, token));
    }

    @Test
    public void testIsLegalDeleteInvalidAdminToken() {
        // Test data
        Offer offer = getTestEntity();
        Token token = new Token(getAdminUser(), "ABCDEFG");

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(offer, token));
    }

    @Override
    protected Offer getTestEntity() {
        return getValidOffer(getBasicUser());
    }
}
