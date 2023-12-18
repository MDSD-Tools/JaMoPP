package meet_eat.data.entity.user.contact;

import meet_eat.data.entity.user.User;
import meet_eat.data.factory.UserFactory;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class ContactDataCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);

        // Execution
        ContactData contactData = new ContactData(contactRequest);

        // Assertions
        assertNotNull(contactData);
        assertNotNull(contactData.getContacts());
        assertEquals(contactRequest, contactData.getRequest());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullRequest() {
        // Execution
        new ContactData(null);
    }

    @Test
    public void testConstructorWithRequestAndContacts() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        String email = "test.meet.eat@example.com";
        contacts.put(ContactType.EMAIL, email);

        // Execution
        ContactData contactData = new ContactData(contactRequest, contacts);

        // Assertion
        assertNotNull(contactData);
        assertEquals(contacts, contactData.getContacts());
        assertEquals(email, contactData.getContactByType(ContactType.EMAIL));
        assertEquals(contactRequest, contactData.getRequest());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullRequestAndContacts() {
        // Test data
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        String email = "test.meet.eat@example.com";
        contacts.put(ContactType.EMAIL, email);

        // Execution
        new ContactData(null, contacts);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithRequestAndNullContacts() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);

        // Execution
        new ContactData(contactRequest, null);
    }

    @Test
    public void testPutContact() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        String email = "test@example.com";
        String phoneNumber = "123456";

        // Execution
        ContactData contactData = new ContactData(contactRequest, contacts);
        contactData.putContact(ContactType.EMAIL, email);
        contactData.putContact(ContactType.PHONE_NUMBER, phoneNumber);

        // Assertions
        assertEquals(email, contactData.getContactByType(ContactType.EMAIL));
        assertEquals(phoneNumber, contactData.getContactByType(ContactType.PHONE_NUMBER));
    }

    @Test
    public void testMultiplePutsInContact() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        String email = "test@example.com";
        String phoneNumber = "123";
        String emailTwo = "test2@example.com";
        String phoneNumberTwo = "123456";
        String emailThree = "test3@example.com";
        String phoneNumberThree = "123456789";

        // Execution
        ContactData contactData = new ContactData(contactRequest, contacts);
        contactData.putContact(ContactType.EMAIL, email);
        contactData.putContact(ContactType.PHONE_NUMBER, phoneNumber);
        contactData.putContact(ContactType.EMAIL, emailTwo);
        contactData.putContact(ContactType.PHONE_NUMBER, phoneNumberTwo);
        contactData.putContact(ContactType.EMAIL, emailThree);
        contactData.putContact(ContactType.PHONE_NUMBER, phoneNumberThree);

        // Assertions
        assertEquals(2, contactData.getContacts().size());
        assertEquals(emailThree, contactData.getContactByType(ContactType.EMAIL));
        assertEquals(phoneNumberThree, contactData.getContactByType(ContactType.PHONE_NUMBER));
        assertFalse(contactData.getContacts().containsValue(email));
        assertFalse(contactData.getContacts().containsValue(phoneNumber));
        assertFalse(contactData.getContacts().containsValue(emailTwo));
        assertFalse(contactData.getContacts().containsValue(phoneNumberTwo));
    }

    @Test(expected = NullPointerException.class)
    public void testPutContactWithNullTypeAndValue() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        String value = "thisIsMyTest@example.com";

        // Execution
        ContactData contactData = new ContactData(contactRequest, contacts);
        contactData.putContact(null, value);

    }

    @Test(expected = NullPointerException.class)
    public void testPutContactWithTypeAndNullValue() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);

        // Execution
        ContactData contactData = new ContactData(contactRequest, contacts);
        contactData.putContact(ContactType.EMAIL, null);
    }

    @Test
    public void testEquals() {
        // Execution
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        contacts.put(ContactType.EMAIL, "test@example.com");
        ContactData contactData = new ContactData(new ContactRequest(requester, requestedUser), contacts);
        ContactData contactDataCopy = new ContactData(contactData.getRequest(), contactData.getContacts());
        // FakeCopyRequest
        ContactRequest fakeContactRequest = new ContactRequest(userFactory.getValidObject(), userFactory.getValidObject());
        ContactData contactDataFakeCopyRequest = new ContactData(fakeContactRequest, contactData.getContacts());
        // FakeCopyContacts
        Map<ContactType, String> fakeContacts = new EnumMap<>(ContactType.class);
        ContactData contactDataFakeCopyContacts = new ContactData(contactData.getRequest(), fakeContacts);

        // Assertions
        assertEquals(contactData, contactData);
        assertNotEquals(contactData, null);
        assertNotEquals(contactData, new Object());
        assertEquals(contactData, contactDataCopy);
        assertNotEquals(contactData, contactDataFakeCopyRequest);
        assertNotEquals(contactData, contactDataFakeCopyContacts);
        assertEquals(contactData.hashCode(), contactDataCopy.hashCode());
    }
}
