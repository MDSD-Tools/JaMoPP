package meet_eat.data.predicate.string;

import java.util.function.BiFunction;

/**
 * Represents a {@link BiFunction} operation for {@link String} values.
 */
public enum StringOperation implements BiFunction<String, String, Boolean> {

    /**
     * The contain operation.
     *
     * @see String#contains(CharSequence)
     */
    CONTAIN {
        @Override
        public Boolean apply(String stringArgument, String stringBase) {
            return stringBase.contains(stringArgument);
        }
    },

    /**
     * Opposite of the contain operation.
     *
     * @see String#contains(CharSequence)
     */
    NOT_CONTAIN {
        @Override
        public Boolean apply(String stringArgument, String stringBase) {
            return !stringBase.contains(stringArgument);
        }
    },

    /**
     * The equal operation.
     *
     * @see String#equals(Object)
     */
    EQUAL {
        @Override
        public Boolean apply(String stringArgument, String stringBase) {
            return stringBase.equals(stringArgument);
        }
    },

    /**
     * The starts with operation.
     *
     * @see String#startsWith(String)
     */
    STARTS_WITH {
        @Override
        public Boolean apply(String stringArgument, String stringBase) {
            return stringBase.startsWith(stringArgument);
        }
    },

    /**
     * The ends with operation.
     *
     * @see String#endsWith(String)
     */
    ENDS_WITH {
        @Override
        public Boolean apply(String stringArgument, String stringBase) {
            return stringBase.endsWith(stringArgument);
        }
    }
}
