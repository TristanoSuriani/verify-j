package nl.suriani.verifyj.redesign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRunnerTest {

    private final static SimulationOptions simulationOptionsFailEarlyOnConstraintsViolation =
            new SimulationOptions(1, 50, 10, true);

    private final SimulationOptions simulationOptionsFailAtTheEnd =
            new SimulationOptions(1, 50, 10, false);

    @Test
    void failEarlyOnStateProperties() {
        var runner = new DefaultRunner<String>(simulationOptionsFailEarlyOnConstraintsViolation);
        var init = new Init<String>(() -> "Initial State");

        var toUpperCase = new NamedAction<String>("toUpperCase", s -> s); // action is not behaving as expected
        var toLowerCase = new NamedAction<String>("toLowerCase", String::toLowerCase);

        var step = new Step<>(toUpperCase, toLowerCase);

        var cannotMixUpperCaseAndLowerCase = new StateProperty<String>("cannotMixUpperCaseAndLowerCase",
                s -> s.chars().anyMatch(Character::isUpperCase) && s.chars().anyMatch(Character::isLowerCase));

        var specification = new Specification<>(init, step)
                .withStateProperty(cannotMixUpperCaseAndLowerCase);

        var report = runner.apply(specification);

        var outcomeSimulation = report.outcomeSimulations().getFirst();
        assertEquals(OutcomeSimulationStatus.FAILED_STATE_PROPERTIES, outcomeSimulation.status());
        assertEquals("cannotMixUpperCaseAndLowerCase", outcomeSimulation.failedPostConditions().getFirst());
    }

    @Test
    void failEarlyOnTemporalProperties() {
        var runner = new DefaultRunner<String>(simulationOptionsFailEarlyOnConstraintsViolation);
        var init = new Init<>(() -> "Initial State");

        var toUpperCase = new NamedAction<String>("toUpperCase", ignored -> ignored);
        var toLowerCase = new NamedAction<String>("toLowerCase", String::toLowerCase);

        var step = new Step<>(toUpperCase, toLowerCase);

        var stringMustBeEventuallyUpperCase = TemporalProperties.<String>eventually("stringMustBeEventuallyUpperCase",
                transition -> transition.to().toUpperCase().equals(transition.to()));

        var specification = new Specification<>(init, step)
                .withTemporalProperties(stringMustBeEventuallyUpperCase);

        var report = runner.apply(specification);

        var outcomeSimulation = report.outcomeSimulations().getFirst();
        assertEquals(OutcomeSimulationStatus.FAILED_TEMPORAL_PROPERTIES, outcomeSimulation.status());
        assertEquals("stringMustBeEventuallyUpperCase", outcomeSimulation.failedTemporalProperties().getFirst());
    }

    @Test
    void noProperties() {
        var runner = new DefaultRunner<String>(simulationOptionsFailAtTheEnd);
        var init = new Init<>(() -> "Initial State");

        var toUpperCase = new NamedAction<String>("toUpperCase", String::toUpperCase); // action is not behaving as expected
        var toLowerCase = new NamedAction<String>("toLowerCase", String::toLowerCase);

        var step = new Step<>(toUpperCase, toLowerCase);

        var specification = new Specification<>(init, step);

        var report = runner.apply(specification);

        var outcomeSimulation = report.outcomeSimulations().getFirst();
        assertEquals(OutcomeSimulationStatus.SUCCESS, outcomeSimulation.status());
    }

}