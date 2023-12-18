package meet_eat.server;

import meet_eat.data.ObjectJsonParser;

import java.beans.PropertyEditorSupport;

/**
 * Represents a custom data serialization and deserialization class especially for header elements used by
 * {@link meet_eat.server.controller.EntityController controllers} to receive and send messages correctly.
 */
public class HeaderPropertyEditor extends PropertyEditorSupport {

    private final Class<?> headerClass;

    /**
     * Constructs a new instance of {@link HeaderPropertyEditor}.
     *
     * @param headerClass {@link Class class} of the received or sent header element
     */
    public HeaderPropertyEditor(Class<?> headerClass) {
        this.headerClass = headerClass;
    }

    @Override
    public String getAsText() {
        return new ObjectJsonParser().parseObjectToJsonString(getValue());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(new ObjectJsonParser().parseJsonStringToObject(text, headerClass));
    }
}
