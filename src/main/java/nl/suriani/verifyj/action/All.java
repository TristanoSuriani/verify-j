package nl.suriani.verifyj.action;

public record All<C>(Action<C>... actions) implements Action<C> {
    @Override
    public C execute(C c) {
        for (Action<C> action : actions) {
            c = action.execute(c);
        }
        return c;
    }
}
