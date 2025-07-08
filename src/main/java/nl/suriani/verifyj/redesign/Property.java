package nl.suriani.verifyj.redesign;

import java.util.function.Predicate;

public sealed interface Property permits StateProperty, TemporalProperty {
}
