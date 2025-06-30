package nl.suriani.verifyj.invariant;

import java.util.function.Predicate;

public interface Invariants {
    static<C> Invariant<C> eventually(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TypeInvariant.EVENTUALLY
        );
    }

    static<C> Invariant<C> always(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TypeInvariant.ALWAYS
        );
    }

    static<C> Invariant<C> never(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TypeInvariant.NEVER
        );
    }
}
