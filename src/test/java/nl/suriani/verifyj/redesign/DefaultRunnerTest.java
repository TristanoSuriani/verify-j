package nl.suriani.verifyj.redesign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRunnerTest {

    private final static SimulationOptions simulationOptionsFailEarlyOnConstraintsViolation =
            new SimulationOptions(1, 50, 10, true);

    private final SimulationOptions simulationOptionsFailAtTheEnd =
            new SimulationOptions(1, 50, 10, false);

    @Test
    void failEarlyOnPostConditions() {
        var runner = new DefaultRunner<String>(simulationOptionsFailEarlyOnConstraintsViolation);
        var init = new Init<String>(() -> "Initial State");

        var toUpperCase = new NamedAction<String>("toUpperCase", s -> s); // action is not behaving as expected
        var toLowerCase = new NamedAction<String>("toLowerCase", String::toLowerCase);

        var step = new Step<>(toUpperCase, toLowerCase);

        var cannotMixUpperCaseAndLowerCase = new Postcondition<String>("cannotMixUpperCaseAndLowerCase",
                s -> s.chars().anyMatch(Character::isUpperCase) && s.chars().anyMatch(Character::isLowerCase));

        var specification = new Specification<>(init, step)
                .withPostconditions(cannotMixUpperCaseAndLowerCase);

        var report = runner.apply(specification);

        var outcomeSimulation = report.outcomeSimulations().getFirst();
        assertEquals(OutcomeSimulationStatus.FAILED_POSTCONDITIONS, outcomeSimulation.status());
        assertEquals("cannotMixUpperCaseAndLowerCase", outcomeSimulation.failedPostConditions().getFirst());
    }

    @Test
    void failEarlyOnInvariants() {
        var runner = new DefaultRunner<String>(simulationOptionsFailEarlyOnConstraintsViolation);
        var init = new Init<>(() -> "Initial State");

        var toUpperCase = new NamedAction<String>("toUpperCase", String::toUpperCase);
        var toLowerCase = new NamedAction<String>("toLowerCase", String::toLowerCase);

        var step = new Step<>(toUpperCase, toLowerCase);

        var stringMustBeEventuallyUpperCase = new Invariant<String>("stringMustBeEventuallyUpperCase",
                transitions -> transitions.stream()
                        .map(Transition::to)
                        .anyMatch(t -> t.toUpperCase().equals(t)));

        var specification = new Specification<>(init, step)
                .withInvariants(stringMustBeEventuallyUpperCase);

        var report = runner.apply(specification);

        var outcomeSimulation = report.outcomeSimulations().getFirst();
        assertEquals(OutcomeSimulationStatus.FAILED_INVARIANTS, outcomeSimulation.status());
        assertEquals("stringMustBeEventuallyUpperCase", outcomeSimulation.failedInvariants().getFirst());
    }

}