package meet_eat.data.entity.user.contact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the information exchanged during a successful {@link ContactRequest}.
 */
public class ContactData {

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_REQUEST = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "request");
    private static final String ERROR_MESSAGE_NULL_CONTACT_TYPE = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "contactType");
    private static final String ERROR_MESSAGE_NULL_VALUE = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "value");

    @JsonProperty
    private final Map<ContactType, String> contacts;
    @JsonProperty
    private final ContactRequest request;

    /**
     * Creates contact data with no given {@link ContactType}.
     *
     * @param request the for the transmission necessary contact information for a {@link ContactRequest}
     */
    public ContactData(ContactRequest request) {
        this.request = Objects.requireNonNull(request, ERROR_MESSAGE_NULL_REQUEST);
        contacts = new EnumMap<>(ContactType.class);
    }

    /**
     * Creates contact data.
     *
     * @param request  the for the transmission necessary contact information for a {@link ContactRequest}
     * @param contacts the requested contact information
     */
    @JsonCreator
    public ContactData(@JsonProperty("request") ContactRequest request,
                       @JsonProperty("contacts") Map<ContactType, String> contacts) {
        this.request = Objects.requireNonNull(request, ERROR_MESSAGE_NULL_REQUEST);
        this.contacts = Objects.requireNonNull(contacts);
    }

    /**
     * Gets the requested contact information.
     *
     * @return the requested contact information
     */
    @JsonGetter
    public Map<ContactType, String> getContacts() {
        return Collections.unmodifiableMap(contacts);
    }

    /**
     * Gets the corresponding value of a {@link ContactType}.
     *
     * @param type the contact type
     * @return the value
     */
    @JsonIgnore
    public String getContactByType(ContactType type) {
        return contacts.get(type);
    }

    /**
     * Gets the {@link ContactRequest}.
     *
     * @return the contact request
     */
    @JsonGetter
    public ContactRequest getRequest() {
        return request;
    }

    /**
     * Puts a contact type with corresponding value to the map.
     *
     * @param type  the contact type
     * @param value the value
     */
    public void putContact(ContactType type, String value) {
        Objects.requireNonNull(type, ERROR_MESSAGE_NULL_CONTACT_TYPE);
        Objects.requireNonNull(value, ERROR_MESSAGE_NULL_VALUE);
        contacts.put(type, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return Maps.difference(contacts, that.contacts).areEqual() && Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts, request);
    }
}