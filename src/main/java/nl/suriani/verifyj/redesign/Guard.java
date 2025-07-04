package nl.suriani.verifyj.redesign;

import java.util.function.Predicate;

public interface Guard<M> extends Predicate<M> {
    String name();
}
