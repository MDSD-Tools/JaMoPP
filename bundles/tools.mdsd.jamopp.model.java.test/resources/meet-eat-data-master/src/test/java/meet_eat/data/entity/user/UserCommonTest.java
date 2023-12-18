package meet_eat.data.entity.user;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.setting.ColorMode;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.entity.user.setting.Setting;
import meet_eat.data.factory.EmailFactory;
import meet_eat.data.factory.OfferFactory;
import meet_eat.data.factory.PasswordFactory;
import meet_eat.data.factory.UserFactory;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.numeric.DoubleOperation;
import meet_eat.data.predicate.numeric.PricePredicate;
import meet_eat.data.predicate.string.NamePredicate;
import meet_eat.data.predicate.string.StringOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserCommonTest {

    private String identifier;
    private Set<User> subscriptions;
    private Collection<Setting> settings;
    private Set<Offer> bookmarks;
    private Role role;
    private Email email;
    private Password password;
    private LocalDate birthDay;
    private String name;
    private String phoneNumber;
    private String description;
    private boolean isVerified;
    private Collection<OfferPredicate> offerPredicates;
    private OfferComparator offerComparator;
    private Localizable localizable;

    @Before
    public void setUp() {
        identifier = "This is my identifier";
        UserFactory userFactory = new UserFactory();
        subscriptions = new HashSet<>();
        subscriptions.add(userFactory.getValidObject());
        subscriptions.add(userFactory.getValidObject());
        settings = new LinkedList<>();
        settings.add(new NotificationSetting(false, 60));
        settings.add(new DisplaySetting(ColorMode.DARK));
        OfferFactory offerFactory = new OfferFactory();
        bookmarks = new HashSet<>();
        bookmarks.add(offerFactory.getValidObject());
        bookmarks.add(offerFactory.getValidObject());
        role = Role.USER;
        email = new EmailFactory().getValidObject();
        password = new PasswordFactory().getValidObject();
        birthDay = LocalDate.of(1998, Month.OCTOBER, 16);
        name = "TestUser";
        phoneNumber = "My test phone number";
        description = "This is my test description";
        isVerified = false;
        offerPredicates = new LinkedList<>();
        offerPredicates.add(new NamePredicate(StringOperation.CONTAIN, "test"));
        localizable = new CityLocation("Karlsruhe");
        offerComparator = new OfferComparator(OfferComparableField.PRICE, localizable);
    }

    @After
    public void tearDown() {
        identifier = null;
        subscriptions = null;
        settings = null;
        bookmarks = null;
        role = null;
        email = null;
        password = null;
        birthDay = null;
        name = null;
        phoneNumber = null;
        description = null;
        isVerified = false;
        offerPredicates = null;
        offerComparator = null;
        localizable = null;
    }

    @Test
    public void testConstructor() {
        // Execution
        User user = new User(email, password, birthDay, name, phoneNumber, description, isVerified, localizable);

        // Assertions
        assertNotNull(user);
        assertNotNull(user.getSettings());
        assertNotNull(user.getOfferPredicates());
        assertNotNull(user.getOfferComparator());
        assertEquals(Role.USER, user.getRole());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(birthDay, user.getBirthDay());
        assertEquals(name, user.getName());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(description, user.getDescription());
        assertEquals(isVerified, user.isVerified());
        assertEquals(localizable, user.getLocalizable());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullEmail() {
        // Execution
        new User(null, password, birthDay, name, phoneNumber, description, isVerified, localizable);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPassword() {
        // Execution
        new User(email, null, birthDay, name, phoneNumber, description, isVerified, localizable);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullBirthDay() {
        // Execution
        new User(email, password, null, name, phoneNumber, description, isVerified, localizable);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullName() {
        // Execution
        new User(email, password, birthDay, null, phoneNumber, description, isVerified, localizable);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPhoneNumber() {
        // Execution
        new User(email, password, birthDay, name, null, description, isVerified, localizable);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullDescription() {
        // Execution
        new User(email, password, birthDay, name, phoneNumber, null, isVerified, localizable);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullLocalizable() {
        // Execution
        new User(email, password, birthDay, name, phoneNumber, description, isVerified, null);
    }

    @Test
    public void testJsonConstructor() {
        // Execution
        User user = new User(identifier, settings, role, email, password,
                birthDay, name, phoneNumber, description, isVerified, offerPredicates, offerComparator, localizable);

        // Assertions
        assertNotNull(user);
        assertEquals(identifier, user.getIdentifier());
        assertTrue(Iterables.elementsEqual(settings, user.getSettings()));
        assertTrue(offerPredicates.containsAll(user.getOfferPredicates()));
        assertEquals(offerComparator, user.getOfferComparator());
        assertEquals(Role.USER, user.getRole());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(birthDay, user.getBirthDay());
        assertEquals(name, user.getName());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(description, user.getDescription());
        assertEquals(isVerified, user.isVerified());
        assertEquals(localizable, user.getLocalizable());
    }

    @Test
    public void testSetRole() {
        // Test data
        Role role = Role.USER;

        // Execution
        User user = new UserFactory().getValidObject();
        user.setRole(role);

        // Assertions
        assertEquals(role, user.getRole());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRole() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setRole(null);
    }

    @Test
    public void testSetEmail() {
        // Test data
        Email email = new EmailFactory().getValidObject();

        // Execution
        User user = new UserFactory().getValidObject();
        user.setEmail(email);

        // Assertions
        assertEquals(email, user.getEmail());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullEmail() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setEmail(null);
    }

    @Test
    public void testSetPassword() {
        // Test data
        Password password = new PasswordFactory().getValidObject();

        // Execution
        User user = new UserFactory().getValidObject();
        user.setPassword(password);

        // Assertions
        assertEquals(password, user.getPassword());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullPassword() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setPassword(null);
    }

    @Test
    public void testSetBirthDay() {
        // Test data
        LocalDate birthDay = LocalDate.of(1998, Month.OCTOBER, 16);

        // Execution
        User user = new UserFactory().getValidObject();
        user.setBirthDay(birthDay);

        // Assertions
        assertEquals(birthDay, user.getBirthDay());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullBirthDay() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setBirthDay(null);
    }

    @Test
    public void testSetName() {
        // Test data
        String name = "Crazy name!";

        // Execution
        User user = new UserFactory().getValidObject();
        user.setName(name);

        // Assertions
        assertEquals(name, user.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullName() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setName(null);
    }

    @Test
    public void testSetPhoneNumber() {
        // Test data
        String phoneNumber = "0123456789";

        // Execution
        User user = new UserFactory().getValidObject();
        user.setPhoneNumber(phoneNumber);

        // Assertions
        assertEquals(phoneNumber, user.getPhoneNumber());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullPhoneNumber() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setPhoneNumber(null);
    }

    @Test
    public void testSetDescription() {
        // Test data
        String description = "A new crazy description.";

        // Execution
        User user = new UserFactory().getValidObject();
        user.setDescription(description);

        // Assertions
        assertEquals(description, user.getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullDescription() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setDescription(null);
    }

    @Test
    public void testSetVerified() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setVerified(true);

        // Assertions
        assertTrue(user.isVerified());
    }

    @Test
    public void testSetOfferComparator() {
        // Test data
        OfferComparator comparator = new OfferComparator(OfferComparableField.PRICE, new CityLocation("Karlsruhe"));

        // Execution
        User user = new UserFactory().getValidObject();
        user.setOfferComparator(comparator);

        // Assertions
        assertEquals(comparator, user.getOfferComparator());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullOfferComparator() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setOfferComparator(null);
    }

    @Test
    public void testSetLocalizable() {
        // Test data
        Localizable location = new CityLocation("Karlsruhe");

        // Execution
        User user = new UserFactory().getValidObject();
        user.setLocalizable(location);

        // Assertions
        assertEquals(location, user.getLocalizable());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullLocalizable() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.setLocalizable(null);
    }

    @Test
    public void testAddSetting() {
        // Test data
        Setting notificationSetting = new NotificationSetting();

        // Execution
        User user = new UserFactory().getValidObject();
        user.addSetting(notificationSetting);

        // Assertions
        assertEquals(2, user.getSettings().size());
        assertTrue(user.getSettings().contains(notificationSetting));
    }

    @Test
    public void testAddSettingOverwrite() {
        // Test data
        Setting notificationSettingToBeOverwritten = new NotificationSetting(true, 30);
        Setting notificationSetting = new NotificationSetting();

        // Execution: Add
        User user = new UserFactory().getValidObject();
        user.addSetting(notificationSettingToBeOverwritten);

        // Assertions: Pre-Overwrite
        assertTrue(user.getSettings().contains(notificationSettingToBeOverwritten));

        // Execution: Overwrite
        user.addSetting(notificationSetting);

        // Assertions: Post-Overwrite
        assertTrue(user.getSettings().contains(notificationSetting));
        assertFalse(user.getSettings().contains(notificationSettingToBeOverwritten));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullSetting() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.addSetting(null);
    }

    @Test
    public void testAddOfferPredicate() {
        // Test data
        OfferPredicate offerPredicate = new NamePredicate(StringOperation.EQUAL, "test");

        // Execution
        User user = new UserFactory().getValidObject();
        user.addOfferPredicate(offerPredicate);

        // Assertions
        assertTrue(user.getOfferPredicates().contains(offerPredicate));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOfferPredicate() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.addOfferPredicate(null);
    }

    @Test
    public void testAddManyOfferPredicates() {
        // Test data
        OfferPredicate namePredicate = new NamePredicate(StringOperation.EQUAL, "test");
        OfferPredicate pricePredicate = new PricePredicate(DoubleOperation.GREATER, 2d);
        Collection<OfferPredicate> offerPredicates = new LinkedList<>();
        offerPredicates.add(namePredicate);
        offerPredicates.add(pricePredicate);

        // Execution
        User user = new UserFactory().getValidObject();
        user.addManyOfferPredicates(offerPredicates);

        // Assertions
        assertTrue(user.getOfferPredicates().contains(namePredicate));
        assertTrue(user.getOfferPredicates().contains(pricePredicate));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOfferPredicates() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.addManyOfferPredicates(null);
    }

    @Test
    public void testRemoveOfferPredicate() {
        // Test data
        OfferPredicate namePredicate = new NamePredicate(StringOperation.EQUAL, "test");

        // Execution
        User user = new UserFactory().getValidObject();
        user.addOfferPredicate(namePredicate);

        // Assertions
        assertTrue(user.getOfferPredicates().contains(namePredicate));

        user.removeOfferPredicate(namePredicate);

        assertFalse(user.getOfferPredicates().contains(namePredicate));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullOfferPredicate() {
        // Execution
        User user = new UserFactory().getValidObject();
        user.removeOfferPredicate(null);
    }

    @Test
    public void testClearOfferPredicates() {
        // Test data
        OfferPredicate namePredicate = new NamePredicate(StringOperation.EQUAL, "test");
        OfferPredicate pricePredicate = new PricePredicate(DoubleOperation.GREATER, 2d);
        Collection<OfferPredicate> offerPredicates = new LinkedList<>();
        offerPredicates.add(namePredicate);
        offerPredicates.add(pricePredicate);

        // Execution
        User user = new UserFactory().getValidObject();
        user.addManyOfferPredicates(offerPredicates);

        // Assertions
        assertFalse(user.getOfferPredicates().isEmpty());

        user.clearOfferPredicates();

        assertTrue(user.getOfferPredicates().isEmpty());
    }

    @Test
    public void testEquals() {
        // Execution
        User user = new UserFactory().getValidObject();
        LinkedList<OfferPredicate> offerPredicates = Lists.newLinkedList(user.getOfferPredicates());
        LinkedList<Setting> settings = Lists.newLinkedList(user.getSettings());
        User userCopy = new User(user.getIdentifier(), settings, user.getRole(), user.getEmail(), user.getPassword(),
                user.getBirthDay(), user.getName(), user.getPhoneNumber(), user.getDescription(), user.isVerified(),
                offerPredicates, user.getOfferComparator(), user.getLocalizable());

        // Assertions
        assertNotEquals(null, user);
        assertNotEquals(new Object(), user);
        assertEquals(user, userCopy);
        assertEquals(user.hashCode(), userCopy.hashCode());
    }
}