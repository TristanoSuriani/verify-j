package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record NamedAction<M>(String name, Expression<M, M> expression, List<Expression<M, Boolean>> guards) implements Action<M> {
    public NamedAction(String name, Expression<M, M> expression, List<Expression<M, Boolean>> guards) {
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(expression, "expression is null");
        Objects.requireNonNull(guards, "guards is null");

        this.name = name;
        this.expression = expression;
        this.guards = List.copyOf(guards);
    }

    @SafeVarargs
    public NamedAction(String name, Expression<M, M> expression, Expression<M, Boolean>... guards) {
        this(name, expression, Arrays.asList(guards));
    }

    public NamedAction(String name, Expression<M, M> expression) {
        this(name, expression, List.of());
    }

    @Override
    public M apply(M model) {
        return guards.stream()
                .allMatch(guard -> guard.apply(model))
                ?   expression.apply(model)
                :   failGuards(model);
    }

    private M failGuards(M model) {
        throw new IllegalStateException();
    }
}
