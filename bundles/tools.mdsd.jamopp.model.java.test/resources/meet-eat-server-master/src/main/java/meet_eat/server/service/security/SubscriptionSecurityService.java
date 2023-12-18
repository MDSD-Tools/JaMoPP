package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing security relevant functionality mainly for {@link Subscription subscriptions}.
 */
@Service
public class SubscriptionSecurityService extends SecurityService<Subscription> {

    /**
     * Constructs a new {@link SubscriptionSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link SecurityService}
     */
    @Autowired
    public SubscriptionSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(Subscription entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken)
                && entity.getSource().equals(authenticationToken.getUser());
    }

    @Override
    public boolean isLegalPut(Subscription entity, Token authenticationToken) {
        return false;
    }

    @Override
    public boolean isLegalDelete(Subscription entity, Token authenticationToken) {
        return isLegalPost(entity, authenticationToken);
    }
}
