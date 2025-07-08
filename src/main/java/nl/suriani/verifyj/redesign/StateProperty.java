package nl.suriani.verifyj.redesign;

import java.util.function.Predicate;

public record StateProperty<M>(String name, Predicate<M> predicate) implements Predicate<M>, Property {
    @Override
    public boolean test(M m) {
        return predicate.test(m);
    }

    public StateProperty<M> and(StateProperty <M> other) {
        return new StateProperty<>("(" + name + " AND " + other.name + ")", predicate.and(other.predicate));
    }

    public StateProperty<M> or(StateProperty <M> other) {
        return new StateProperty<>("(" + name + " OR " + other.name + ")", predicate.or(other.predicate));
    }

    public StateProperty<M> not() {
        return new StateProperty<>("(NOT " + name + ")", predicate.negate());
    }
}
