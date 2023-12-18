package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Represents a service class providing security relevant functionality mainly for {@link Bookmark bookmarks}.
 */
@Service
public class BookmarkSecurityService extends SecurityService<Bookmark> {

    /**
     * Constructs a new {@link BookmarkSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link SecurityService}
     */
    @Autowired
    public BookmarkSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(Bookmark entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken)
                && Objects.equals(entity.getSource(), authenticationToken.getUser());
    }

    @Override
    public boolean isLegalPut(Bookmark entity, Token authenticationToken) {
        return false;
    }

    @Override
    public boolean isLegalDelete(Bookmark entity, Token authenticationToken) {
        return isLegalPost(entity, authenticationToken);
    }
}
