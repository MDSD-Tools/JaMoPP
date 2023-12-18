package meet_eat.data.factory;

import meet_eat.data.entity.user.Email;

public class EmailFactory extends ObjectFactory<Email> {

    public EmailFactory() {
        super();
    }

    @Override
    protected Email createObject() {
        return new Email("test.address" + objectCounter + "@example.com");
    }
}
