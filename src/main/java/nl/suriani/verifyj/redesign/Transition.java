package nl.suriani.verifyj.redesign;

import java.util.Objects;

public record Transition<M>(M from, M to, String actionName, Integer transitionNumber, Integer attemptNumber) {

    public Transition {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(actionName);
        if (transitionNumber == null || transitionNumber < 0) {
            throw new IllegalArgumentException("transitionNumber must be a non-negative integer");
        }
        if (attemptNumber == null || attemptNumber < 0) {
            throw new IllegalArgumentException("attemptNumber must be a non-negative integer");
        }
        if (actionName.isBlank()) {
            throw new IllegalArgumentException("Action name must not be blank");
        }
    }
}
