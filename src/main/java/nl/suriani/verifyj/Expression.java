package nl.suriani.verifyj;

/**
 * Represents a function from T1 to T2, used for model expressions.
 *
 * @param <T1> the input type
 * @param <T2> the output type
 */
public interface Expression<T1, T2> extends java.util.function.Function<T1, T2> {
}
