package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.TagService;
import meet_eat.server.service.security.SecurityService;
import meet_eat.server.service.security.TagSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link Tag} entities.
 */
@RestController
public class TagController extends EntityController<Tag, String, TagService> {

    /**
     * Constructs a new instance of {@link TagController}.
     *
     * @param tagService         the {@link EntityService} used by this controller
     * @param tagSecurityService the {@link SecurityService} used by this controller
     */
    @Autowired
    public TagController(TagService tagService, TagSecurityService tagSecurityService) {
        super(tagService, tagSecurityService);
    }

    // GET

    /**
     * Gets all {@link Tag tags} from the persistence layer.
     *
     * @param token the authentication token of the requester
     * @return all available tags within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.TAGS)
    public ResponseEntity<Iterable<Tag>> getAllTags(@RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGetAll(token);
    }

    /**
     * Gets a {@link Tag tag} identified by its identifier from the persistence layer.
     *
     * @param identifier the identifier of the tag to be got
     * @param token      the authentication token of the requester
     * @return the identified tag within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.TAGS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Tag> getTag(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGet(identifier, token);
    }

    // POST

    /**
     * Posts a new {@link Tag tag} into the persistence layer.
     *
     * @param tag   the tag to be posted
     * @param token the authentication token of the requester
     * @return the posted tag within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.TAGS)
    public ResponseEntity<Tag> postTag(@RequestBody Tag tag,
                                       @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(tag, token);
    }

    // PUT

    /**
     * Puts a modified {@link Tag tag} into the persistence layer.
     *
     * @param tag   the tag to be put
     * @param token the authentication token of the requester
     * @return the put tag within a {@link ResponseEntity}
     */
    @PutMapping(EndpointPath.TAGS)
    public ResponseEntity<Tag> putTag(@RequestBody Tag tag,
                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(null, tag, token);
    }

    /**
     * Puts a modified {@link Tag tag} identified by its identifier into the persistence layer.
     *
     * @param identifier the identifier of the tag to be put
     * @param tag        the tag to be put
     * @param token      the authentication token of the requester
     * @return the put tag within a {@link ResponseEntity}
     */
    @PutMapping(EndpointPath.TAGS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Tag> putTag(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                      @RequestBody Tag tag,
                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(identifier, tag, token);
    }

    // DELETE

    /**
     * Deletes a {@link Tag tag} from the persistence layer.
     *
     * @param tag   the tag to be deleted
     * @param token the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.TAGS)
    public ResponseEntity<Void> deleteTag(@RequestBody Tag tag,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(tag, token);
    }

    /**
     * Deletes a {@link Tag tag} identified by its identifier from the persistence layer.
     *
     * @param identifier the identifier of the tag to be deleted
     * @param token      the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.TAGS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Void> deleteTag(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(identifier, token);
    }
}
