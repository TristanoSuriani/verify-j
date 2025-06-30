package nl.suriani.verifyj.invariant;

import java.util.Objects;
import java.util.function.Predicate;

public record Invariant<C>(
        String description,
        Predicate<C> predicate,
        TypeInvariant type
) {
    public Invariant {
        Objects.requireNonNull(description);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(type);
    }

    public boolean isSatisfiedBy(C context) {
        return predicate.test(context);
    }
}
