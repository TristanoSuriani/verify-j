package nl.suriani.verifyj;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Simulator for executing specifications and generating reports.
 *
 * @param <M> the model type
 */
public class Simulator<M> implements ExecutionModel<M, Report<M>> {
    private final SimulationOptions simulationOptions;

    /**
     * Constructs a Simulator with the given simulation options.
     *
     * @param simulationOptions the simulation options
     */
    public Simulator(SimulationOptions simulationOptions) {
        this.simulationOptions = simulationOptions;
    }

    /**
     * Runs the given specification and returns a report.
     *
     * @param spec the specification to run
     * @return the report of the simulation
     */
    @Override
    public Report<M> run(Specification<M> spec) {
        var outcomes = Stream.iterate(0, i -> i < simulationOptions.numberOfSimulations(), i -> i + 1)
                .map(i -> runSimulation(spec))
                .toList();

        return new Report<>(outcomes);
    }

    private OutcomeSimulation<M> runSimulation(Specification<M> spec) {
        var attemptsCount = 1;
        var transitionsCount = 1;
        var transitions = new ArrayList<Transition<M>>();

        var maybeModel = tryInit(spec.init());

        if (maybeModel.isEmpty()) {
            return new OutcomeSimulation<M>(OutcomeSimulationStatus.FAILED_INIT);
        }

        M model = maybeModel.get();

        while (transitionsCount <= simulationOptions.maxTransitions()
            && attemptsCount <= simulationOptions.maxAttempts()) {

            var action = NonDet.oneOf(spec.step().actions());
            var maybeNewState = tryApplyAction(action, model);
            if (maybeNewState.isEmpty()) {
                attemptsCount++;
                continue;
            }
            var newState = maybeNewState.get();

            var failingStateProperties = spec.stateProperties().stream()
                    .filter(stateProperty -> stateProperty.not().test(newState))
                    .map(StateProperty::name)
                    .distinct()
                    .toList();

            if (!failingStateProperties.isEmpty()) {

                    return new OutcomeSimulation<M>(OutcomeSimulationStatus.FAILED_STATE_PROPERTIES)
                            .withFailedStateProperties(failingStateProperties)
                            .withTransitions(transitions);
            }

            var transition = new Transition<>(model, newState, action.name(), transitionsCount, attemptsCount);
            transitions.add(transition);
            transitionsCount++;
            attemptsCount++;
            model = newState;
        }

        var failingTemporalProperties = spec.temporalProperties().stream()
                .filter(temporalProperty -> temporalProperty.not().test(transitions))
                .map(TemporalProperty::name)
                .distinct()
                .toList();

        if (!failingTemporalProperties.isEmpty()) {
            return
                new OutcomeSimulation<M>(OutcomeSimulationStatus.FAILED_TEMPORAL_PROPERTIES)
                        .withFailedTemporalProperties(failingTemporalProperties)
                        .withTransitions(transitions);
        }

        return new OutcomeSimulation<M>(OutcomeSimulationStatus.SUCCESS)
                                .withTransitions(transitions);
    }

    /**
     * Attempts to initialize the model using the provided Init function.
     * Tries up to 100 times.
     *
     * @param init the initialization function
     * @return an Optional containing the model if successful, otherwise empty
     */
    private Optional<M> tryInit(Init<M> init) {
        for (var i = 0; i < 100; i++) {
            try {
                var model = init.apply(null);
                if (model != null) {
                    return Optional.of(model);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return Optional.empty();
    }

    /**
     * Attempts to apply the given action to the model.
     *
     * @param action the action to apply
     * @param model the model to apply the action to
     * @return an Optional containing the new model if successful, otherwise empty
     */
    private Optional<M> tryApplyAction(NamedAction<M> action, M model) {
        try {
            return Optional.of(action.apply(model));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}