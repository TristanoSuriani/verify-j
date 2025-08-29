package nl.suriani.verifyj;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a report containing the results of one or more outcome simulations.
 *
 * @param <M> the model type
 */
public record Report<M>(List<OutcomeSimulation<M>> outcomeSimulations) {
    /**
     * Constructs a Report with the given outcome simulations.
     *
     * @param outcomeSimulations the list of outcome simulations
     */
    public Report {
        Objects.requireNonNull(outcomeSimulations);
    }

    /**
     * Returns a string representation of the report, including all simulations and their results.
     *
     * @return the string representation of the report
     */
    @Override
    public String toString() {
        var builder = new StringBuilder();
        var simulationCount = new AtomicInteger(1);

        for (OutcomeSimulation<M> outcomeSimulation : outcomeSimulations) {
            int simId = simulationCount.get();

            for (Transition<M> transition : outcomeSimulation.transitions()) {
                builder.append(String.format(
                        "[%d] (%d/%d - %s) -> %s\n",
                        simId,
                        transition.transitionNumber(),
                        transition.attemptNumber(),
                        transition.actionName(),
                        transition.to()
                ));
            }

            builder.append(String.format(
                    "\nSimulation %d: %s\n",
                    simId,
                    outcomeSimulation.status().value()
            ));

            if (!outcomeSimulation.failedStateProperties().isEmpty()) {
                builder.append("\nFailed state properties:");
                for (var p : outcomeSimulation.failedStateProperties()) {
                    builder.append("\n\t - ").append(p);
                }
                builder.append("\n");
            }

            if (!outcomeSimulation.failedTemporalProperties().isEmpty()) {
                builder.append("\nFailed temporal properties:");
                for (var p : outcomeSimulation.failedTemporalProperties()) {
                    builder.append("\n\t - ").append(p);
                }
                builder.append("\n");
            }

            builder.append("\n")
                    .append("-".repeat(140))
                    .append("\n\n");

            simulationCount.incrementAndGet();
        }

        return builder.toString();
    }
}