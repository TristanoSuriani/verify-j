package nl.suriani.verifyj.action;

public record Any<C> (Action<C>... actions) implements Action<C> {
    @Override
    public C execute(C c) {
        var length = actions.length;
        var index = (int) (Math.random() * length);
        var action = actions[index];
        return action.execute(c);
    }
}
