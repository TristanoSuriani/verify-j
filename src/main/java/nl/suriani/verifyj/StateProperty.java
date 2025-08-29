package nl.suriani.verifyj;

import java.util.function.Predicate;

/**
 * Represents a state property for a model, defined by a name and a predicate.
 *
 * @param <M> the model type
 */
public record StateProperty<M>(String name, Predicate<M> predicate) implements Predicate<M>, Property {
    /**
     * Tests the property on the given model.
     *
     * @param m the model to test
     * @return true if the property holds, false otherwise
     */
    @Override
    public boolean test(M m) {
        return predicate.test(m);
    }

    /**
     * Returns a new StateProperty representing the logical AND of this and another property.
     *
     * @param other the other state property
     * @return the combined state property
     */
    public StateProperty<M> and(StateProperty <M> other) {
        return new StateProperty<>("(" + name + " AND " + other.name + ")", predicate.and(other.predicate));
    }

    /**
     * Returns a new StateProperty representing the logical OR of this and another property.
     *
     * @param other the other state property
     * @return the combined state property
     */
    public StateProperty<M> or(StateProperty <M> other) {
        return new StateProperty<>("(" + name + " OR " + other.name + ")", predicate.or(other.predicate));
    }

    /**
     * Returns a new StateProperty representing the logical NOT of this property.
     *
     * @return the negated state property
     */
    public StateProperty<M> not() {
        return new StateProperty<>("(NOT " + name + ")", predicate.negate());
    }
}
