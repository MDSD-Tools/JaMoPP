package meet_eat.data.location;

/**
 * Thrown to indicate that a location has been unlocalizable.
 */
public class UnlocalizableException extends Exception {

    private static final long serialVersionUID = -8479470469258687628L;

    /**
     * Creates an {@code UnlocalizableException} with no detail message.
     */
    public UnlocalizableException() {
        super();
    }

    /**
     * Creates an {@code UnlocalizableException} with the specified detail message.
     *
     * @param message the detail message
     */
    public UnlocalizableException(String message) {
        super(message);
    }
}
