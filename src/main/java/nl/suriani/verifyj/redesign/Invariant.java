package nl.suriani.verifyj.redesign;

import java.util.List;
import java.util.function.Predicate;

public record Invariant<M>(String name, Predicate<List<Transition<M>>> predicate) implements Predicate<List<Transition<M>>> {
    @Override
    public boolean test(List<Transition<M>> transitions) {
        return predicate.test(transitions);
    }
}
