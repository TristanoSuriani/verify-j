package nl.suriani.verifyj;

public record NamedAction<M>(String name, Expression<M, M> expression) implements Action<M> {
    @Override
    public M apply(M model) {
        return expression.apply(model);
    }
}
