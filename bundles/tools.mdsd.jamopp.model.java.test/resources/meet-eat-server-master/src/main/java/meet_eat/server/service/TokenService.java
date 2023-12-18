package meet_eat.server.service;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Token tokens} and their state persistence.
 */
@Service
public class TokenService extends EntityService<Token, String, TokenRepository> {

    private static final String ERROR_MESSAGE_INVALID_LOGIN_CREDENTIALS = "Given login credentials must be valid.";

    private final UserService userService;

    /**
     * Constructs a new instance of {@link TokenService}.
     *
     * @param tokenRepository the repository used for persistence operations
     * @param userService     the service used for operations on and with {@link User} entities
     */
    @Lazy
    @Autowired
    public TokenService(TokenRepository tokenRepository, UserService userService) {
        super(tokenRepository);
        this.userService = userService;
    }

    /**
     * Creates and returns a new persistent and distinct {@link Token} instance.
     *
     * @param loginCredential the {@link LoginCredential} on which the token is based
     * @return a new persistent {@link Token}
     */
    public Token createToken(LoginCredential loginCredential) {
        // Check whether the user exists and login credentials are valid.
        Objects.requireNonNull(loginCredential);
        Optional<User> optionalUser = userService.getByEmail(loginCredential.getEmail());
        if (optionalUser.isEmpty() || !isValidLoginCredential(loginCredential)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_LOGIN_CREDENTIALS);
        }

        // Generate a random salt for the token hash value
        String salt = Password.generateSalt();

        // Concat the salt's bytes, email and current time to get a string with "high entropy", randomness respectively.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(salt);
        stringBuilder.append(loginCredential.getEmail());
        stringBuilder.append(LocalDateTime.now());

        // Hash the generated string, create the token and insert it into the repository.
        String tokenValue = Hashing.sha256().hashString(stringBuilder, Charsets.UTF_16).toString();
        return post(new Token(optionalUser.get(), tokenValue));
    }

    /**
     * Signalizes whether given {@link LoginCredential loginCredentials} are valid or not.
     *
     * @param loginCredential the {@link LoginCredential} to checked for validity
     * @return True if the {@link LoginCredential} instance is valid, false otherwise
     */
    public boolean isValidLoginCredential(LoginCredential loginCredential) {
        if (Objects.isNull(loginCredential)) {
            return false;
        }
        Optional<User> optionalUser = userService.getByEmail(loginCredential.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return loginCredential.getPassword().matches(user.getPassword());
        }
        return false;
    }

    /**
     * Signalizes whether a given token is valid or not.
     *
     * @param token the token to be checked for validity
     * @return True if the given token is valid, false otherwise
     */
    public boolean isValidToken(Token token) {
        if (Objects.isNull(token) || Objects.isNull(token.getIdentifier())) {
            return false;
        }
        Optional<Token> repoToken = getRepository().findById(token.getIdentifier());
        return repoToken.isPresent()
                && token.getValue().equals(repoToken.get().getValue())
                && token.getUser().getIdentifier().equals(repoToken.get().getUser().getIdentifier());
    }

    /**
     * Deletes all {@link Token tokens} from the repository identified by their {@link User user}.
     *
     * @param user the user of the tokens to be deleted
     */
    public void deleteByUser(User user) {
        getRepository().deleteByUser(Objects.requireNonNull(user));
    }

    /**
     * Deletes all {@link Token tokens} from the repository identified by their {@link User user's} identifier.
     *
     * @param userId the identifier of the user
     */
    public void deleteByUser(String userId) {
        Optional<User> optionalUser = userService.get(userId);
        optionalUser.ifPresent(this::deleteByUser);
    }
}
