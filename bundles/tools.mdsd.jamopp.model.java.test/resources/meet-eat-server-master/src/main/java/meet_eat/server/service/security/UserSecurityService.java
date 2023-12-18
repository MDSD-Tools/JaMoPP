package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing security relevant functionality mainly for {@link User users}.
 */
@Service
public class UserSecurityService extends SecurityService<User> {

    /**
     * Constructs a new {@link UserSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link UserSecurityService}
     */
    @Autowired
    protected UserSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(User entity, Token authenticationToken) {
        boolean isAuthenticatedAdmin = isValidAuthentication(authenticationToken) &&
                authenticationToken.getUser().getRole().equals(Role.ADMIN);
        boolean isBasicUser = entity.getRole().equals(Role.USER);

        return isAuthenticatedAdmin || isBasicUser;
    }

    @Override
    public boolean isLegalPut(User entity, Token authenticationToken) {
        boolean isAdmin = authenticationToken.getUser().getRole().equals(Role.ADMIN);
        boolean isEntityOwner = authenticationToken.getUser().getIdentifier().equals(entity.getIdentifier());

        return isValidAuthentication(authenticationToken) && (isAdmin || isEntityOwner);
    }

    @Override
    public boolean isLegalDelete(User entity, Token authenticationToken) {
        return isLegalPut(entity, authenticationToken);
    }
}
