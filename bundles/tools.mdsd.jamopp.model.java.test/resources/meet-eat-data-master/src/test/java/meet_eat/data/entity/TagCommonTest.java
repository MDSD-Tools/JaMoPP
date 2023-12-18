package meet_eat.data.entity;

import meet_eat.data.factory.TagFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        String name = "MyTag";

        // Execution
        Tag tag = new Tag(name);

        // Assertion
        assertNotNull(tag);
        assertEquals(name, tag.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullName() {
        // Execution
        new Tag(null);
    }

    @Test
    public void testConstructorWithIdentifierAndName() {
        // Test data
        String identifier = "MyIdentifier";
        String name = "ThisIsMyName";

        // Execution
        Tag tag = new Tag(identifier, name);

        // Assertions
        assertNotNull(tag);
        assertEquals(identifier, tag.getIdentifier());
        assertEquals(name, tag.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithIdentifierAndNullIdentifier() {
        // Test data
        String identifier = "IdentifierXY";

        // Execution
        new Tag(identifier, null);
    }

    @Test
    public void testConstructorWithNullIdentifierAndName() {
        // Test data
        String name = "MySpecialName123";

        // Execution
        Tag tag = new Tag(null, name);

        // Assertions
        assertNotNull(tag);
        assertNull(tag.getIdentifier());
    }

    @Test
    public void testSetName() {
        // Test data
        String name = "NewTagName";
        TagFactory tagFactory = new TagFactory();
        Tag tag = tagFactory.getValidObject();

        // Execution
        tag.setName(name);

        // Assertions
        assertEquals(name, tag.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullName() {
        // Test data
        TagFactory tagFactory = new TagFactory();
        Tag tag = tagFactory.getValidObject();

        // Execution
        tag.setName(null);
    }
}
