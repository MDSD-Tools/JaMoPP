package meet_eat.server.controller;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookmarkControllerTest extends EntityControllerTest<BookmarkController, Bookmark, String> {

    //#region @Test @RequestMapping

    @Test
    public void testGetBookmarksByUser() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Iterable<Bookmark>> responseEntity = getEntityController().getBookmarksByUser(token.getUser().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(Iterables.contains(responseEntity.getBody(), bookmark));
    }

    @Test
    public void testGetBookmarksByUserInvalidIdentifier() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Iterable<Bookmark>> responseEntity = getEntityController().getBookmarksByUser(getTestIdentifierInvalid(), token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetBookmarksByUserNullToken() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Iterable<Bookmark>> responseEntity = getEntityController().getBookmarksByUser(token.getUser().getIdentifier(), null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testGetBookmarksByUserInvalidToken() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenInvalid(bookmark.getSource());

        // Execution
        ResponseEntity<Iterable<Bookmark>> responseEntity = getEntityController().getBookmarksByUser(token.getUser().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testPostBookmark() {
        // Test data
        Bookmark bookmark = getTestEntityTransient();
        Token token = getTokenPersistent(bookmark.getSource());

        // Test frame
        createHandlePostEndpointTest((e, t) -> getEntityController().postBookmark(e.getSource().getIdentifier(), e, t), bookmark, token);
    }

    @Test
    public void testPostBookmarkInvalidUserIdentifier() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Bookmark> responseEntity = getEntityController().postBookmark(getTestIdentifierInvalid(), bookmark, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteBookmark() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Test frame
        createHandleDeleteByEntityEndpointTest((e, t) -> getEntityController().deleteBookmark(e.getSource().getIdentifier(), e, t), bookmark, token);
    }

    @Test
    public void testDeleteBookmarkInvalidUserIdentifier() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteBookmark(getTestIdentifierInvalid(), bookmark, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        // Test data
        Bookmark bookmark = getTestEntityTransient();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Bookmark> responseEntity = getEntityController().handlePost(bookmark, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Override
    public void testHandlePostMultipleEntities() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 3;
        List<Bookmark> bookmarks = new LinkedList<>();
        repeat(entityAmount, i -> bookmarks.add(i, new Bookmark(user, getOfferPersistent(getUserPersistent(Role.USER)))));

        // Execution
        List<ResponseEntity<Bookmark>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(bookmarks.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
        responses.forEach(r -> assertNotNull(r.getBody()));
    }

    @Override
    public void testHandlePostEntityConflict() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Bookmark> responseEntity = getEntityController().handlePost(bookmark, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePut

    @Override
    public void testHandlePutUnknownEntity() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        getEntityController().getEntityService().delete(bookmark);
        ResponseEntity<Bookmark> responseEntity = getEntityController().handlePut(bookmark.getIdentifier(), bookmark, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Override
    public void testHandlePutNullIdentifier() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Bookmark> responseEntity = getEntityController().handlePut(null, bookmark, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handleDelete

    @Override
    public void testHandleDeleteSingleEntity() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(bookmark, token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Override
    public void testHandleDeleteMultipleEntities() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 4;
        List<Bookmark> bookmarks = new LinkedList<>();
        repeat(entityAmount, i -> bookmarks.add(getEntityController().getEntityService().post(new Bookmark(user, getOfferPersistent(getUserPersistent(Role.USER))))));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(bookmarks.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandleDeleteExistingIdentifier() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(bookmark.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Override
    public void testHandleDeleteMultipleIdentifiers() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 4;
        List<Bookmark> bookmarks = new LinkedList<>();
        repeat(entityAmount, i -> bookmarks.add(getEntityController().getEntityService().post(new Bookmark(user, getOfferPersistent(getUserPersistent(Role.USER))))));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(bookmarks.get(i).getIdentifier(), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandleDeleteUnknownEntity() {
        // Test data
        Bookmark bookmark = getTestEntityPersistent();
        Token token = getTokenPersistent(bookmark.getSource());

        // Execution
        getEntityController().handleDelete(bookmark, token);
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(bookmark, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected Bookmark getTestEntityTransient() {
        return new Bookmark(getUserPersistent(Role.USER), getOfferPersistent(getUserPersistent(Role.USER)));
    }

    @Override
    protected Bookmark getTestEntityPersistent() {
        return getEntityController().getEntityService().post(getTestEntityTransient());
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidBookmarkIdentifier";
    }
}
