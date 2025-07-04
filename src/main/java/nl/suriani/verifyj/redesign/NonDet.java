package nl.suriani.verifyj.redesign;

import java.util.Arrays;
import java.util.List;

public interface NonDet {
    static <T> T oneOf(T... values) {
        return oneOf(Arrays.asList(values));
    }

    static <T> T oneOf(List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("At least one value must be provided");
        }
        return values.get((int) (Math.random() * values.size()));
    }

    static int withinRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        return min + (int) (Math.random() * (max - min));
    }
}
