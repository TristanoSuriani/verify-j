package nl.suriani.verifyj;

import java.util.Objects;

/**
 * Represents a counterexample found during verification, containing the failed property and the transition where it failed.
 *
 * @param <M> the model type
 */
public record CounterExample<M>(Property failedProperty,
                                Transition<M> transition) {
    /**
     * Constructs a CounterExample with the given failed property and transition.
     *
     * @param failedProperty the property that failed
     * @param transition the transition where the failure occurred
     */
    public CounterExample {
        Objects.requireNonNull(failedProperty);
        Objects.requireNonNull(transition);
    }
}
