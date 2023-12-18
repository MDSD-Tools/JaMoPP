package meet_eat.data.factory;

import meet_eat.data.entity.user.Password;

public class PasswordFactory extends ObjectFactory<Password> {

    public PasswordFactory() {
        super();
    }

    @Override
    protected Password createObject() {
        return Password.createHashedPassword("MyVeryStr0ngPW!" + objectCounter);
    }
}
