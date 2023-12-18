package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Bookmark;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookmarkSecurityServiceTest extends SecurityServiceTest<BookmarkSecurityService, Bookmark> {

    @Override
    public void testIsLegalPostNullToken() {
        // Assertions
        assertFalse(getSecurityService().isLegalPost(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPostNotSourceToken() {
        // Test data
        Bookmark bookmark = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(bookmark, token));
    }

    @Test
    public void testIsLegalPostSourceToken() {
        // Test data
        Bookmark bookmark = getTestEntity();
        Token token = getValidToken(bookmark.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalPost(bookmark, token));
    }

    @Test
    public void testIsLegalPostTargetCreatorToken() {
        // Test data
        Bookmark bookmark = getTestEntity();
        Token token = getValidToken(bookmark.getTarget().getCreator());

        // Assertions
        assertFalse(getSecurityService().isLegalPost(bookmark, token));
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
        Bookmark bookmark = getTestEntity();
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(bookmark, token));
    }

    @Test
    public void testIsLegalDeleteSourceToken() {
        // Test data
        Bookmark bookmark = getTestEntity();
        Token token = getValidToken(bookmark.getSource());

        // Assertions
        assertTrue(getSecurityService().isLegalDelete(bookmark, token));
    }

    @Test
    public void testIsLegalDeleteTargetCreatorToken() {
        // Test data
        Bookmark bookmark = getTestEntity();
        Token token = getValidToken(bookmark.getTarget().getCreator());

        // Assertions
        assertFalse(getSecurityService().isLegalDelete(bookmark, token));
    }

    @Override
    protected Bookmark getTestEntity() {
        return new Bookmark(getBasicUser(), getValidOffer(getBasicUser()));
    }
}
