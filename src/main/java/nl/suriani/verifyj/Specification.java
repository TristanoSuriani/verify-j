package nl.suriani.verifyj;

import nl.suriani.verifyj.action.Action;
import nl.suriani.verifyj.action.Any;
import nl.suriani.verifyj.action.NonDet;
import nl.suriani.verifyj.invariant.Invariant;

import java.util.ArrayList;
import java.util.List;

public record Specification<C>(Action<C> init, Any<C> step, SpecificationOptions options, Invariant<C>... invariants) {

    public C run() {
        var context = init.execute(null);
        var stepCount = 1;
        var attemptCount = 1;
        boolean[] invariantsSatisfaction = initialiseInvariantSatisfactionVector();
        updateInvariantSatisfactionVector(context, invariantsSatisfaction);
        var failingInvariantsAfterInit = intermediateCheckInvariantSatisfactionVector(invariantsSatisfaction);
        if (!failingInvariantsAfterInit.isEmpty()) {
            System.out.println("Specifications failed at step " + stepCount + ": " + failingInvariantsAfterInit.stream()
                    .map(Invariant::description)
                    .toList());

            return context;
        }
        while (stepCount <= options.numberOfSteps() && attemptCount <= options.maxAttempts()) {
            try {
                //context = step.execute(context);
                var action = NonDet.oneOf(step.actions());
                var actionName = action instanceof Action.NamedAction<C> namedAction ? namedAction.name() : "unknown";
                context = action.execute(context);
                var debug = String.format("[%s/%s] (%s) - %s", stepCount, attemptCount, actionName, context);
                System.out.println(debug);
                updateInvariantSatisfactionVector(context, invariantsSatisfaction);
                var failingInvariants = intermediateCheckInvariantSatisfactionVector(invariantsSatisfaction);
                if (!failingInvariants.isEmpty()) {
                    System.out.println("Specifications failed at step " + stepCount + ": " + failingInvariants.stream()
                            .map(Invariant::description)
                            .toList());

                    return context;
                }

                stepCount++;
            } catch (Exception exception) {
                // perfectly fine to happen, just retry
            } finally {
                attemptCount++;
            }

        }
        var failingInvariants = finalCheckInvariantSatisfactionVector(invariantsSatisfaction);
        if (!failingInvariants.isEmpty()) {
            System.out.println("Specification failed at step " + stepCount + ": " + failingInvariants.stream()
                    .map(Invariant::description)
                    .toList());
        }
        return context;
    }

    private boolean[] initialiseInvariantSatisfactionVector() {
        boolean[] invariantSatisfied = new boolean[invariants.length];
        for (int i = 0; i < invariantSatisfied.length; i++) {
            var invariant = invariants[i];
            invariantSatisfied[i] = switch (invariant.type()) {
                case EVENTUALLY -> false;
                case ALWAYS -> true;
                case NEVER -> false;
            };
        }
        return invariantSatisfied;
    }

    private void updateInvariantSatisfactionVector(C context, boolean[] invariantSatisfaction) {
        for (int i = 0; i < invariantSatisfaction.length; i++) {
            var invariant = invariants[i];
            invariantSatisfaction[i] = switch (invariant.type()) {
                case EVENTUALLY -> invariantSatisfaction[i] || invariant.isSatisfiedBy(context);
                case ALWAYS -> invariantSatisfaction[i] && invariant.isSatisfiedBy(context);
                case NEVER -> !invariantSatisfaction[i] || !invariant.isSatisfiedBy(context);
            };
        }
    }

    private List<Invariant<C>> intermediateCheckInvariantSatisfactionVector(boolean[] invariantSatisfaction) {
        var failingInvariants = new ArrayList<Invariant<C>>();
        for (int i = 0; i < invariantSatisfaction.length; i++) {
            var invariant = invariants[i];
            if (!invariantSatisfaction[i]) {
                switch (invariant.type()) {
                    case ALWAYS, NEVER -> {
                        failingInvariants.add(invariant);
                    }
                    default -> {}
                }
            }
        }
        return failingInvariants;
    }

    private List<Invariant<C>> finalCheckInvariantSatisfactionVector(boolean[] invariantSatisfaction) {
        var failingInvariants = new ArrayList<Invariant<C>>();
        for (int i = 0; i < invariantSatisfaction.length; i++) {
            var invariant = invariants[i];
            if (!invariantSatisfaction[i]) {
                failingInvariants.add(invariant);
            }
        }
        return failingInvariants;
    }
}
