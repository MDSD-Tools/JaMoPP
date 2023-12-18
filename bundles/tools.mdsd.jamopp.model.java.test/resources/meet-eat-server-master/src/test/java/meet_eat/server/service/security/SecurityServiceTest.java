package meet_eat.server.service.security;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.server.service.OfferService;
import meet_eat.server.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class SecurityServiceTest<T extends SecurityService<S>, S extends Entity<?>> {

    private static final String PASSWORD_VALID_VALUE = "AbcdefgTest1234!?";

    private static int userCount = 0;
    private static int offerCount = 0;

    @Autowired
    private T securityService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfferService offerService;

    @Before
    public void prepareRepositories() {
        securityService.getTokenService().getRepository().deleteAll();
        userService.getRepository().deleteAll();
        offerService.getRepository().deleteAll();
    }

    // isLegalGet

    @Test
    public void testIsLegalGetBasicToken() {
        // Test data
        Token token = getValidToken(getBasicUser());

        // Assertions
        assertTrue(getSecurityService().isLegalGet(token));
    }

    @Test
    public void testIsLegalGetModeratorToken() {
        // Test data
        Token token = getValidToken(getModeratorUser());

        // Assertions
        assertTrue(getSecurityService().isLegalGet(token));
    }

    @Test
    public void testIsLegalGetAdminToken() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertTrue(getSecurityService().isLegalGet(token));
    }

    @Test
    public void testIsLegalGetNullToken() {
        // Assertions
        assertFalse(securityService.isLegalGet(null));
    }

    // isLegalPost

    @Test
    public void testIsLegalPostNullToken() {
        // Assertions
        assertThrows(NullPointerException.class, () -> securityService.isLegalPost(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPostNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertThrows(NullPointerException.class, () -> securityService.isLegalPost(null, token));
    }

    // isLegalPut

    @Test
    public void testIsLegalPutNullToken() {
        // Assertions
        assertThrows(NullPointerException.class, () -> securityService.isLegalPut(getTestEntity(), null));
    }

    @Test
    public void testIsLegalPutNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertThrows(NullPointerException.class, () -> securityService.isLegalPut(null, token));
    }

    // isLegalDelete

    @Test
    public void testIsLegalDeleteNullToken() {
        // Assertions
        assertThrows(NullPointerException.class, () -> securityService.isLegalDelete(getTestEntity(), null));
    }

    @Test
    public void testIsLegalDeleteNullEntity() {
        // Test data
        Token token = getValidToken(getAdminUser());

        // Assertions
        assertThrows(NullPointerException.class, () -> securityService.isLegalDelete(null, token));
    }


    // isValidAuthentication

    @Test
    public void testIsValidAuthenticationBasic() {
        // Test data
        Token validToken = getValidToken(getBasicUser());

        // Assertions
        assertTrue(securityService.isValidAuthentication(validToken));
    }

    @Test
    public void testIsValidAuthenticationModerator() {
        // Test data
        Token validToken = getValidToken(getModeratorUser());

        // Assertions
        assertTrue(securityService.isValidAuthentication(validToken));
    }

    @Test
    public void testIsValidAuthenticationAdmin() {
        // Test data
        Token validToken = getValidToken(getAdminUser());

        // Assertions
        assertTrue(securityService.isValidAuthentication(validToken));
    }

    @Test
    public void testIsValidAuthenticationNull() {
        // Assertions
        assertFalse(securityService.isValidAuthentication(null));
    }

    @Test
    public void testIsValidAuthenticationNoIdentifier() {
        // Test data
        Token validToken = getValidToken(getBasicUser());
        Token tokenWithoutIdentifier = new Token(validToken.getUser(), validToken.getValue());

        // Assertions
        assertFalse(securityService.isValidAuthentication(tokenWithoutIdentifier));
    }

    @Test
    public void testGetTokenService() {
        // Assertions
        assertNotNull(securityService.getTokenService());
    }

    protected abstract S getTestEntity();

    protected T getSecurityService() {
        return securityService;
    }

    protected Token getValidToken(User user) {
        Email email = user.getEmail();
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        return securityService.getTokenService().createToken(new LoginCredential(email, password));
    }

    protected Offer getValidOffer(User creator) {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JULY, 30, 12, 32);
        Localizable location = new CityLocation("Karlsruhe");
        Set<Tag> tags = new HashSet<>();
        Offer offer = new Offer(creator, tags, "Offer " + offerCount++,
                "Spaghetti. Mhmmm.", 4.99, 3, dateTime, location);
        return offerService.post(offer);
    }

    protected User getBasicUser() {
        return createUser(Role.USER);
    }

    protected User getModeratorUser() {
        return createUser(Role.MODERATOR);
    }

    protected User getAdminUser() {
        return createUser(Role.ADMIN);
    }

    private User createUser(Role role) {
        Email email = new Email("noreply" + userCount + ".meet.eat@example.com");
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        Localizable validLocalizable = new SphericalLocation(new SphericalPosition(0, 0));
        User user = new User(email, password, LocalDate.EPOCH, "User" + userCount, "12345" + userCount,
                "Description" + userCount, true, validLocalizable);
        user.setRole(role);
        userCount++;
        return userService.post(user);
    }
}
