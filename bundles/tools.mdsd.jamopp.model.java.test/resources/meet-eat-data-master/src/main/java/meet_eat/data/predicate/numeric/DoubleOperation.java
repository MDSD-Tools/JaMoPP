package meet_eat.data.predicate.numeric;

import java.util.function.BiFunction;

/**
 * Represents a {@link BiFunction} operation for {@link Double} values.
 */
public enum DoubleOperation implements BiFunction<Double, Double, Boolean> {

    /**
     * The greater operation comparing two {@link Double} values.
     *
     * @see Double#compare(double, double)
     */
    GREATER {
        @Override
        public Boolean apply(Double doubleArgument, Double doubleBase) {
            return Double.compare(doubleBase, doubleArgument) > 0;
        }
    },

    /**
     * The equal operation comparing two {@link Double} values.
     *
     * @see Double#compare(double, double)
     */
    EQUAL {
        @Override
        public Boolean apply(Double doubleArgument, Double doubleBase) {
            return Double.compare(doubleBase, doubleArgument) == 0;
        }
    },

    /**
     * The less operation comparing two {@link Double} values.
     *
     * @see Double#compare(double, double)
     */
    LESS {
        @Override
        public Boolean apply(Double doubleArgument, Double doubleBase) {
            return Double.compare(doubleBase, doubleArgument) < 0;
        }
    }
}
