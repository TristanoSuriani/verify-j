package nl.suriani.verifyj;

import java.util.function.Supplier;

/**
 * Represents an initialization action for a model, using a supplier.
 *
 * @param <M> the model type
 */
public record Init<M>(Supplier<M> supplier) implements Action<M> {
    /**
     * Applies the initialization action, returning a new model instance.
     *
     * @param m ignored (not used)
     * @return a new model instance
     */
    @Override
    public M apply(M m) {
        return supplier().get();
    }
}
