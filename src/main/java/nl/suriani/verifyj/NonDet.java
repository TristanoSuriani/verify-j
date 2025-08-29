package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.List;

/**
 * Utility interface for non-deterministic operations, such as random selection.
 */
public interface NonDet {
    /**
     * Selects one value at random from the given values.
     *
     * @param values the values to choose from
     * @return a randomly selected value
     * @param <T> the type of values
     */
    static <T> T oneOf(T... values) {
        return oneOf(Arrays.asList(values));
    }

    /**
     * Selects one value at random from the given list.
     *
     * @param values the list of values to choose from
     * @return a randomly selected value
     * @param <T> the type of values
     */
    static <T> T oneOf(List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("At least one value must be provided");
        }
        return values.get((int) (Math.random() * values.size()));
    }

    /**
     * Returns a random integer within the specified range (min inclusive, max exclusive).
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a random integer within the range
     */
    static int withinRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        return min + (int) (Math.random() * (max - min));
    }
}
