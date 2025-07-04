package nl.suriani.verifyj.redesign;

import java.util.function.Predicate;

public record Postcondition<M>(String name, Predicate<M> test) implements Predicate<M> {
    @Override
    public boolean test(M m) {
        return test.test(m);
    }
}
