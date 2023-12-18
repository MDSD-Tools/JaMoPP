package meet_eat.data.factory;

import java.util.concurrent.ThreadLocalRandom;

public abstract class ObjectFactory<T> {

    private static final int LOWER_BOUND = 0;
    private static final int UPPER_BOUND = 10000;

    protected int objectCounter;

    protected ObjectFactory() {
        objectCounter = ThreadLocalRandom.current().nextInt(LOWER_BOUND, UPPER_BOUND);
    }

    public T getValidObject() {
        T object = createObject();
        objectCounter++;
        return object;
    }

    protected abstract T createObject();
}
