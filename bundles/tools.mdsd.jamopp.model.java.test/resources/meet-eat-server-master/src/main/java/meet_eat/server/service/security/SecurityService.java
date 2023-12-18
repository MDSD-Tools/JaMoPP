package meet_eat.server.service.security;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import org.springframework.stereotype.Service;

/**
 * Represents an abstract service class providing security relevant functionality for specific {@link Entity entities}.
 *
 * @param <T> the type of {@link Entity entity} this service mainly provides functionality for
 */
@Service
public abstract class SecurityService<T extends Entity<?>> {

    /**
     * Represents the number of iterations used for password derivation
     */
    public static final int PASSWORD_ITERATION_COUNT = 100000;

    private final TokenService tokenService;

    /**
     * Constructs a new {@link SecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link SecurityService}
     */
    protected SecurityService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Signalizes whether a GET operation authenticated by a given {@link Token token} is legal or not.
     *
     * @param authenticationToken the token used for authentication
     * @return {@code true} if a GET operation is legal with the given token, {@code false} otherwise.
     */
    public abstract boolean isLegalGet(Token authenticationToken);

    /**
     * Signalizes whether a POST operation containing a given {@link Entity entity}
     * authenticated by a given {@link Token token} is legal or not.
     *
     * @param entity              the entity to be created by the POST operation
     * @param authenticationToken the token used for authentication
     * @return {@code true} if the POST operation is legal with the given token, {@code false} otherwise.
     */
    public abstract boolean isLegalPost(T entity, Token authenticationToken);

    /**
     * Signalizes whether a PUT operation on a given {@link Entity entity}
     * authenticated by a given {@link Token token} is legal or not.
     *
     * @param entity              the entity to be modified by the PUT operation
     * @param authenticationToken the token used for authentication
     * @return {@code true} if the PUT operation is legal with the given token, {@code false} otherwise.
     */
    public abstract boolean isLegalPut(T entity, Token authenticationToken);

    /**
     * Signalizes whether a DELETE operation of a given {@link Entity entity}
     * authenticated by a given {@link Token token} is legal or not.
     *
     * @param entity              the entity to be deleted by the DELETE operation
     * @param authenticationToken the token used for authentication
     * @return {@code true} if the DELETE operation is legal with the given token, {@code false} otherwise.
     */
    public abstract boolean isLegalDelete(T entity, Token authenticationToken);

    /**
     * Signalizes whether a given {@link Token token} is valid and may be used for authentication or not.
     *
     * @param authenticationToken the token that is tested for validity
     * @return {@code true} if the given token is valid, {@code false} otherwise.
     */
    public boolean isValidAuthentication(Token authenticationToken) {
        return tokenService.isValidToken(authenticationToken);
    }

    /**
     * Gets the {@link TokenService token service} of this {@link SecurityService security service}.
     *
     * @return the token service.
     */
    protected TokenService getTokenService() {
        return tokenService;
    }
}
