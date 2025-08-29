package nl.suriani.verifyj;

/**
 * Represents an execution model that can run a specification and produce a result.
 *
 * @param <M> the model type
 * @param <R> the result type
 */
@FunctionalInterface
public interface ExecutionModel<M, R> {
    /**
     * Runs the given specification and returns a result.
     *
     * @param specification the specification to run
     * @return the result of running the specification
     */
    R run(Specification<M> specification);
}
