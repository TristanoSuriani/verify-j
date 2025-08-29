package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a named action with optional guards and an expression to apply to a model.
 *
 * @param <M> the model type
 */
public record NamedAction<M>(String name, Expression<M, M> expression, List<Expression<M, Boolean>> guards) implements Action<M> {
    /**
     * Constructs a NamedAction with the given name, expression, and guards.
     *
     * @param name the name of the action
     * @param expression the expression to apply
     * @param guards the guard conditions
     */
    public NamedAction(String name, Expression<M, M> expression, List<Expression<M, Boolean>> guards) {
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(expression, "expression is null");
        Objects.requireNonNull(guards, "guards is null");

        this.name = name;
        this.expression = expression;
        this.guards = List.copyOf(guards);
    }

    /**
     * Constructs a NamedAction with the given name, expression, and varargs guards.
     *
     * @param name the name of the action
     * @param expression the expression to apply
     * @param guards the guard conditions
     */
    @SafeVarargs
    public NamedAction(String name, Expression<M, M> expression, Expression<M, Boolean>... guards) {
        this(name, expression, Arrays.asList(guards));
    }

    /**
     * Constructs a NamedAction with the given name and expression, with no guards.
     *
     * @param name the name of the action
     * @param expression the expression to apply
     */
    public NamedAction(String name, Expression<M, M> expression) {
        this(name, expression, List.of());
    }

    /**
     * Applies the action to the given model if all guards pass.
     *
     * @param model the model to apply the action to
     * @return the new model after applying the action
     * @throws IllegalStateException if any guard fails
     */
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
