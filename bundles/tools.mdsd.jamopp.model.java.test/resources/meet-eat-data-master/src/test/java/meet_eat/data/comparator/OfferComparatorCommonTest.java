package meet_eat.data.comparator;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.LocationFactory;
import meet_eat.data.factory.OfferFactory;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.PostcodeLocation;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OfferComparatorCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        OfferComparableField field = OfferComparableField.DISTANCE;
        Localizable location = new LocationFactory().getValidObject();

        // Execution
        OfferComparator comparator = new OfferComparator(field, location);

        // Assertions
        assertNotNull(comparator);
        assertNotNull(comparator.getField());
        assertNotNull(comparator.getLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullField() {
        // Test data
        Localizable location = new LocationFactory().getValidObject();

        // Execution
        new OfferComparator(null, location);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullLocation() {
        // Test data
        OfferComparableField field = OfferComparableField.DISTANCE;

        // Execution
        new OfferComparator(field, null);
    }

    @Test
    public void testPrice() {
        // Test data
        OfferComparableField price = OfferComparableField.PRICE;
        Localizable location = new LocationFactory().getValidObject();
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();
        offerOne.setPrice(5d);
        offerTwo.setPrice(8d);
        offerThree.setPrice(2d);
        offerFour.setPrice(11d);
        offerFive.setPrice(20d);
        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(price, location);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        // Sort list from small to big
        list.sort(comparator);

        assertEquals(offerThree, list.get(0));
        assertEquals(offerOne, list.get(1));
        assertEquals(offerTwo, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        // Reverse list order from big to small
        list.sort(comparator.reversed());

        assertEquals(offerFive, list.get(0));
        assertEquals(offerFour, list.get(1));
        assertEquals(offerTwo, list.get(2));
        assertEquals(offerOne, list.get(3));
        assertEquals(offerThree, list.get(4));
    }

    @Test
    public void testTime() {
        // Test data
        OfferComparableField time = OfferComparableField.TIME;
        Localizable location = new LocationFactory().getValidObject();
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();
        offerOne.setDateTime(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0));
        offerTwo.setDateTime(LocalDateTime.of(1950, Month.JANUARY, 1, 0, 0));
        offerThree.setDateTime(LocalDateTime.of(1975, Month.JANUARY, 1, 0, 0));
        offerFour.setDateTime(LocalDateTime.of(2010, Month.JANUARY, 1, 0, 0));
        offerFive.setDateTime(LocalDateTime.of(1940, Month.JANUARY, 1, 0, 0));
        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(time, location);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        // Sort list from early to late
        list.sort(comparator);

        assertEquals(offerFive, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerOne, list.get(3));
        assertEquals(offerFour, list.get(4));

        // Reverse list order from late to early
        list.sort(comparator.reversed());

        assertEquals(offerFour, list.get(0));
        assertEquals(offerOne, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerTwo, list.get(3));
        assertEquals(offerFive, list.get(4));
    }

    @Test
    public void testDistance() {
        // Test data
        OfferComparableField distance = OfferComparableField.DISTANCE;
        Localizable location = new CityLocation("Karlsruhe");
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();
        offerOne.setLocation(new CityLocation("Stuttgart"));
        offerTwo.setLocation(new CityLocation("München"));
        // Berlin Mitte
        offerThree.setLocation(new PostcodeLocation("10115"));
        // Hamburg
        offerFour.setLocation(new SphericalLocation(new SphericalPosition(53.550341, 10.000654)));
        // Bad Herrenalb
        offerFive.setLocation(new PostcodeLocation("76332"));

        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(distance, location);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        /*
         * Approx. distances from Karlsruhe to:
         * Stuttgart        - 62,48 km
         * München          - 252,90 km
         * Berlin Mitte     - 525,56 km
         * Hamburg          - 516,82 km
         * Bad Herrenalb    - 24,04 km
         */

        // Sort list from close to far
        list.sort(comparator);

        assertEquals(offerFive, list.get(0));
        assertEquals(offerOne, list.get(1));
        assertEquals(offerTwo, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerThree, list.get(4));

        // Reverse list order from far to close
        list.sort(comparator.reversed());

        assertEquals(offerThree, list.get(0));
        assertEquals(offerFour, list.get(1));
        assertEquals(offerTwo, list.get(2));
        assertEquals(offerOne, list.get(3));
        assertEquals(offerFive, list.get(4));
    }

    @Test
    public void testDistanceUnlocalizable() {
        // Test data
        OfferComparableField distance = OfferComparableField.DISTANCE;
        Localizable location = new CityLocation("Karlsruhe");
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();
        offerOne.setLocation(new CityLocation("Stuttgart"));
        offerTwo.setLocation(new CityLocation("abc123"));
        offerThree.setLocation(new CityLocation("Berlin"));
        offerFour.setLocation(new CityLocation("Hamburg"));
        offerFive.setLocation(new CityLocation("Bad Herrenalb"));

        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(distance, location);
        list.sort(comparator);

        // Assertions
        assertEquals(offerFive, list.get(0));
        assertEquals(offerOne, list.get(1));
        assertEquals(offerFour, list.get(2));
        assertEquals(offerThree, list.get(3));
        assertEquals(offerTwo, list.get(4));
    }

    @Test
    public void testParticipants() {
        // Test data
        OfferComparableField participants = OfferComparableField.PARTICIPANTS;
        Localizable location = new LocationFactory().getValidObject();
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();

        // Add a map able to return the number of participants an offer has
        Map<Offer, Integer> participantAmounts = new HashMap<>();
        participantAmounts.put(offerOne, 4);
        participantAmounts.put(offerTwo, 3);
        participantAmounts.put(offerThree, 2);
        participantAmounts.put(offerFour, 1);
        participantAmounts.put(offerFive, 0);

        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(participants, location);
        comparator.setParticipantAmountGetter(participantAmounts::get);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        // Sort list from low to high participation
        list.sort(comparator);

        assertEquals(offerFive, list.get(0));
        assertEquals(offerFour, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerTwo, list.get(3));
        assertEquals(offerOne, list.get(4));

        // Reverse list order from high to low participation
        list.sort(comparator.reversed());

        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));
    }

    @Test(expected = IllegalStateException.class)
    public void testParticipantsWithoutFunction() {
        // Test data
        OfferComparableField participants = OfferComparableField.PARTICIPANTS;
        Localizable location = new LocationFactory().getValidObject();
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();

        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(participants, location);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        list.sort(comparator);
    }

    @Test
    public void testRating() {
        // Test data
        OfferComparableField participants = OfferComparableField.RATING;
        Localizable location = new LocationFactory().getValidObject();
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();

        // Add a map able to return the numeric host rating of an offer
        Map<Offer, Double> hostRatings = new HashMap<>();
        hostRatings.put(offerOne, 4d);
        hostRatings.put(offerTwo, 3d);
        hostRatings.put(offerThree, 2d);
        hostRatings.put(offerFour, 1d);
        hostRatings.put(offerFive, 0d);

        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(participants, location);
        comparator.setHostRatingGetter(hostRatings::get);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        // Sort list from low to high rating
        list.sort(comparator);

        assertEquals(offerFive, list.get(0));
        assertEquals(offerFour, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerTwo, list.get(3));
        assertEquals(offerOne, list.get(4));

        // Reverse list order from high to low rating
        list.sort(comparator.reversed());

        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));
    }

    @Test(expected = IllegalStateException.class)
    public void testRatingWithoutFunction() {
        // Test data
        OfferComparableField participants = OfferComparableField.RATING;
        Localizable location = new LocationFactory().getValidObject();
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        Offer offerTwo = offerFactory.getValidObject();
        Offer offerThree = offerFactory.getValidObject();
        Offer offerFour = offerFactory.getValidObject();
        Offer offerFive = offerFactory.getValidObject();

        List<Offer> list = new ArrayList<>();
        list.add(offerOne);
        list.add(offerTwo);
        list.add(offerThree);
        list.add(offerFour);
        list.add(offerFive);

        // Execution
        OfferComparator comparator = new OfferComparator(participants, location);

        // Assertions
        assertEquals(offerOne, list.get(0));
        assertEquals(offerTwo, list.get(1));
        assertEquals(offerThree, list.get(2));
        assertEquals(offerFour, list.get(3));
        assertEquals(offerFive, list.get(4));

        list.sort(comparator);
    }
}
