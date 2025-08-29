package nl.suriani.verifyj;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a temporal property over a list of transitions, with a name and a predicate.
 *
 * @param name the name of the property
 * @param predicate the predicate to evaluate over a list of transitions
 * @param <M> the model type
 */
public record TemporalProperty<M>(String name, Predicate<List<Transition<M>>> predicate) implements Predicate<List<Transition<M>>>, Property {
    /**
     * Tests the predicate on the given list of transitions.
     *
     * @param transitions the list of transitions to test
     * @return true if the predicate holds, false otherwise
     */
    @Override
    public boolean test(List<Transition<M>> transitions) {
        return predicate.test(transitions);
    }

    /**
     * Returns a new TemporalProperty representing the logical AND of this property and another.
     *
     * @param other the other temporal property
     * @return a new TemporalProperty representing the conjunction
     */
    public TemporalProperty<M> and(TemporalProperty <M> other) {
        return new TemporalProperty<>("(" + name + " AND " + other.name + ")", predicate.and(other.predicate));
    }

    /**
     * Returns a new TemporalProperty representing the logical OR of this property and another.
     *
     * @param other the other temporal property
     * @return a new TemporalProperty representing the disjunction
     */
    public TemporalProperty<M> or(TemporalProperty <M> other) {
        return new TemporalProperty<>("(" + name + " OR " + other.name + ")", predicate.or(other.predicate));
    }

    /**
     * Returns a new TemporalProperty representing the logical negation of this property.
     *
     * @return a new TemporalProperty representing the negation
     */
    public TemporalProperty<M> not() {
        return new TemporalProperty<>("(NOT " + name + ")", predicate.negate());
    }
}
