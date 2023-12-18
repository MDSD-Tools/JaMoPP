package meet_eat.data.factory;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class OfferFactory extends ObjectFactory<Offer> {

    private static final int AMOUNT_TAGS = 2;
    private static final double TEST_PRICE = 5d;
    private static final int TEST_MAX_AMOUNT_PARTICIPANTS = 10;

    private final UserFactory userFactory;
    private final TagFactory tagFactory;
    private final LocationFactory locationFactory;

    public OfferFactory() {
        super();
        userFactory = new UserFactory();
        tagFactory = new TagFactory();
        locationFactory = new LocationFactory();
    }

    @Override
    protected Offer createObject() {
        String identifier = Integer.toString(objectCounter);
        User creator = userFactory.getValidObject();
        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < AMOUNT_TAGS; i++) {
            tags.add(tagFactory.getValidObject());
        }
        String name = "TestOffer" + objectCounter;
        String description = "This is test offer number " + objectCounter;
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2050, Month.OCTOBER, 16), LocalTime.NOON);
        Localizable location = locationFactory.getValidObject();
        return new Offer(identifier, creator, tags, name, description, TEST_PRICE, TEST_MAX_AMOUNT_PARTICIPANTS,
                dateTime, location);
    }
}
