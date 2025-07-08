package nl.suriani.verifyj;

@FunctionalInterface
public interface Runner<M> {
    Report<M> run(Specification<M> specification);
}
