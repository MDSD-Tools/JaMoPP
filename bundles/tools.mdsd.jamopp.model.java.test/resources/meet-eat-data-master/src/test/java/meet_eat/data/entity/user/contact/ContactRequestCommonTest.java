package meet_eat.data.entity.user.contact;

import meet_eat.data.entity.user.User;
import meet_eat.data.factory.UserFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactRequestCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();
        User requestedUser = userFactory.getValidObject();

        // Execution
        ContactRequest contactRequest = new ContactRequest(requester, requestedUser);

        // Assertions
        assertNotNull(contactRequest);
        assertEquals(requester, contactRequest.getRequester());
        assertEquals(requestedUser, contactRequest.getRequestedUser());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullRequesterAndRequestedUser() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requestedUser = userFactory.getValidObject();

        // Execution
        new ContactRequest(null, requestedUser);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithRequesterAndNullRequestedUser() {
        // Test data
        UserFactory userFactory = new UserFactory();
        User requester = userFactory.getValidObject();

        // Execution
        new ContactRequest(requester, null);
    }

    @Test
    public void testEquals() {
        // Execution
        UserFactory userFactory = new UserFactory();
        ContactRequest contactRequest = new ContactRequest(userFactory.getValidObject(), userFactory.getValidObject());
        ContactRequest contactRequestCopy = new ContactRequest(contactRequest.getRequester(), contactRequest.getRequestedUser());
        ContactRequest contactRequestFakeCopyRequester = new ContactRequest(userFactory.getValidObject(), contactRequest.getRequestedUser());
        ContactRequest contactRequestFakeCopyRequestedUser = new ContactRequest(contactRequest.getRequester(), userFactory.getValidObject());

        // Assertions
        assertEquals(contactRequest, contactRequest);
        assertNotEquals(contactRequest, null);
        assertNotEquals(contactRequest, new Object());
        assertEquals(contactRequest, contactRequestCopy);
        assertNotEquals(contactRequest, contactRequestFakeCopyRequester);
        assertNotEquals(contactRequest, contactRequestFakeCopyRequestedUser);
        assertEquals(contactRequest.hashCode(), contactRequestCopy.hashCode());
    }
}
