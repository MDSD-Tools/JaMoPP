package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Participation;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Represents a service class providing security relevant functionality mainly for {@link Participation participations}.
 */
@Service
public class ParticipationSecurityService extends SecurityService<Participation> {

    /**
     * Constructs a new {@link ParticipationSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link ParticipationSecurityService}
     */
    @Autowired
    public ParticipationSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(Participation entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken)
                && Objects.equals(entity.getSource(), authenticationToken.getUser());
    }

    @Override
    public boolean isLegalPut(Participation entity, Token authenticationToken) {
        return false;
    }

    @Override
    public boolean isLegalDelete(Participation entity, Token authenticationToken) {
        return isLegalPost(entity, authenticationToken);
    }
}
