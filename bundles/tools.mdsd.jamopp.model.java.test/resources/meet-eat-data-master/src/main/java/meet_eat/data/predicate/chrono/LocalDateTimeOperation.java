package meet_eat.data.predicate.chrono;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

/**
 * An enumeration containing boolean returning {@link BiFunction BiFunctions}
 * applicable to {@link LocalDateTime} instances.
 */
public enum LocalDateTimeOperation implements BiFunction<LocalDateTime, LocalDateTime, Boolean> {

    /**
     * The before operation.
     *
     * @see LocalDateTime#isBefore
     */
    BEFORE {
        @Override
        public Boolean apply(LocalDateTime LocalDateTimeArgument, LocalDateTime LocalDateTimeBase) {
            return LocalDateTimeBase.isBefore(LocalDateTimeArgument);
        }
    },

    /**
     * The equal operation.
     *
     * @see LocalDateTime#equals
     */
    EQUAL {
        @Override
        public Boolean apply(LocalDateTime LocalDateTimeArgument, LocalDateTime LocalDateTimeBase) {
            return LocalDateTimeBase.isEqual(LocalDateTimeArgument);
        }
    },

    /**
     * The after operation.
     *
     * @see LocalDateTime#isAfter
     */
    AFTER {
        @Override
        public Boolean apply(LocalDateTime LocalDateTimeArgument, LocalDateTime LocalDateTimeBase) {
            return LocalDateTimeBase.isAfter(LocalDateTimeArgument);
        }
    }
}
