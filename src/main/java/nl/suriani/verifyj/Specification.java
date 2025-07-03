package nl.suriani.verifyj;

import nl.suriani.verifyj.action.Action;
import nl.suriani.verifyj.action.Any;
import nl.suriani.verifyj.invariant.Invariant;

import java.util.List;

public record Specification<C>(Action<C> init, Any<C> step, SimulationOptions options, Invariant<C>... invariants) {

    public Specification(Action<C> init, Any<C> step, SimulationOptions options, List<Invariant<C>> invariants) {
        this(
                init,
                step,
                options,
                invariants.toArray(new Invariant[0])
        );
    }

    public Outcome run() {
        var runtime = new  DefaultRuntime<C>(this, options);
        var outcome = runtime.run();
        return outcome;
    }
}
