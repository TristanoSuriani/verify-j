package nl.suriani.verifyj;

public interface SimulationRuntime<C> {
    SimulationOptions options();
    Specification<C> specification();
    Outcome run();
}
