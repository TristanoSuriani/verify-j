package nl.suriani.verifyj;

import java.util.HashSet;
import java.util.function.Function;

/**
 * Generates a directed graph (digraph) representation of the state space from a simulation report.
 *
 * @param <M> the model type
 */
public class StateSpaceDigraphGenerator<M> {
    private final Function<M, String> describe;

    /**
     * Constructs a StateSpaceDigraphGenerator with a function to describe model states as strings.
     *
     * @param describe function to convert a model state to a string
     */
    public StateSpaceDigraphGenerator(Function<M, String> describe) {
        this.describe = describe;
    }

    /**
     * Generates a PlantUML digraph representation of the state space from the given report.
     *
     * @param report the simulation report
     * @return a PlantUML-formatted string representing the state space digraph
     */
    public String run(Report<M> report) {
        var header = "@startuml\ndigraph StateSpace {\n";
        var builder = new StringBuilder(header);
        var transitions = new HashSet<FromTo<M>>();

        for (var outcome: report.outcomeSimulations()) {
            for (var transition : outcome.transitions()) {
                var from = describe.apply(transition.from());
                var to = describe.apply(transition.to());

                if (from.equals(to)) {
                    continue; // Skip self-loops
                }

                if (transitions.add(new FromTo<>(from, to))) {
                    builder.append("\t").append(from).append(" -> ").append(to).append("\n");
                }
            }
        }

        var footer = "}\n@enduml";
        builder.append(footer);
        return builder.toString();
    }

    private record FromTo<M>(String from, String to) { }
}
