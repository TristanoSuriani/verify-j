package nl.suriani.verifyj.redesign;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Specification<M>(
        Init<M> init,
        Step<M> step,
        List<Guard<M>> guards,
        List<Postcondition<M>> postconditions,
        List<Invariant<M>> invariants
) {
    public Specification(Init<M> init, Step<M> step) {
        this(init, step, List.of(), List.of(), List.of());
    }

    public Specification(Init<M> init,
                         Step<M> step,
                         List<Guard<M>> guards,
                         List<Postcondition<M>> postconditions,
                         List<Invariant<M>> invariants) {

        Objects.requireNonNull(init, "init is null");
        Objects.requireNonNull(step, "transitionNumber is null");
        Objects.requireNonNull(guards, "Guards is null");
        Objects.requireNonNull(postconditions, "postconditions is null");
        Objects.requireNonNull(invariants, "invariants is null");

        var guardNames = guards.stream()
                .map(Guard::name)
                .collect(Collectors.toSet());

        if (guardNames.size() != guards.size()) {
            throw new IllegalArgumentException("Guards must have unique names: " + guardNames);
        }

        var postconditionNames = postconditions.stream()
                .map(Postcondition::name)
                .collect(Collectors.toSet());

        if (postconditionNames.size() != postconditions.size()) {
            throw new IllegalArgumentException("Postconditions must have unique names: " + postconditionNames);
        }

        var invariantNames = invariants.stream()
                .map(Invariant::name)
                .collect(Collectors.toSet());

        if (invariantNames.size() != invariants.size()) {
            throw new IllegalArgumentException("Invariants must have unique names: " + invariantNames);
        }

        this.init = init;
        this.step = step;
        this.guards = List.copyOf(guards);
        this.postconditions = List.copyOf(postconditions);
        this.invariants = List.copyOf(invariants);
    }

    public Specification<M> withGuards(List<Guard<M>> guards) {
        return new Specification<>(init, step, guards, postconditions, invariants);
    }

    @SafeVarargs
    public final Specification<M> withGuards(Guard<M>... guards) {
        return new Specification<>(init, step, Arrays.asList(guards), postconditions, invariants);
    }

    public Specification<M> withPostconditions(List<Postcondition<M>> postconditions) {
        return new Specification<>(init, step, guards, postconditions, invariants);
    }

    @SafeVarargs
    public final Specification<M> withPostconditions(Postcondition<M>... postconditions) {
        return new Specification<>(init, step, guards, Arrays.asList(postconditions), invariants);
    }

    public Specification<M> withInvariants(List<Invariant<M>> invariants) {
        return new Specification<>(init, step, guards, postconditions, invariants);
    }

    @SafeVarargs
    public final Specification<M> withInvariants(Invariant<M>... invariants) {
        return new Specification<>(init, step, guards, postconditions, Arrays.asList(invariants));
    }
}
