package meet_eat.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.relation.rating.RatingValue;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactData;
import meet_eat.data.entity.user.contact.ContactRequest;
import meet_eat.data.entity.user.contact.ContactType;
import meet_eat.data.entity.user.setting.ColorMode;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.entity.user.setting.Setting;
import meet_eat.data.factory.TagFactory;
import meet_eat.data.factory.UserFactory;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.PostcodeLocation;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ObjectJsonParserCommonTest {

    private static final class UnparsableObject {

        public UnparsableObject() {
            int attribute = 0;
        }

        @JsonGetter
        public int getAttribute() {
            throw new IllegalStateException();
        }

    }

    private static final Localizable VALID_LOCALIZABLE = new SphericalLocation(new SphericalPosition(0, 0));

    @Test
    public void testConstructorEmpty() {
        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();

        // Assertions
        assertNotNull(objectJsonParser);
        assertNotNull(objectJsonParser.getObjectMapper());
    }

    @Test
    public void testConstructor() {
        // Test data
        ObjectMapper objectMapper = new ObjectMapper();

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser(objectMapper);

        // Assertions
        assertNotNull(objectJsonParser);
        assertNotNull(objectJsonParser.getObjectMapper());
        assertEquals(objectMapper, objectJsonParser.getObjectMapper());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnparsableJsonStringToObjectWithClassType() {
        // Test data
        String jsonString = "This is unparsable.";

        // Execution
        new ObjectJsonParser().parseJsonStringToObject(jsonString, Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnparsableJsonStringToObjectWithJavaType() {
        // Test data
        String jsonString = "This is unparsable.";

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        JavaType javaType = objectJsonParser.getObjectMapper().getTypeFactory().constructArrayType(ArrayList.class);
        objectJsonParser.parseJsonStringToObject(jsonString, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnparsableObjectToJsonString() {
        // Test data
        UnparsableObject unparsableObject = new UnparsableObject();

        // Execution
        new ObjectJsonParser().parseObjectToJsonString(unparsableObject);
    }

    @Test
    public void testSetObjectMapper() {
        // Test data
        ObjectMapper objectMapper = new ObjectMapper();

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        objectJsonParser.setObjectMapper(objectMapper);

        // Assertions
        assertNotNull(objectJsonParser);
        assertNotNull(objectJsonParser.getObjectMapper());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullObjectMapper() {
        // Execution
        new ObjectJsonParser().setObjectMapper(null);
    }

    @Test
    public void testParseString() {
        // Test data
        String testString = "Hello World!";

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(testString);

        // Assertions
        assertNotNull(jsonString);
        assertEquals(testString, objectJsonParser.parseJsonStringToObject(jsonString, String.class));
    }

    @Test
    public void testParsePage() {
        // Test data
        Page page = new Page(2, 25);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(page);
        Page parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Page.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(page, parsedObject);
    }

    @Test
    public void testParseTag() {
        // Test data
        Tag tag = new Tag("ABCDEFG1234", "TestTag");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(tag);
        Tag parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Tag.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(tag, parsedObject);
    }

    @Test
    public void testParseUser() {
        // Test data
        User user = new User(new Email("noreply.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(user);
        User parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, User.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(user, parsedObject);
        assertTrue(Iterables.elementsEqual(user.getSettings(), parsedObject.getSettings()));
    }

    @Test
    public void testParseOffer() {
        // Test data
        User user = new User(new Email("noreply.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("1", "TestTag1"));
        tags.add(new Tag("2", "TestTag2"));
        Offer offer = new Offer(user, tags, "Spaghetti", "Leckere Spaghetti. Mhmmmmmmm.",
                9.5, 2, LocalDateTime.of(2021, Month.JULY, 27, 20, 58), new CityLocation("Karlsruhe"));

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(offer);
        Offer parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Offer.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(offer, parsedObject);
    }

    @Test
    public void testParseToken() {
        // Test data
        User user = new User(new Email("noreply.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        Token token = new Token("ID1234", user, "12345678");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(token);
        Token parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Token.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(token, parsedObject);
    }

    @Test
    public void testParseReport() {
        // Test data
        User reporter = new User(new Email("noreply.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        User reported = new User(new Email("noreply2.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann 2", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        Report report = new Report(reporter, reported, "Max does not use power saving mode.");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(report);
        Report parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Report.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(report, parsedObject);
    }

    @Test
    public void testParseColorMode() {
        // Test data
        ColorMode colorMode = ColorMode.LIGHT;

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(colorMode);
        ColorMode parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, ColorMode.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(colorMode, parsedObject);
    }

    @Test
    public void testParseDisplaySetting() {
        // Test data
        ColorMode colorMode = ColorMode.LIGHT;
        DisplaySetting displaySetting = new DisplaySetting(colorMode);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(displaySetting);
        DisplaySetting parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, DisplaySetting.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(displaySetting, parsedObject);
    }

    @Test
    public void testParseDisplaySettingAsSetting() {
        // Test data
        ColorMode colorMode = ColorMode.LIGHT;
        Setting displaySetting = new DisplaySetting(colorMode);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(displaySetting);
        Setting parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Setting.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(DisplaySetting.class, parsedObject.getClass());
        assertEquals(displaySetting, parsedObject);
    }

    @Test
    public void testParseNotificationSetting() {
        // Test data
        NotificationSetting notificationSetting = new NotificationSetting(true, 42);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(notificationSetting);
        NotificationSetting parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, NotificationSetting.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(notificationSetting, parsedObject);
    }

    @Test
    public void testParseNotificationSettingAsSetting() {
        // Test data
        Setting notificationSetting = new NotificationSetting(true, 42);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(notificationSetting);
        Setting parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Setting.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(NotificationSetting.class, parsedObject.getClass());
        assertEquals(notificationSetting, parsedObject);
    }

    @Test
    public void testParseEmail() {
        // Test data
        Email email = new Email("noreply.meet.eat@gmail.com");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(email);
        Email parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Email.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(email, parsedObject);
    }

    @Test
    public void testParsePassword() {
        // Test data
        Password password = Password.createHashedPassword("Noreply.meet.eat@gmail.com1234");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(password);
        Password parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Password.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(password, parsedObject);
    }

    @Test
    public void testParseLoginCredential() {
        // Test data
        Email email = new Email("noreply.meet.eat@gmail.com");
        Password password = Password.createHashedPassword("Noreply.meet.eat@gmail.com1234");
        LoginCredential loginCredential = new LoginCredential(email, password);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(loginCredential);
        LoginCredential parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, LoginCredential.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(loginCredential, parsedObject);
    }

    @Test
    public void testParseRole() {
        // Test data
        Role role = Role.ADMIN;

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(role);
        Role parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Role.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(role, parsedObject);
    }

    @Test
    public void testParseRatingValue() {
        // Test data
        RatingValue ratingValue = RatingValue.POINTS_3;

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(ratingValue);
        RatingValue parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, RatingValue.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(ratingValue, parsedObject);
    }

    @Test
    public void testParseRatingBasis() {
        // Test data
        RatingBasis ratingBasis = RatingBasis.HOST;

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(ratingBasis);
        RatingBasis parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, RatingBasis.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(ratingBasis, parsedObject);
    }

    @Test
    public void testParseRating() {
        // Test data
        UserFactory userFactory = new UserFactory();
        TagFactory tagFactory = new TagFactory();
        User guest = userFactory.getValidObject();
        User host = userFactory.getValidObject();
        // Offer
        Set<Tag> tags = new HashSet<>();
        tags.add(tagFactory.getValidObject());
        tags.add(tagFactory.getValidObject());
        String name = "Test offer fhgeoirguh4";
        String description = "A crazy description";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2050, Month.OCTOBER, 16, 15, 0);
        Offer offer = new Offer(host, tags, name, description, price, maxParticipants, dateTime, VALID_LOCALIZABLE);
        Rating rating = Rating.createHostRating(guest, offer, RatingValue.POINTS_4);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(rating);
        Rating parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Rating.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(rating, parsedObject);
    }

    @Test
    public void testParseContactType() {
        // Test data
        ContactType contactType = ContactType.EMAIL;

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(contactType);
        ContactType parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, ContactType.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(contactType, parsedObject);
    }

    @Test
    public void testParseContactRequest() {
        // Test data
        User userFst = new User(new Email("noreply.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        User userSnd = new User(new Email("noreply2.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann 2", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        ContactRequest contactRequest = new ContactRequest(userFst, userSnd);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(contactRequest);
        ContactRequest parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, ContactRequest.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(contactRequest, parsedObject);
    }

    @Test
    public void testParseContactData() {
        // Test data
        User userFst = new User(new Email("noreply.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        User userSnd = new User(new Email("noreply2.meet.eat@gmail.com"), Password.createHashedPassword("AbcdefghijkL1!"),
                LocalDate.now(), "Max Mustermann 2", "+49 12345678", "Empty Description", false, VALID_LOCALIZABLE);
        ContactRequest contactRequest = new ContactRequest(userFst, userSnd);
        ContactData contactData = new ContactData(contactRequest);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(contactData);
        ContactData parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, ContactData.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(contactData, parsedObject);
    }

    @Test
    public void testParseSphericalPosition() {
        // Test data
        SphericalPosition sphericalPosition = new SphericalPosition(-10d, 10d);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(sphericalPosition);
        SphericalPosition parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, SphericalPosition.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(sphericalPosition, parsedObject);
    }

    @Test
    public void testParseSphericalLocation() {
        // Test data
        SphericalPosition sphericalPosition = new SphericalPosition(-10d, 10d);
        SphericalLocation sphericalLocation = new SphericalLocation(sphericalPosition);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(sphericalLocation);
        SphericalLocation parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, SphericalLocation.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(sphericalLocation, parsedObject);
    }

    @Test
    public void testParseSphericalLocationAsLocalizable() {
        // Test data
        SphericalPosition sphericalPosition = new SphericalPosition(-10d, 10d);
        Localizable localizable = new SphericalLocation(sphericalPosition);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(localizable);
        Localizable parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Localizable.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(SphericalLocation.class, parsedObject.getClass());
        assertEquals(localizable, parsedObject);
    }

    @Test
    public void testParseCityLocation() {
        // Test data
        CityLocation cityLocation = new CityLocation("Karlsruhe");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(cityLocation);
        CityLocation parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, CityLocation.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(cityLocation, parsedObject);
    }

    @Test
    public void testParseCityLocationAsLocalizable() {
        // Test data
        Localizable localizable = new CityLocation("Karlsruhe");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(localizable);
        Localizable parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Localizable.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(CityLocation.class, parsedObject.getClass());
        assertEquals(localizable, parsedObject);

    }

    @Test
    public void testParsePostcodeLocation() {
        // Test data
        PostcodeLocation postcodeLocation = new PostcodeLocation("76131");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(postcodeLocation);
        PostcodeLocation parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, PostcodeLocation.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(postcodeLocation, parsedObject);
    }

    @Test
    public void testParsePostcodeLocationAsLocalizable() {
        // Test data
        Localizable localizable = new PostcodeLocation("76131");

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(localizable);
        Localizable parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, Localizable.class);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(PostcodeLocation.class, parsedObject.getClass());
        assertEquals(localizable, parsedObject);
    }

    @Test
    public void testParseSettingMap() {
        // Test data
        Map<Class<? extends Setting>, Setting> settings = new HashMap<>();
        Setting notificationSetting = new NotificationSetting();
        Setting displaySetting = new DisplaySetting();
        settings.put(NotificationSetting.class, notificationSetting);
        settings.put(DisplaySetting.class, displaySetting);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(settings);
        JavaType keyType = objectJsonParser.getObjectMapper().getTypeFactory()
                .constructParametricType(Class.class, Setting.class);
        JavaType valueType = objectJsonParser.getObjectMapper().getTypeFactory()
                .constructType(Setting.class);
        JavaType type = objectJsonParser.getObjectMapper().getTypeFactory()
                .constructMapType(HashMap.class, keyType, valueType);
        Map<Class<? extends Setting>, Setting> parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, type);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertEquals(notificationSetting, parsedObject.get(NotificationSetting.class));
        assertEquals(displaySetting, parsedObject.get(DisplaySetting.class));
        assertEquals(settings, parsedObject);
    }

    @Test
    public void testParseHomogeneousLocalizableCollection() {
        // Test data
        Localizable localizableFst = new PostcodeLocation("76131");
        Localizable localizableSnd = new PostcodeLocation("76131");
        Localizable localizableTrd = new PostcodeLocation("76131");
        Collection<Localizable> localizables = new LinkedList<>();
        localizables.add(localizableFst);
        localizables.add(localizableSnd);
        localizables.add(localizableTrd);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(localizables);
        JavaType type = objectJsonParser.getObjectMapper().getTypeFactory().
                constructCollectionType(LinkedList.class, Localizable.class);
        Collection<Localizable> parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, type);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertTrue(parsedObject.containsAll(localizables));

        parsedObject.removeAll(localizables);
        assertEquals(0, parsedObject.size());
    }

    @Test
    public void testParseHeterogeneousLocalizableCollection() {
        // Test data
        Localizable localizableFst = new PostcodeLocation("76131");
        Localizable localizableSnd = new SphericalLocation(new SphericalPosition(-10, 10));
        Localizable localizableTrd = new CityLocation("Karlsruhe");
        Collection<Localizable> localizables = new LinkedList<>();
        localizables.add(localizableFst);
        localizables.add(localizableSnd);
        localizables.add(localizableTrd);

        // Execution
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        String jsonString = objectJsonParser.parseObjectToJsonString(localizables);
        JavaType type = objectJsonParser.getObjectMapper().getTypeFactory().
                constructCollectionType(LinkedList.class, Localizable.class);
        Collection<Localizable> parsedObject = objectJsonParser.parseJsonStringToObject(jsonString, type);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertTrue(parsedObject.containsAll(localizables));

        parsedObject.removeAll(localizables);
        assertEquals(0, parsedObject.size());
    }

    @Test
    public void testParseHeterogeneousLocalizableCollectionWithDefaultDeserializer() {
        // Test data
        Localizable localizableFst = new PostcodeLocation("76131");
        Localizable localizableSnd = new SphericalLocation(new SphericalPosition(-10, 10));
        Localizable localizableTrd = new CityLocation("Karlsruhe");
        Collection<Localizable> localizables = new LinkedList<>();
        localizables.add(localizableFst);
        localizables.add(localizableSnd);
        localizables.add(localizableTrd);

        // Execution - Serialization
        ObjectJsonParser objectSerializer = new ObjectJsonParser();
        String jsonString = objectSerializer.parseObjectToJsonString(localizables);
        JavaType type = objectSerializer.getObjectMapper().getTypeFactory().
                constructCollectionType(LinkedList.class, Localizable.class);

        // Execution - Deserialization
        ObjectJsonParser objectDeserializer = new ObjectJsonParser(new ObjectMapper());
        Collection<Localizable> parsedObject = objectDeserializer.parseJsonStringToObject(jsonString, type);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertTrue(parsedObject.containsAll(localizables));

        parsedObject.removeAll(localizables);
        assertEquals(0, parsedObject.size());
    }

    @Test
    public void testParseIntegerCollectionWithDefaultDeserializer() {
        // Test data
        Collection<Integer> integers = new LinkedList<>();
        integers.add(0);
        integers.add(4);
        integers.add(2);

        // Execution - Serialization
        ObjectJsonParser objectSerializer = new ObjectJsonParser();
        String jsonString = objectSerializer.parseObjectToJsonString(integers);
        JavaType type = objectSerializer.getObjectMapper().getTypeFactory().
                constructCollectionType(LinkedList.class, Integer.class);

        // Execution - Deserialization
        ObjectJsonParser objectDeserializer = new ObjectJsonParser(new ObjectMapper());
        Collection<Integer> parsedObject = objectDeserializer.parseJsonStringToObject(jsonString, type);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertTrue(parsedObject.containsAll(integers));

        parsedObject.removeAll(integers);
        assertEquals(0, parsedObject.size());
    }

    @Test
    public void testParseStringCollectionWithDefaultDeserializer() {
        // Test data
        Collection<String> strings = new LinkedList<>();
        strings.add("Hello ");
        strings.add("World");
        strings.add("!");

        // Execution - Serialization
        ObjectJsonParser objectSerializer = new ObjectJsonParser();
        String jsonString = objectSerializer.parseObjectToJsonString(strings);
        JavaType type = objectSerializer.getObjectMapper().getTypeFactory().
                constructCollectionType(LinkedList.class, String.class);

        // Execution - Deserialization
        ObjectJsonParser objectDeserializer = new ObjectJsonParser(new ObjectMapper());
        Collection<String> parsedObject = objectDeserializer.parseJsonStringToObject(jsonString, type);

        // Assertions
        assertNotNull(jsonString);
        assertNotNull(parsedObject);
        assertTrue(parsedObject.containsAll(strings));

        parsedObject.removeAll(strings);
        assertEquals(0, parsedObject.size());
    }
}
