package nl.suriani.verifyj.invariant;

import java.util.function.Predicate;

public interface Invariants {
    static<C> Invariant<C> eventually(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TemporalClause.EVENTUALLY
        );
    }

    static<C> Invariant<C> always(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TemporalClause.ALWAYS
        );
    }

    static<C> Invariant<C> never(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TemporalClause.NEVER
        );
    }

    static<C> Invariant<C> atLast(String description, Predicate<C> predicate) {
        return new Invariant<C>(
                description,
                predicate,
                TemporalClause.AT_LAST
        );
    }
}
