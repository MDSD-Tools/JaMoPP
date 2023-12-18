package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.user.Role;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Represents a service class providing security relevant functionality mainly for {@link Rating ratings}.
 */
@Service
public class RatingSecurityService extends SecurityService<Rating> {

    /**
     * Constructs a new {@link RatingSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link RatingSecurityService}
     */
    @Autowired
    public RatingSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(Rating entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken)
                && Objects.equals(entity.getSource(), authenticationToken.getUser());
    }

    @Override
    public boolean isLegalPut(Rating entity, Token authenticationToken) {
        return false;
    }

    @Override
    public boolean isLegalDelete(Rating entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken)
                && Objects.equals(authenticationToken.getUser().getRole(), Role.ADMIN);
    }
}
