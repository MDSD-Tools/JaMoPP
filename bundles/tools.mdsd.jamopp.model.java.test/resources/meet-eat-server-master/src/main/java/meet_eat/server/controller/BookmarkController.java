package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.BookmarkService;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link Bookmark} entities.
 */
@RestController
public class BookmarkController extends EntityController<Bookmark, String, BookmarkService> {

    /**
     * Constructs a new instance of {@link BookmarkController}.
     *
     * @param entityService   the {@link EntityService} used by this controller
     * @param securityService the {@link SecurityService} used by this controller
     */
    @Lazy
    @Autowired
    public BookmarkController(BookmarkService entityService, SecurityService<Bookmark> securityService) {
        super(entityService, securityService);
    }

    // GET

    /**
     * Gets all persistent {@link Bookmark bookmarks} of an identified user from the persistence layer.
     *
     * @param userIdentifier the identifier of the {@link User} whose bookmarks are requested
     * @param token          the authentication token of the requester
     * @return all requested bookmarks within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.BOOKMARKS)
    public ResponseEntity<Iterable<Bookmark>> getBookmarksByUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                                 @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Check if authentication is valid
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get the bookmarks of the user
        Optional<Iterable<Bookmark>> optionalBookmarks = getEntityService().getByUserIdentifier(userIdentifier);
        if (optionalBookmarks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalBookmarks.get(), HttpStatus.OK);
    }

    // Post

    /**
     * Posts a new {@link Bookmark bookmark} into the persistence layer.
     *
     * @param userIdentifier the identifier of the {@link User user} of the bookmark
     * @param bookmark       the bookmark to be posted
     * @param token          the authentication token of the requester
     * @return the posted bookmark within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.BOOKMARKS)
    public ResponseEntity<Bookmark> postBookmark(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                 @RequestBody Bookmark bookmark,
                                                 @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!userIdentifier.equals(bookmark.getSource().getIdentifier())) {
            // Signalize a conflict between bookmark's user and path identifier variable
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return handlePost(bookmark, token);
    }

    // Delete

    /**
     * Deletes a {@link Bookmark bookmark} from the persistence layer.
     *
     * @param userIdentifier the identifier of the {@link User user} of the bookmark
     * @param bookmark       the bookmark to be deleted
     * @param token          the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.BOOKMARKS)
    public ResponseEntity<Void> deleteBookmark(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                               @RequestBody Bookmark bookmark,
                                               @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!userIdentifier.equals(bookmark.getSource().getIdentifier())) {
            // Signalize a conflict between bookmark's user and path identifier variable
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return handleDelete(bookmark, token);
    }
}
