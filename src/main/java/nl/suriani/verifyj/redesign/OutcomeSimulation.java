package nl.suriani.verifyj.redesign;

import java.util.List;
import java.util.Objects;

public record OutcomeSimulation<M>(OutcomeSimulationStatus status,
                                   List<Transition<M>> transitions,
                                   List<String> failedPostConditions,
                                   List<String> failedInvariants) {

    public OutcomeSimulation {
        Objects.requireNonNull(status);
        Objects.requireNonNull(transitions);
        Objects.requireNonNull(failedPostConditions);
        Objects.requireNonNull(failedInvariants);
    }

    public OutcomeSimulation(List<Transition<M>> transitions, OutcomeSimulationStatus status) {
        this(status, transitions, List.of(), List.of());
    }

    public OutcomeSimulation(OutcomeSimulationStatus status) {
        this(status, List.of(), List.of(), List.of());
    }

    public OutcomeSimulation<M> withTransitions(List<Transition<M>> transitions) {
        return new OutcomeSimulation<>(status, transitions, failedPostConditions, failedInvariants);
    }

    public OutcomeSimulation<M> withFailedPostConditions(List<String> failedPostConditions) {
        return new OutcomeSimulation<>(status, transitions, failedPostConditions, failedInvariants);
    }

    public OutcomeSimulation<M> withFailedInvariants(List<String> failedInvariants) {
        return new OutcomeSimulation<>(status, transitions, failedPostConditions, failedInvariants);
    }

    public OutcomeSimulation<M> withStatus(OutcomeSimulationStatus status) {
        return new OutcomeSimulation<>(status, transitions, failedPostConditions, failedInvariants);
    }
}
