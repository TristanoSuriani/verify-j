package nl.suriani.verifyj;

import java.util.List;
import java.util.Objects;

public record OutcomeSimulation<M>(OutcomeSimulationStatus status,
                                   List<Transition<M>> transitions,
                                   List<String> failedStateProperties,
                                   List<String> failedTemporalProperties) {

    public OutcomeSimulation {
        Objects.requireNonNull(status);
        Objects.requireNonNull(transitions);
        Objects.requireNonNull(failedStateProperties);
        Objects.requireNonNull(failedTemporalProperties);
    }

    public OutcomeSimulation(List<Transition<M>> transitions, OutcomeSimulationStatus status) {
        this(status, transitions, List.of(), List.of());
    }

    public OutcomeSimulation(OutcomeSimulationStatus status) {
        this(status, List.of(), List.of(), List.of());
    }

    public OutcomeSimulation<M> withTransitions(List<Transition<M>> transitions) {
        return new OutcomeSimulation<>(status, transitions, failedStateProperties, failedTemporalProperties);
    }

    public OutcomeSimulation<M> withFailedStateProperties(List<String> failedPostConditions) {
        return new OutcomeSimulation<>(status, transitions, failedPostConditions, failedTemporalProperties);
    }

    public OutcomeSimulation<M> withFailedTemporalProperties(List<String> failedTemporalProperties) {
        return new OutcomeSimulation<>(status, transitions, failedStateProperties, failedTemporalProperties);
    }

    public OutcomeSimulation<M> withStatus(OutcomeSimulationStatus status) {
        return new OutcomeSimulation<>(status, transitions, failedStateProperties, failedTemporalProperties);
    }
}
