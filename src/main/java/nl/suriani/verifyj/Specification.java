package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Specification<M>(
        Init<M> init,
        Step<M> step,
        List<Guard<M>> guards,
        List<StateProperty<M>> stateProperties,
        List<TemporalProperty<M>> temporalProperties
) {
    public Specification(Init<M> init, Step<M> step) {
        this(init, step, List.of(), List.of(), List.of());
    }

    public Specification(Init<M> init,
                         Step<M> step,
                         List<Guard<M>> guards,
                         List<StateProperty<M>> stateProperties,
                         List<TemporalProperty<M>> temporalProperties) {

        Objects.requireNonNull(init, "init is null");
        Objects.requireNonNull(step, "step is null");
        Objects.requireNonNull(guards, "Guards is null");
        Objects.requireNonNull(stateProperties, "stateProperties is null");
        Objects.requireNonNull(temporalProperties, "temporalProperties is null");

        var guardNames = guards.stream()
                .map(Guard::name)
                .collect(Collectors.toSet());

        if (guardNames.size() != guards.size()) {
            throw new IllegalArgumentException("Guards must have unique names: " + guardNames);
        }

        var statePropertiesNames = stateProperties.stream()
                .map(StateProperty::name)
                .collect(Collectors.toSet());

        if (statePropertiesNames.size() != stateProperties.size()) {
            throw new IllegalArgumentException("State properties must have unique names: " + statePropertiesNames);
        }

        var temporalPropertiesNames = temporalProperties.stream()
                .map(TemporalProperty::name)
                .collect(Collectors.toSet());

        if (temporalPropertiesNames.size() != temporalProperties.size()) {
            throw new IllegalArgumentException("Temporal properties must have unique names: " + temporalPropertiesNames);
        }

        var allNames = new HashSet<>(guardNames);
        allNames.addAll(statePropertiesNames);
        allNames.addAll(temporalPropertiesNames);

        if (allNames.size() != guardNames.size() + statePropertiesNames.size() + temporalPropertiesNames.size()) {
            throw new IllegalArgumentException("All properties (guards, state, temporal) must have unique names");
        }

        this.init = init;
        this.step = step;
        this.guards = List.copyOf(guards);
        this.stateProperties = List.copyOf(stateProperties);
        this.temporalProperties = List.copyOf(temporalProperties);
    }

    public Specification<M> withGuards(List<Guard<M>> guards) {
        return new Specification<>(init, step, guards, stateProperties, temporalProperties);
    }

    @SafeVarargs
    public final Specification<M> withGuards(Guard<M>... guards) {
        return new Specification<>(init, step, Arrays.asList(guards), stateProperties, temporalProperties);
    }

    public Specification<M> withStateProperty(List<StateProperty<M>> stateProperties) {
        return new Specification<>(init, step, guards, stateProperties, temporalProperties);
    }

    @SafeVarargs
    public final Specification<M> withStateProperty(StateProperty<M>... stateProperties) {
        return new Specification<>(init, step, guards, Arrays.asList(stateProperties), temporalProperties);
    }

    public Specification<M> withTemporalProperties(List<TemporalProperty<M>> temporalProperties) {
        return new Specification<>(init, step, guards, stateProperties, temporalProperties);
    }

    @SafeVarargs
    public final Specification<M> withTemporalProperties(TemporalProperty<M>... temporalProperties) {
        return new Specification<>(init, step, guards, stateProperties, Arrays.asList(temporalProperties));
    }
}
