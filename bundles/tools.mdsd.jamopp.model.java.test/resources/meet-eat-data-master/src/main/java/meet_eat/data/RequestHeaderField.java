package meet_eat.data;

/**
 * Represents types which can be passed into request headers used for HTTP requests.
 */
public final class RequestHeaderField {

    /**
     * The string representation of the header field identifier: Token.
     */
    public static final String TOKEN = "token";

    /**
     * The string representation of the header field identifier: Page.
     */
    public static final String PAGE = "page";

    /**
     * The string representation of the header field identifier: Predicates.
     */
    public static final String PREDICATES = "predicates";

    /**
     * The string representation of the header field identifier: Comparators.
     */
    public static final String COMPARATORS = "comparators";

    private RequestHeaderField() {
    }
}
