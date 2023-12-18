package meet_eat.data.factory;

import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.setting.ColorMode;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.entity.user.setting.Setting;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.numeric.DoubleOperation;
import meet_eat.data.predicate.numeric.PricePredicate;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserFactory extends ObjectFactory<User> {

    private static final Role DEFAULT_ROLE = Role.USER;
    private static final boolean DEFAULT_NOTIFICATION = false;
    private static final boolean DEFAULT_VERIFIED = false;

    private final EmailFactory emailFactory;
    private final PasswordFactory passwordFactory;

    public UserFactory() {
        super();
        emailFactory = new EmailFactory();
        passwordFactory = new PasswordFactory();
    }

    @Override
    protected User createObject() {
        String identifier = Integer.toString(objectCounter);
        Collection<Setting> settings = new LinkedList<>();
        settings.add(new NotificationSetting(DEFAULT_NOTIFICATION, objectCounter));
        settings.add(new DisplaySetting(getRandomEnumValue(ColorMode.class)));
        Email email = emailFactory.getValidObject();
        Password password = passwordFactory.getValidObject();
        LocalDate birthDay = LocalDate.of(1970, Month.OCTOBER, 10);
        String name = "TestUser" + objectCounter;
        String phoneNumber = Integer.toString(objectCounter);
        String description = "I am " + name + " and this is my description.";
        Collection<OfferPredicate> offerPredicates = new LinkedList<>();
        offerPredicates.add(new PricePredicate(DoubleOperation.LESS, 20d));
        Localizable localizable = new SphericalLocation(new SphericalPosition(0, 0));
        OfferComparator offerComparator = new OfferComparator(OfferComparableField.TIME, localizable);
        return new User(identifier, settings, DEFAULT_ROLE, email, password,
                birthDay, name, phoneNumber, description, DEFAULT_VERIFIED, offerPredicates, offerComparator, localizable);
    }

    private <T extends Enum<T>> T getRandomEnumValue(Class<T> classType) {
        List<T> values = Arrays.asList(classType.getEnumConstants());
        Collections.shuffle(values);
        Optional<T> value = values.stream().findFirst();
        if (value.isPresent()) {
            return values.stream().findFirst().get();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
