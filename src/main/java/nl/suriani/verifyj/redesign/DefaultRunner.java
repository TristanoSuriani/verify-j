package nl.suriani.verifyj.redesign;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultRunner<M> implements Runner<M> {
    private final SimulationOptions simulationOptions;

    public DefaultRunner(SimulationOptions simulationOptions) {
        this.simulationOptions = simulationOptions;
    }

    @Override
    public Report<M> apply(Specification<M> spec) {
        var attemptsCount = 1;
        var transitionsCount = 1;
        var transitions = new ArrayList<Transition<M>>();

        var maybeModel = tryInit(spec.init());

        // what if init fails consistently?

        M model = maybeModel.get();

        while (transitionsCount <= simulationOptions.maxTransitions()
            && attemptsCount <= simulationOptions.maxAttempts()) {

            var action = NonDet.oneOf(spec.step().actions());
            var maybeNewState = tryApplyAction(action, model);
            var newState = maybeNewState.get();

            var failingStateProperties = spec.stateProperties().stream()
                    .filter(stateProperty -> stateProperty.not().test(newState))
                    .map(StateProperty::name)
                    .distinct()
                    .toList();

            if (!failingStateProperties.isEmpty()) {
                return new Report<M>(
                        List.of(
                                new OutcomeSimulation<M>(OutcomeSimulationStatus.FAILED_STATE_PROPERTIES)
                                        .withFailedStateProperties(failingStateProperties)
                        )
                );
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
            return new Report<M>(
                    List.of(
                            new OutcomeSimulation<M>(OutcomeSimulationStatus.FAILED_TEMPORAL_PROPERTIES)
                                    .withFailedTemporalProperties(failingTemporalProperties)
                    )
            );
        }

        return new Report<M>(
                List.of(
                        new OutcomeSimulation<M>(OutcomeSimulationStatus.SUCCESS)
                )
        );
    }

    private Optional<M> tryInit(Init<M> init) {
        for (var i = 0; i < 100; i++) {
            var model = init.apply(null);
            if (model != null) {
                return Optional.of(model);
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
