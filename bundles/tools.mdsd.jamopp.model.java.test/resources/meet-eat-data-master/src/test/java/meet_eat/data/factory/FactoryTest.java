package meet_eat.data.factory;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class FactoryTest {

    @Test
    public void testEmailFactory() {
        // Execution
        EmailFactory emailFactory = new EmailFactory();
        Email email1 = emailFactory.getValidObject();
        Email email2 = emailFactory.getValidObject();

        // Assertions
        assertNotEquals(email1, email2);
    }

    @Test
    public void testPasswordFactory() {
        // Execution
        PasswordFactory passwordFactory = new PasswordFactory();
        Password password1 = passwordFactory.getValidObject();
        Password password2 = passwordFactory.getValidObject();

        // Assertions
        assertNotEquals(password1, password2);
    }

    @Test
    public void testLocationFactory() {
        // Execution
        LocationFactory locationFactory = new LocationFactory();
        Localizable location1 = locationFactory.getValidObject();
        Localizable location2 = locationFactory.getValidObject();

        // Assertions
        assertNotEquals(location1, location2);
    }

    @Test
    public void testTagFactory() {
        // Execution
        TagFactory tagFactory = new TagFactory();
        Tag tag1 = tagFactory.getValidObject();
        Tag tag2 = tagFactory.getValidObject();

        // Assertions
        assertNotEquals(tag1, tag2);
    }

    @Test
    public void testUserFactory() {
        // Execution
        UserFactory userFactory = new UserFactory();
        User user1 = userFactory.getValidObject();
        User user2 = userFactory.getValidObject();

        // Assertions
        assertNotEquals(user1, user2);
    }

    @Test
    public void testOfferFactory() {
        // Execution
        OfferFactory offerFactory = new OfferFactory();
        Offer offer1 = offerFactory.getValidObject();
        Offer offer2 = offerFactory.getValidObject();

        // Assertions
        assertNotEquals(offer1, offer2);
    }
}
