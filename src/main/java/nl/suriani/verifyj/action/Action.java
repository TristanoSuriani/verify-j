package nl.suriani.verifyj.action;

import java.util.function.UnaryOperator;

public interface Action<C> {
    C execute(C context);

    static<C> Action<C> define(UnaryOperator<C> init) {
        return init::apply;
    }

    static<C> NamedAction<C> define(String name, UnaryOperator<C> init) {
        var action = define(init::apply);
        return new NamedAction<>(name, action);
    }

    record NamedAction<C>(
            String name,
            Action<C> action
    ) implements Action<C> {
        public NamedAction {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be null or blank");
            }
        }

        @Override
        public C execute(C context) {
            return action.execute(context);
        }
    }
}
