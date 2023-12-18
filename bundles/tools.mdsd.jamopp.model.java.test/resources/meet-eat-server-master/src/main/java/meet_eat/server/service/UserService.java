package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.UserRepository;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link User users} and their state persistence.
 */
@Service
public class UserService extends EntityService<User, String, UserRepository> {

    private static final Email EMAIL_SENDER = new Email("noreply.meet.eat@gmail.com");
    private static final String PASSWORD_RESET_SUBJECT = "Meet & Eat Password Reset";
    private static final String PASSWORD_RESET_TEXT_TEMPLATE = "Your new password is %s .";
    private static final int PASSWORD_BASIC_CHAR_COUNT = 15;
    private static final int PASSWORD_SPECIAL_CHAR_COUNT = 2;
    private static final int PASSWORD_DIGIT_COUNT = 5;

    private final OfferService offerService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final SubscriptionService subscriptionService;
    private final ParticipationService participationService;
    private final ReportService reportService;
    private final RatingService ratingService;
    private final BookmarkService bookmarkService;

    /**
     * Constructs a new instance of {@link UserService}.
     *
     * @param userRepository      the repository used for persistence operations
     * @param offerService        the service used for operations on and with {@link Offer} entities
     * @param tokenService        the service used for operations on and with {@link Token} entities
     * @param emailService        the service used for sending messages via {@link Email}
     * @param subscriptionService the service used for operations on and with {@link Subscription} entities
     */
    @Lazy
    @Autowired
    public UserService(UserRepository userRepository, OfferService offerService, TokenService tokenService,
                       EmailService emailService, SubscriptionService subscriptionService,
                       ParticipationService participationService, ReportService reportService,
                       RatingService ratingService, BookmarkService bookmarkService) {
        super(userRepository);
        this.offerService = offerService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.subscriptionService = subscriptionService;
        this.participationService = participationService;
        this.reportService = reportService;
        this.ratingService = ratingService;
        this.bookmarkService = bookmarkService;
    }

    /**
     * Gets a {@link User user} by {@link Email email} from the repository.
     *
     * @param email the {@link Email email} of an user.
     * @return a user identified by {@link Email email}
     */
    public Optional<User> getByEmail(Email email) {
        return getRepository().findOneByEmail(Objects.requireNonNull(email));
    }

    /**
     * Generates a new random {@link Password password} for an {@link User user} and sends it to the given email address.
     *
     * @param emailAddress the address to send the password to
     */
    public void resetPassword(String emailAddress) {
        Email userEmail = new Email(emailAddress);
        Optional<User> optionalUser = getByEmail(userEmail);
        if (optionalUser.isPresent()) {
            // Generate a new password by using a password value supplier.
            PasswordValueSupplier passwordValueSupplier = new PasswordValueSupplier(PASSWORD_BASIC_CHAR_COUNT,
                    PASSWORD_SPECIAL_CHAR_COUNT, PASSWORD_DIGIT_COUNT);
            String passwordValue = passwordValueSupplier.get();
            Password password = Password.createHashedPassword(passwordValue);

            // Send an email with the new password to the user.
            String emailText = String.format(PASSWORD_RESET_TEXT_TEMPLATE, passwordValue);
            emailService.sendEmail(EMAIL_SENDER, userEmail, PASSWORD_RESET_SUBJECT, emailText);

            // Write back the new password to the repository.
            User user = optionalUser.get();
            user.setPassword(password);
            put(user);
        }
    }

    @Override
    public User post(User entity) {
        Password derivedPassword = entity.getPassword().derive(Password.generateSalt(), SecurityService.PASSWORD_ITERATION_COUNT);
        entity.setPassword(derivedPassword);
        return super.post(entity);
    }

    @Override
    public User put(User entity) {
        if (hasModifiedPassword(entity)) {
            Password derivedPassword = entity.getPassword().derive(entity.getIdentifier(), SecurityService.PASSWORD_ITERATION_COUNT);
            entity.setPassword(derivedPassword);
        }
        return super.put(entity);
    }

    @Override
    public void delete(User entity) {
        Objects.requireNonNull(entity);

        // Cascading deletion of non-relation entities
        offerService.deleteByCreator(entity);
        tokenService.deleteByUser(entity);

        // Cascading deletion of relation entities
        subscriptionService.deleteBySourceOrTarget(entity, entity);
        bookmarkService.deleteBySource(entity);
        participationService.deleteBySource(entity);
        reportService.deleteBySourceOrTarget(entity, entity);
        ratingService.deleteBySourceOrTarget(entity, entity);

        super.delete(entity);
    }

    @Override
    public void delete(String identifier) {
        Objects.requireNonNull(identifier);

        // Cascading deletion of non-relation entities
        offerService.deleteByCreator(identifier);
        tokenService.deleteByUser(identifier);

        // Cascading deletion of relations
        Optional<User> optionalUser = get(identifier);
        optionalUser.ifPresent(user -> subscriptionService.deleteBySourceOrTarget(user, user));
        optionalUser.ifPresent(bookmarkService::deleteBySource);
        optionalUser.ifPresent(participationService::deleteBySource);
        optionalUser.ifPresent(user -> reportService.deleteBySourceOrTarget(user, user));
        optionalUser.ifPresent(user -> ratingService.deleteBySourceOrTarget(user, user));

        super.delete(identifier);
    }

    @Override
    public boolean existsPostConflict(User entity) {
        return existsEmailConflict(entity) || super.existsPostConflict(entity);
    }

    @Override
    public boolean existsPutConflict(User entity) {
        return existsEmailConflict(entity);
    }

    /**
     * Signalizes whether an {@link Email email} is already taken by another {@link User user}.
     *
     * @param user the user to be checked for email conflict
     * @return True if an conflict exists, false otherwise
     */
    private boolean existsEmailConflict(User user) {
        Optional<User> optionalUserByEmail = getByEmail(user.getEmail());
        return optionalUserByEmail.isPresent()
                && !optionalUserByEmail.get().getIdentifier().equals(user.getIdentifier());
    }

    /**
     * Signalizes whether the {@link Password password} of an {@link User user} has been modified.
     *
     * @param user the user whose password will be checked
     * @return True if the password has been modified, false otherwise
     */
    private boolean hasModifiedPassword(User user) {
        Optional<User> optionalPersistentUser = getRepository().findById(user.getIdentifier());
        if (optionalPersistentUser.isPresent()) {
            User persistentUser = optionalPersistentUser.get();
            return !persistentUser.getPassword().equals(user.getPassword());
        }
        return false;
    }
}
