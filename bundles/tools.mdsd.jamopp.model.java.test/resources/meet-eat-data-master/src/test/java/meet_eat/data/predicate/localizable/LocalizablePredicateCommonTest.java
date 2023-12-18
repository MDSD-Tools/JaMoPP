package meet_eat.data.predicate.localizable;

import meet_eat.data.entity.Offer;
import meet_eat.data.factory.OfferFactory;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;
import meet_eat.data.predicate.numeric.DoubleOperation;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalizablePredicateCommonTest {

    @Test
    public void testConstructor() throws UnlocalizableException {
        // Test data
        DoubleOperation operation = DoubleOperation.LESS;
        double reference = 30d;
        Localizable localizable = new CityLocation("Karlsruhe");

        // Execution
        LocalizablePredicate localizablePredicate = new LocalizablePredicate(operation, reference, localizable);

        // Assertions
        assertNotNull(localizablePredicate);
        assertNotNull(localizablePredicate.getOperation());
        assertNotNull(localizablePredicate.getReferenceValue());
        assertNotNull(localizablePredicate.getLocalizable());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOperation() throws UnlocalizableException {
        // Test data
        double reference = 30d;
        Localizable localizable = new CityLocation("Karlsruhe");

        // Execution
        new LocalizablePredicate(null, reference, localizable);
    }

    @Test
    public void testOperate() throws UnlocalizableException {
        // Test data
        DoubleOperation operation = DoubleOperation.LESS;
        double reference = 30000d;
        Localizable localizable = new CityLocation("Karlsruhe");
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.setLocation(new CityLocation("Mannheim"));
        Offer offerTwo = offerFactory.getValidObject();
        offerTwo.setLocation(new CityLocation("Ettlingen"));

        // Execution
        LocalizablePredicate localizablePredicate = new LocalizablePredicate(operation, reference, localizable);

        // Assertions
        assertFalse(localizablePredicate.test(offerOne));
        assertTrue(localizablePredicate.test(offerTwo));
    }

    @Test(expected = UnlocalizableException.class)
    public void testOperateUnlocalizable() throws UnlocalizableException {
        // Test data
        DoubleOperation operation = DoubleOperation.LESS;
        double reference = 30d;
        Localizable localizable = new CityLocation("abc123456");
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.setLocation(new CityLocation("Stuttgart"));
        Offer offerTwo = offerFactory.getValidObject();
        offerTwo.setLocation(new CityLocation("Mannheim"));

        // Execution
        new LocalizablePredicate(operation, reference, localizable);
    }

    @Test
    public void testGetDistanceUnlocalizable() throws UnlocalizableException {
        // Test data
        DoubleOperation operation = DoubleOperation.LESS;
        double reference = 30d;
        Localizable localizable = new CityLocation("Karlsruhe");
        OfferFactory offerFactory = new OfferFactory();
        Offer offerOne = offerFactory.getValidObject();
        offerOne.setLocation(new CityLocation("abc123"));
        Offer offerTwo = offerFactory.getValidObject();
        offerTwo.setLocation(new CityLocation("Mannheim"));

        // Execution
        LocalizablePredicate localizablePredicate = new LocalizablePredicate(operation, reference, localizable);

        // Assertions
        assertFalse(localizablePredicate.test(offerOne));
    }
}
