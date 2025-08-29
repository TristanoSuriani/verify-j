package nl.suriani.verifyj;

/**
 * Represents a transformation from one model type to another, extending Expression.
 *
 * @param <M1> the input model type
 * @param <M2> the output model type
 */
public interface Transformation<M1, M2> extends Expression<M1, M2> {
}
