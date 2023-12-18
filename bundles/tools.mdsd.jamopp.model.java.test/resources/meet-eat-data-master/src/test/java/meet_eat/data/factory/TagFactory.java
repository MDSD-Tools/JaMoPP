package meet_eat.data.factory;

import meet_eat.data.entity.Tag;

public class TagFactory extends ObjectFactory<Tag> {

    public TagFactory() {
        super();
    }

    @Override
    protected Tag createObject() {
        String identifier = Integer.toString(objectCounter);
        String name = "TestTag" + objectCounter;
        return new Tag(identifier, name);
    }
}
