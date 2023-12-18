package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing security relevant functionality mainly for {@link Token tokens}.
 */
@Service
public class TokenSecurityService extends SecurityService<Token> {

    /**
     * Constructs a new {@link TokenSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link TokenSecurityService}
     */
    @Autowired
    protected TokenSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLegalPost(Token entity, Token authenticationToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLegalPut(Token entity, Token authenticationToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLegalDelete(Token entity, Token authenticationToken) {
        throw new UnsupportedOperationException();
    }
}
