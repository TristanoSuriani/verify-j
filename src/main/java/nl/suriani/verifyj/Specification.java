package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Specification<M>(
        Init<M> init,
        Step<M> step,
        List<StateProperty<M>> stateProperties,
        List<TemporalProperty<M>> temporalProperties
) {
    public Specification(Init<M> init, Step<M> step) {
        this(init, step, List.of(), List.of());
    }

    public Specification(Init<M> init,
                         Step<M> step,
                         List<StateProperty<M>> stateProperties,
                         List<TemporalProperty<M>> temporalProperties) {

        Objects.requireNonNull(init, "init is null");
        Objects.requireNonNull(step, "step is null");
        Objects.requireNonNull(stateProperties, "stateProperties is null");
        Objects.requireNonNull(temporalProperties, "temporalProperties is null");

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

        var allNames = new HashSet<>(statePropertiesNames);
        allNames.addAll(temporalPropertiesNames);

        if (allNames.size() != statePropertiesNames.size() + temporalPropertiesNames.size()) {
            throw new IllegalArgumentException("All properties (state, temporal) must have unique names");
        }

        this.init = init;
        this.step = step;
        this.stateProperties = List.copyOf(stateProperties);
        this.temporalProperties = List.copyOf(temporalProperties);
    }
    
    public Specification<M> withStateProperty(List<StateProperty<M>> stateProperties) {
        return new Specification<>(init, step, stateProperties, temporalProperties);
    }

    @SafeVarargs
    public final Specification<M> withStateProperty(StateProperty<M>... stateProperties) {
        return new Specification<>(init, step, Arrays.asList(stateProperties), temporalProperties);
    }

    public Specification<M> withTemporalProperties(List<TemporalProperty<M>> temporalProperties) {
        return new Specification<>(init, step, stateProperties, temporalProperties);
    }

    @SafeVarargs
    public final Specification<M> withTemporalProperties(TemporalProperty<M>... temporalProperties) {
        return new Specification<>(init, step, stateProperties, Arrays.asList(temporalProperties));
    }
}
