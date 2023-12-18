package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.TokenService;
import meet_eat.server.service.security.SecurityService;
import meet_eat.server.service.security.TokenSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link Token} entities.
 */
@RestController
public class TokenController extends EntityController<Token, String, TokenService> {

    /**
     * Constructs a new instance of {@link TokenController}.
     *
     * @param tokenService         the {@link EntityService} used by this controller
     * @param tokenSecurityService the {@link SecurityService} used by this controller
     */
    @Autowired
    public TokenController(TokenService tokenService, TokenSecurityService tokenSecurityService) {
        super(tokenService, tokenSecurityService);
    }

    /**
     * Signalizes whether a given {@link Token token} is valid or not.
     *
     * @param token the token to be checked for validity
     * @return {@code true} if the token is valid, {@link false} otherwise
     */
    @PostMapping(EndpointPath.TOKENS + EndpointPath.VALIDITY)
    public ResponseEntity<Boolean> isValidToken(@RequestBody Token token) {
        return new ResponseEntity<>(getEntityService().isValidToken(token), HttpStatus.OK);
    }

    /**
     * Creates a new persistent {@link Token} that can be used for authentication purposes.
     *
     * @param loginCredential the {@link LoginCredential} to verify the login request
     * @return the created token within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.LOGIN)
    public ResponseEntity<Token> login(@RequestBody LoginCredential loginCredential) {
        if (!getEntityService().isValidLoginCredential(loginCredential)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Token token = getEntityService().createToken(loginCredential);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    /**
     * Deletes a {@link Token} from the persistence layer.
     *
     * @param token the token to be deleted
     * @return a bodiless {@link ResponseEntity}
     */
    @DeleteMapping(EndpointPath.LOGOUT)
    public ResponseEntity<Void> logout(@RequestBody Token token) {
        if (!getEntityService().exists(token.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        getEntityService().delete(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
