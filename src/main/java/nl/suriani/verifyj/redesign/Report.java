package nl.suriani.verifyj.redesign;

import java.util.List;
import java.util.Objects;

public record Report<M>(List<OutcomeSimulation<M>> outcomeSimulations) {
    public Report {
        Objects.requireNonNull(outcomeSimulations);
    }
}
