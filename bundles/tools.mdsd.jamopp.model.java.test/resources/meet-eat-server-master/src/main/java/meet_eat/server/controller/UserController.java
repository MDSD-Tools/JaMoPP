package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.UserService;
import meet_eat.server.service.security.SecurityService;
import meet_eat.server.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link User} entities.
 */
@RestController
public class UserController extends EntityController<User, String, UserService> {

    private static final String PATH_VARIABLE_EMAIL = "email";
    private static final String URI_PATH_SEGMENT_EMAIL = "/{" + PATH_VARIABLE_EMAIL + "}";
    private static final String URI_PATH_SEGMENT_PASSWORD_RESET = "/password/reset";

    /**
     * Constructs a new instance of {@link UserController}.
     *
     * @param userService         the {@link EntityService} used by this controller
     * @param userSecurityService the {@link SecurityService} used by this controller
     */
    @Autowired
    public UserController(UserService userService, UserSecurityService userSecurityService) {
        super(userService, userSecurityService);
    }

    // GET

    /**
     * Gets a {@link User user} identified by its identifier from the persistence layer.
     *
     * @param identifier the identifier of the tag to be got
     * @param token      the authentication token of the requester
     * @return the identified user within a {@link ResponseEntity}
     */
    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<User> getUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                        @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGet(identifier, token);
    }

    // POST

    /**
     * Posts a new {@link User user} into the persistence layer.
     *
     * @param user  the user to be posted
     * @param token the authentication token of the requester
     * @return the posted user within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.USERS)
    public ResponseEntity<User> postUser(@RequestBody User user,
                                         @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(user, token);
    }

    @Override
    protected ResponseEntity<User> handlePost(User entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!getSecurityService().isLegalPost(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (getEntityService().existsPostConflict(entity)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User postedUser = getEntityService().post(entity);
        return new ResponseEntity<>(postedUser, HttpStatus.CREATED);
    }

    /**
     * Resets the {@link meet_eat.data.entity.user.Password password} of an {@link User user} by generating a new
     * password and sending it to the user's {@link Email email}.
     *
     * @param emailAddress the address for identification and to send the password to
     * @return a bodiless {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.USERS + URI_PATH_SEGMENT_EMAIL + URI_PATH_SEGMENT_PASSWORD_RESET)
    public ResponseEntity<Void> postPasswordReset(@PathVariable(value = PATH_VARIABLE_EMAIL) String emailAddress) {
        // No errors are sent to the caller in order to avoid brute force searches identifying valid email addresses.
        if (Email.isLegalEmailAddress(emailAddress)) {
            getEntityService().resetPassword(emailAddress);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // PUT

    /**
     * Puts a modified {@link User user} into the persistence layer.
     *
     * @param user  the user to be put
     * @param token the authentication token of the requester
     * @return the put user within a {@link ResponseEntity}
     */
    @PutMapping(EndpointPath.USERS)
    public ResponseEntity<User> putUser(@RequestBody User user,
                                        @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(null, user, token);
    }

    /**
     * Puts a modified {@link User user} into the persistence layer.
     *
     * @param identifier the identifier of the user to be put
     * @param user       the user to be put
     * @param token      the authentication token of the requester
     * @return the put user within a {@link ResponseEntity}
     */
    @PutMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<User> putUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                        @RequestBody User user,
                                        @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(identifier, user, token);
    }

    // DELETE

    /**
     * Deletes an {@link User user} from the persistence layer.
     *
     * @param user  the user to be deleted
     * @param token the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.USERS)
    public ResponseEntity<Void> deleteUser(@RequestBody User user,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(user, token);
    }

    /**
     * Deletes an {@link User user} from the persistence layer.
     *
     * @param identifier the identifier of the user to be deleted
     * @param token      the authentication token of the requester
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Void> deleteUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(identifier, token);
    }
}
