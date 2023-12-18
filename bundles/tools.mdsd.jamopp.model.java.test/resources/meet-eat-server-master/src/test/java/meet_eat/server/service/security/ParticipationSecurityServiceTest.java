package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Participation;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParticipationSecurityServiceTest extends SecurityServiceTest<ParticipationSecurityService, Participation> {

    @Override
    public void testIsLegalPostNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPostNotSourceToken() {
        // Test data
        Participation participation = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(participation, token));
    }

    @Test
    public void testIsLegalPostSourceToken() {
        // Test data
        Participation participation = getTestEntity();
        Token token = getValidToken(participation.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(participation, token));
    }

    @Test
    public void testIsLegalPostTargetCreatorToken() {
        // Test data
        Participation participation = getTestEntity();
        Token token = getValidToken(participation.getTarget().getCreator());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(participation, token));
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
        Participation participation = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(participation, token));
    }

    @Test
    public void testIsLegalDeleteSourceToken() {
        // Test data
        Participation participation = getTestEntity();
        Token token = getValidToken(participation.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(participation, token));
    }

    @Test
    public void testIsLegalDeleteTargetCreatorToken() {
        // Test data
        Participation participation = getTestEntity();
        Token token = getValidToken(participation.getTarget().getCreator());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(participation, token));
    }

    @Override
    protected Participation getTestEntity() {
        return new Participation(getBasicUser(), getValidOffer(getBasicUser()));
    }
}
