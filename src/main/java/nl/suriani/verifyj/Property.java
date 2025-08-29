package nl.suriani.verifyj;

/**
 * Marker interface for properties (state or temporal) used in specifications.
 */
public sealed interface Property permits StateProperty, TemporalProperty {
}
