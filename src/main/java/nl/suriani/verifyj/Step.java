package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.Objects;
import java.util.List;

/**
 * Represents a step in a specification, containing a list of named actions.
 *
 * @param <M> the model type
 */
public record Step<M>(List<NamedAction<M>> actions) {
    /**
     * Constructs a Step with the given list of actions.
     *
     * @param actions the list of named actions
     * @throws IllegalArgumentException if the list is empty or contains duplicate action names
     */
    public Step {
        Objects.requireNonNull(actions);
        if (actions.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one action");
        }

        var actionNames = actions.stream()
                .map(NamedAction::name)
                .toList();

        var uniqueActionNames = actions.stream()
                .map(NamedAction::name)
                .distinct()
                .toList();

        if (actionNames.size() != uniqueActionNames.size()) {
            throw new IllegalArgumentException("Actions must have unique names: " + actionNames);
        }
    }

    /**
     * Constructs a Step with the given varargs of named actions.
     *
     * @param actions the named actions
     */
    @SafeVarargs
    public Step(NamedAction<M>... actions) {
        this(Arrays.asList(actions));
    }
}
