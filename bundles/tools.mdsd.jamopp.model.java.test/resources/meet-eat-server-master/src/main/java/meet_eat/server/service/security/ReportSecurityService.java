package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.Role;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Represents a service class providing security relevant functionality mainly for {@link Report reports}.
 */
@Service
public class ReportSecurityService extends SecurityService<Report> {

    /**
     * Constructs a new {@link ReportSecurityService} instance.
     *
     * @param tokenService the {@link TokenService} to be used by the {@link ReportSecurityService}
     */
    @Autowired
    public ReportSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        boolean isModerator = authenticationToken.getUser().getRole().equals(Role.MODERATOR);
        boolean isAdmin = authenticationToken.getUser().getRole().equals(Role.ADMIN);
        return isValidAuthentication(authenticationToken)
                && (isModerator || isAdmin);
    }

    @Override
    public boolean isLegalPost(Report entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken)
                && Objects.equals(entity.getSource(), authenticationToken.getUser());
    }

    @Override
    public boolean isLegalPut(Report entity, Token authenticationToken) {
        return isLegalGet(authenticationToken);
    }

    @Override
    public boolean isLegalDelete(Report entity, Token authenticationToken) {
        boolean isAdmin = authenticationToken.getUser().getRole().equals(Role.ADMIN);
        return isValidAuthentication(authenticationToken) && isAdmin;
    }
}
