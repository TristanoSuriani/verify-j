package nl.suriani.verifyj;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class Simulator<M> implements ExecutionModel<M, Report<M>> {
    private final SimulationOptions simulationOptions;

    public Simulator(SimulationOptions simulationOptions) {
        this.simulationOptions = simulationOptions;
    }

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

    private Optional<M> tryApplyAction(NamedAction<M> action, M model) {
        try {
            return Optional.of(action.expression().apply(model));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
