package meet_eat.data.predicate.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;

/**
 * Represents a {@link BiFunction} operation for {@link Collection} values.
 */
public enum CollectionOperation implements BiFunction<Collection<?>, Collection<?>, Boolean> {

    /**
     * The contain all operation.
     *
     * @see Collection#containsAll(Collection)
     */
    CONTAIN_ALL {
        @Override
        public Boolean apply(Collection<?> collectionArgument, Collection<?> collectionBase) {
            if (collectionArgument.isEmpty()) {
                return true;
            }
            return collectionBase.containsAll(collectionArgument);
        }
    },

    /**
     * The contain any operation comparing if a two {@link Collection}s have at least one element in common.
     */
    CONTAIN_ANY {
        @Override
        public Boolean apply(Collection<?> collectionArgument, Collection<?> collectionBase) {
            if (collectionArgument.isEmpty()) {
                return true;
            }
            int sizeBeforeRemoval = collectionBase.size();
            collectionBase.removeAll(collectionArgument);
            int sizeAfterRemoval = collectionBase.size();
            return sizeBeforeRemoval != sizeAfterRemoval;
        }
    },

    /**
     * The contain none operation comparing if two {@link Collection}s have no element in common.
     *
     * @see Collections#disjoint(Collection, Collection)
     */
    CONTAIN_NONE {
        @Override
        public Boolean apply(Collection<?> collectionArgument, Collection<?> collectionBase) {
            return Collections.disjoint(collectionArgument, collectionBase);
        }
    }
}
