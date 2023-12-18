package meet_eat.data.factory;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;

public class TokenFactory extends ObjectFactory<Token> {

    private final UserFactory userFactory;

    public TokenFactory() {
        super();
        userFactory = new UserFactory();
    }

    @Override
    protected Token createObject() {
        String identifier = Integer.toString(objectCounter);
        User user = userFactory.getValidObject();
        String value = "This is value " + objectCounter;
        return new Token(identifier, user, value);
    }
}
