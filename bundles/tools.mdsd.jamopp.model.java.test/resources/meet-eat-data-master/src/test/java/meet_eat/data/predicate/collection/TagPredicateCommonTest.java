package meet_eat.data.predicate.collection;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.factory.OfferFactory;
import meet_eat.data.factory.TagFactory;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TagPredicateCommonTest {

    @Test
    public void testConstructor() {
        // Test data
        CollectionOperation operation = CollectionOperation.CONTAIN_ALL;
        Set<Tag> reference = new HashSet<>();

        // Execution
        TagPredicate tagPredicate = new TagPredicate(operation, reference);

        // Assertions
        assertNotNull(tagPredicate);
        assertNotNull(tagPredicate.getOperation());
        assertNotNull(tagPredicate.getReferenceValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() {
        // Test data
        Set<Tag> reference = new HashSet<>();

        // Execution
        new TagPredicate(null, reference);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReferenceValue() {
        // Test data
        CollectionOperation operation = CollectionOperation.CONTAIN_ALL;

        // Execution
        new TagPredicate(operation, null);
    }

    @Test
    public void testOperate() {
        CollectionOperation operation = CollectionOperation.CONTAIN_ALL;
        Set<Tag> reference = new HashSet<>();
        TagFactory tagFactory = new TagFactory();
        Tag tagOne = tagFactory.getValidObject();
        Tag tagTwo = tagFactory.getValidObject();
        reference.add(tagOne);
        reference.add(tagTwo);
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.addTag(tagOne);
        offerOne.addTag(tagTwo);
        Offer offerTwo = offerFactory.getValidObject();
        offerTwo.addTag(tagOne);
        offerTwo.addTag(tagFactory.getValidObject());

        // Execution
        TagPredicate tagPredicate = new TagPredicate(operation, reference);

        // Assertions
        assertTrue(tagPredicate.test(offerOne));
        assertFalse(tagPredicate.test(offerTwo));
    }
}
