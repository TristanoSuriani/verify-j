package nl.suriani.verifyj.misc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public interface Range {
    static List<Integer> enumerate(Integer from, Integer to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        var step = to < from ? 1: -1;
            return Stream.iterate(from, i -> i + step)
                    .limit(to)
                    .toList();
    }

}
