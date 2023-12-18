package meet_eat.server;

import meet_eat.data.ObjectJsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HeaderPropertyEditorTest {

    @Test
    public void testConstructor() {
        // Execution
        HeaderPropertyEditor headerPropertyEditor = new HeaderPropertyEditor(Object.class);

        // Assertions
        assertNotNull(headerPropertyEditor);
    }

    @Test
    public void testSetAsText() {
        // Test data
        HeaderPropertyEditor headerPropertyEditor = new HeaderPropertyEditor(String.class);
        String string = "TestString";
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();

        // Execution
        headerPropertyEditor.setAsText(objectJsonParser.parseObjectToJsonString(string));

        // Assertions
        assertEquals(string, headerPropertyEditor.getValue());
    }

    @Test
    public void testGetAsText() {
        // Test data
        HeaderPropertyEditor headerPropertyEditor = new HeaderPropertyEditor(String.class);
        String string = "TestString";
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();

        // Execution
        headerPropertyEditor.setValue(string);

        // Assertions
        assertEquals(objectJsonParser.parseObjectToJsonString(string), headerPropertyEditor.getAsText());
    }
}
