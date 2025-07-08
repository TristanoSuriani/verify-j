package nl.suriani.verifyj.redesign;

import java.util.Objects;

public record CounterExample<M>(Property failedProperty,
                                Transition<M> transition) {

    public CounterExample {
        Objects.requireNonNull(failedProperty);
        Objects.requireNonNull(transition);
    }
}
