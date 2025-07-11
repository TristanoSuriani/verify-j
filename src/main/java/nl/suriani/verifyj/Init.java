package nl.suriani.verifyj;

import java.util.function.Supplier;

public record Init<M>(Supplier<M> supplier) implements Action<M> {
    @Override
    public M apply(M m) {
        return supplier().get();
    }
}
