package nl.suriani.verifyj;

import java.util.function.Predicate;

public interface Guard<M> extends Predicate<M> {
    String name();
}
