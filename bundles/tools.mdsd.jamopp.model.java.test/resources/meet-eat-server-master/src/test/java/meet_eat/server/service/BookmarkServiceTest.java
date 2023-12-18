package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookmarkServiceTest extends EntityRelationServiceTest<BookmarkService, Bookmark, User, Offer, String> {

    private static final class BookmarkMock extends Bookmark {

        private static final long serialVersionUID = 8238669973606994892L;

        protected BookmarkMock(String identifier, User source, Offer target) {
            super(identifier, source, target);
        }
    }

    //#region @Test getByUserIdentifier

    @Test(expected = NullPointerException.class)
    public void testGetByUserIdentifierNull() {
        // Assertions
        getEntityService().getByUserIdentifier(null);
    }

    @Test
    public void testGetByUserIdentifierNonExistingUserEmpty() {
        // Test data
        String identifier = "ABCDEFG123!";

        // Execution
        Optional<Iterable<Bookmark>> optionalBookmarks = getEntityService().getByUserIdentifier(identifier);

        // Assertions
        assertTrue(optionalBookmarks.isEmpty());
    }

    @Test
    public void testGetByUserIdentifierExistingUserEmpty() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        Optional<Iterable<Bookmark>> optionalBookmarks = getEntityService().getByUserIdentifier(user.getIdentifier());

        // Assertions
        assertTrue(optionalBookmarks.isPresent());
        assertNotNull(optionalBookmarks.get());
        assertEquals(0, Iterables.size(optionalBookmarks.get()));
    }

    @Test
    public void testGetByUserIdentifierSingleBookmark() {
        // Test data
        User user = getBasicUserPersistent();
        Bookmark bookmark = getRelationEntityPersistent(user, getTargetEntity());

        // Execution
        Optional<Iterable<Bookmark>> optionalBookmarks = getEntityService().getByUserIdentifier(user.getIdentifier());

        // Assertions
        assertTrue(optionalBookmarks.isPresent());
        assertNotNull(optionalBookmarks.get());
        assertEquals(1, Iterables.size(optionalBookmarks.get()));
        assertTrue(Iterables.contains(optionalBookmarks.get(), bookmark));
    }

    @Test
    public void testGetByUserIdentifierMultipleBookmarks() {
        // Test data
        User user = getBasicUserPersistent();
        Bookmark bookmarkFst = getRelationEntityPersistent(user, getTargetEntity());
        Bookmark bookmarkSnd = getRelationEntityPersistent(user, getTargetEntity());
        Bookmark bookmarkForeign = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());

        // Execution
        Optional<Iterable<Bookmark>> optionalBookmarks = getEntityService().getByUserIdentifier(user.getIdentifier());

        // Assertions
        assertEquals(2, Iterables.size(optionalBookmarks.orElseThrow()));
        assertTrue(Iterables.contains(optionalBookmarks.get(), bookmarkFst));
        assertTrue(Iterables.contains(optionalBookmarks.get(), bookmarkSnd));
    }

    //#endregion

    //#region @Test existsPostConflict

    @Test
    public void testExistsPostConflictSameIdentifier() {
        // Test data
        Bookmark bookmark = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Bookmark conflictBookmark = new BookmarkMock(bookmark.getIdentifier(), getSourceEntity(), getTargetEntity());

        // Assertions
        assertTrue(getEntityService().existsPostConflict(conflictBookmark));
    }

    @Test
    public void testExistsPostConflictSameSourceAndSameTarget() {
        // Test data
        Bookmark bookmark = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Bookmark conflictBookmark = new Bookmark(bookmark.getSource(), bookmark.getTarget());

        // Assertions
        assertTrue(getEntityService().existsPostConflict(conflictBookmark));
    }

    @Test
    public void testExistsPostConflictDifferentSourceAndSameTarget() {
        // Test data
        Bookmark bookmark = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Bookmark conflictBookmark = new Bookmark(getSourceEntity(), bookmark.getTarget());

        // Assertions
        assertFalse(getEntityService().existsPostConflict(conflictBookmark));
    }

    @Test
    public void testExistsPostConflictSameSourceAndDifferentTarget() {
        // Test data
        Bookmark bookmark = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Bookmark conflictBookmark = new Bookmark(bookmark.getSource(), getTargetEntity());

        // Assertions
        assertFalse(getEntityService().existsPostConflict(conflictBookmark));
    }

    //#endregion

    @Override
    protected User getSourceEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected Offer getTargetEntity() {
        return getOfferPersistent(getBasicUserPersistent());
    }

    @Override
    protected Bookmark createDistinctTestEntity(User source, Offer target) {
        return new Bookmark(source, target);
    }
}
