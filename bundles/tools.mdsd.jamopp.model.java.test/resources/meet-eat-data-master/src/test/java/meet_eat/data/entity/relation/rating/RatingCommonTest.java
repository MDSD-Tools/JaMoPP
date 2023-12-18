package meet_eat.data.entity.relation.rating;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.User;
import meet_eat.data.factory.UserFactory;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RatingCommonTest {

    @Test
    public void testCreateHostRating() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        User host = userFactory.getValidObject();
        // Offer
        Set<Tag> tags = new HashSet<>();
        String name = "Test offer";
        String description = "This is a test description.";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2050, Month.OCTOBER, 16, 15, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Offer offer = new Offer(host, tags, name, description, price, maxParticipants, dateTime, location);
        RatingValue value = RatingValue.POINTS_3;

        // Execution
        Rating rating = Rating.createHostRating(guest, offer, value);

        // Assertions
        assertNotNull(rating);
        assertNotNull(rating.getSource());
        assertNotNull(rating.getTarget());
        assertNotNull(rating.getOffer());
        assertNotNull(rating.getBasis());
        assertNotNull(rating.getValue());
        assertEquals(guest, rating.getSource());
        assertEquals(rating.getOffer().getCreator(), rating.getTarget());
        assertEquals(RatingBasis.HOST, rating.getBasis());
        assertEquals(value, rating.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateHostRatingNullOffer() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        RatingValue value = RatingValue.POINTS_3;

        // Execution
        Rating.createHostRating(guest, null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateHostRatingNullValue() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        User host = userFactory.getValidObject();
        // Offer
        Set<Tag> tags = new HashSet<>();
        String name = "Test offer";
        String description = "This is a test description.";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2050, Month.OCTOBER, 16, 15, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Offer offer = new Offer(host, tags, name, description, price, maxParticipants, dateTime, location);

        // Execution
        Rating.createHostRating(guest, offer, null);
    }

    @Test
    public void testCreateGuestRating() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        User host = userFactory.getValidObject();
        // Offer
        Set<Tag> tags = new HashSet<>();
        String name = "Test offer";
        String description = "This is a test description.";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2050, Month.OCTOBER, 16, 15, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Offer offer = new Offer(host, tags, name, description, price, maxParticipants, dateTime, location);
        RatingValue value = RatingValue.POINTS_3;

        // Execution
        Rating rating = Rating.createGuestRating(guest, offer, value);

        // Assertions
        assertNotNull(rating);
        assertNotNull(rating.getSource());
        assertNotNull(rating.getTarget());
        assertNotNull(rating.getOffer());
        assertNotNull(rating.getBasis());
        assertNotNull(rating.getValue());
        assertEquals(rating.getOffer().getCreator(), rating.getSource());
        assertEquals(guest, rating.getTarget());
        assertEquals(RatingBasis.GUEST, rating.getBasis());
        assertEquals(value, rating.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateGuestRatingNullOffer() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        RatingValue value = RatingValue.POINTS_3;

        // Execution
        Rating.createGuestRating(guest, null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateGuestRatingNullValue() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        User host = userFactory.getValidObject();
        // Offer
        Set<Tag> tags = new HashSet<>();
        String name = "Test offer";
        String description = "This is a test description.";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2050, Month.OCTOBER, 16, 15, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Offer offer = new Offer(host, tags, name, description, price, maxParticipants, dateTime, location);

        // Execution
        Rating.createGuestRating(guest, offer, null);
    }

    @Test
    public void testJsonConstructor() {
        // Test data
        String identifier = "fif1093r0urf";
        UserFactory userFactory = new UserFactory();
        User guest = userFactory.getValidObject();
        User host = userFactory.getValidObject();
        // Offer
        Set<Tag> tags = new HashSet<>();
        String name = "Test offer";
        String description = "This is a test description.";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2050, Month.OCTOBER, 16, 15, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Offer offer = new Offer(host, tags, name, description, price, maxParticipants, dateTime, location);
        RatingValue value = RatingValue.POINTS_3;

        // Execution
        Rating rating = new Rating(identifier, guest, host, offer, value, RatingBasis.HOST);

        // Assertions
        assertNotNull(rating);
        assertNotNull(rating.getIdentifier());
        assertNotNull(rating.getSource());
        assertNotNull(rating.getTarget());
        assertNotNull(rating.getOffer());
        assertNotNull(rating.getBasis());
        assertNotNull(rating.getValue());
        assertEquals(identifier, rating.getIdentifier());
        assertEquals(guest, rating.getSource());
        assertEquals(rating.getOffer().getCreator(), rating.getTarget());
        assertEquals(RatingBasis.HOST, rating.getBasis());
        assertEquals(value, rating.getValue());
    }
}
