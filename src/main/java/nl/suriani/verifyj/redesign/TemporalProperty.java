package nl.suriani.verifyj.redesign;

import java.util.List;
import java.util.function.Predicate;

public record TemporalProperty<M>(String name, Predicate<List<Transition<M>>> predicate) implements Predicate<List<Transition<M>>>, Property {
    @Override
    public boolean test(List<Transition<M>> transitions) {
        return predicate.test(transitions);
    }

    public TemporalProperty<M> and(TemporalProperty <M> other) {
        return new TemporalProperty<>("(" + name + " AND " + other.name + ")", predicate.and(other.predicate));
    }

    public TemporalProperty<M> or(TemporalProperty <M> other) {
        return new TemporalProperty<>("(" + name + " OR " + other.name + ")", predicate.or(other.predicate));
    }

    public TemporalProperty<M> not() {
        return new TemporalProperty<>("(NOT " + name + ")", predicate.negate());
    }
}
