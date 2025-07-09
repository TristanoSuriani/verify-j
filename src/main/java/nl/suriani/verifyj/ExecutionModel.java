package nl.suriani.verifyj;

@FunctionalInterface
public interface ExecutionModel<M, R> {
    R run(Specification<M> specification);
}
