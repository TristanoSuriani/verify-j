package nl.suriani.verifyj;

import java.util.List;
import java.util.Objects;

/**
 * Represents the outcome of a single simulation, including status, transitions, and failed properties.
 *
 * @param <M> the model type
 */
public record OutcomeSimulation<M>(OutcomeSimulationStatus status,
                                   List<Transition<M>> transitions,
                                   List<String> failedStateProperties,
                                   List<String> failedTemporalProperties) {
    /**
     * Constructs an OutcomeSimulation with the given status, transitions, and failed properties.
     *
     * @param status the simulation status
     * @param transitions the list of transitions
     * @param failedStateProperties the names of failed state properties
     * @param failedTemporalProperties the names of failed temporal properties
     */
    public OutcomeSimulation {
        Objects.requireNonNull(status);
        Objects.requireNonNull(transitions);
        Objects.requireNonNull(failedStateProperties);
        Objects.requireNonNull(failedTemporalProperties);
    }

    /**
     * Constructs an OutcomeSimulation with the given transitions and status.
     *
     * @param transitions the list of transitions
     * @param status the simulation status
     */
    public OutcomeSimulation(List<Transition<M>> transitions, OutcomeSimulationStatus status) {
        this(status, transitions, List.of(), List.of());
    }

    /**
     * Constructs an OutcomeSimulation with the given status and empty lists for transitions and failed properties.
     *
     * @param status the simulation status
     */
    public OutcomeSimulation(OutcomeSimulationStatus status) {
        this(status, List.of(), List.of(), List.of());
    }

    /**
     * Returns a new OutcomeSimulation with the given transitions.
     *
     * @param transitions the list of transitions
     * @return a new OutcomeSimulation with updated transitions
     */
    public OutcomeSimulation<M> withTransitions(List<Transition<M>> transitions) {
        return new OutcomeSimulation<>(status, transitions, failedStateProperties, failedTemporalProperties);
    }

    /**
     * Returns a new OutcomeSimulation with the given failed state properties.
     *
     * @param failedPostConditions the names of failed state properties
     * @return a new OutcomeSimulation with updated failed state properties
     */
    public OutcomeSimulation<M> withFailedStateProperties(List<String> failedPostConditions) {
        return new OutcomeSimulation<>(status, transitions, failedPostConditions, failedTemporalProperties);
    }

    /**
     * Returns a new OutcomeSimulation with the given failed temporal properties.
     *
     * @param failedTemporalProperties the names of failed temporal properties
     * @return a new OutcomeSimulation with updated failed temporal properties
     */
    public OutcomeSimulation<M> withFailedTemporalProperties(List<String> failedTemporalProperties) {
        return new OutcomeSimulation<>(status, transitions, failedStateProperties, failedTemporalProperties);
    }

    /**
     * Returns a new OutcomeSimulation with the given status.
     *
     * @param status the new status
     * @return a new OutcomeSimulation with updated status
     */
    public OutcomeSimulation<M> withStatus(OutcomeSimulationStatus status) {
        return new OutcomeSimulation<>(status, transitions, failedStateProperties, failedTemporalProperties);
    }
}
