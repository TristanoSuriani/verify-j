package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.Objects;
import java.util.List;

public record Step<M>(List<NamedAction<M>> actions) {

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

    @SafeVarargs
    public Step(NamedAction<M>... actions) {
        this(Arrays.asList(actions));
    }
}
