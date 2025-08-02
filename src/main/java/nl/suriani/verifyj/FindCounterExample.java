package nl.suriani.verifyj;

import java.util.ArrayList;
import java.util.Optional;

public class FindCounterExample<M> implements ExecutionModel<M, Optional<CounterExample<M>>> {
    private FindCounterExampleOptions options;

    public FindCounterExample(FindCounterExampleOptions options) {
        this.options = options;
    }

    @Override
    public Optional<CounterExample<M>> run(Specification<M> spec) {
        var attemptsCount = 1;
        var transitionsCount = 1;
        var transitions = new ArrayList<Transition<M>>();

        var maybeModel = tryInit(spec.init());

        if (maybeModel.isEmpty()) {
            throw new IllegalStateException("Could not initialize model after 100 attempts");
        }

        M model = maybeModel.get();

        while (transitionsCount <= options.maxTransitions()
                && attemptsCount <= options.maxAttempts()) {

            var action = NonDet.oneOf(spec.step().actions());
            var maybeNewState = tryApplyAction(action, model);
            if (maybeNewState.isEmpty()) {
                attemptsCount++;
                continue;
            }
            var newState = maybeNewState.get();

            var failingStateProperties = spec.stateProperties().stream()
                    .filter(stateProperty -> stateProperty.not().test(newState))
                    .toList();

            var transition = new Transition<>(model, newState, action.name(), transitionsCount, attemptsCount);

            if (!failingStateProperties.isEmpty()) {
                return Optional.of(new CounterExample<M>(failingStateProperties.getFirst(), transition));
            }


            transitions.add(transition);
            transitionsCount++;
            attemptsCount++;
            model = newState;
        }

        return Optional.empty();
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
            return Optional.of(action.apply(model));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}