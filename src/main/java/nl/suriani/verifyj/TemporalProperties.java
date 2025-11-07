package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Utility interface providing static factory methods for creating various temporal properties
 * over sequences of transitions. These properties can be used to specify and check temporal
 * constraints on models.
 */
public interface TemporalProperties {
    /**
     * Creates a temporal property that requires the predicate to hold for all elements in the sequence.
     *
     * @param name the name of the property
     * @param predicate the predicate to check on each element
     * @return a temporal property enforcing the predicate always holds
     */
    static <M> TemporalProperty<M> always(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList = list -> list.stream()
                .map(Transition::to)
                .allMatch(predicate);
        return new TemporalProperty<>(name, predicateOnList);
    }

    /**
     * Creates a temporal property that requires the predicate to never hold for any element in the sequence.
     *
     * @param name the name of the property
     * @param predicate the predicate to check on each element
     * @return a temporal property enforcing the predicate never holds
     */
    static <M> TemporalProperty<M> never(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList = list -> list.stream()
                .map(Transition::to)
                .noneMatch(predicate);
        return new TemporalProperty<>(name, predicateOnList);
    }

    /**
     * Creates a temporal property that requires the predicate to eventually hold for at least one element in the sequence.
     *
     * @param name the name of the property
     * @param predicate the predicate to check on each element
     * @return a temporal property enforcing the predicate eventually holds
     */
    static <M> TemporalProperty<M> eventually(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList = list -> list.stream()
                .map(Transition::to)
                .anyMatch(predicate);
        return new TemporalProperty<>(name, predicateOnList);
    }

    /**
     * Creates a temporal property that requires the predicate to hold for the first element in the sequence.
     *
     * @param name the name of the property
     * @param predicate the predicate to check on the first element
     * @return a temporal property enforcing the predicate holds initially
     */
    static <M> TemporalProperty<M> initially(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList =
                list -> !list.isEmpty() && predicate.test(list.getFirst().to());

        return new TemporalProperty<>(name, predicateOnList);
    }

    /**
     * Creates a temporal property that requires the predicate to hold for the last element in the sequence.
     *
     * @param name the name of the property
     * @param predicate the predicate to check on the last element
     * @return a temporal property enforcing the predicate holds at last
     */
    static <M> TemporalProperty<M> atLast(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList =
                list -> !list.isEmpty() && predicate.test(list.getLast().to());

        return new TemporalProperty<>(name, predicateOnList);
    }

    /**
     * Creates a temporal property that requires the predicate to hold exactly once in the sequence.
     *
     * @param name the name of the property
     * @param predicate the predicate to check on each element
     * @return a temporal property enforcing the predicate holds exactly once
     */
    static <M> TemporalProperty<M> exactlyOnce(String name, Predicate<M> predicate) {
        return new TemporalProperty<>(name, list -> list.stream()
                .map(Transition::to)
                .filter(predicate).count() == 1);
    }

    /**
     * Creates a temporal property that requires: after xPredicate holds, yPredicate must eventually hold.
     *
     * @param name the name of the property
     * @param xPredicate the predicate that triggers the requirement
     * @param yPredicate the predicate that must eventually hold after xPredicate
     * @return a temporal property enforcing the described behavior
     */
    static <M> TemporalProperty<M> xAndThenThenEventuallyY(String name, Predicate<M> xPredicate,
                                               Predicate<M> yPredicate) {

        return new TemporalProperty<>(name, list -> {
            boolean foundX = false;
            for (Transition<M> transition : list) {
                foundX = foundX || xPredicate.test(transition.to());
                if (foundX && yPredicate.test(transition.to())) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Creates a temporal property that requires: whenever xPredicate holds for a transition, yPredicate must hold for the immediately following transition.
     *
     * @param name the name of the property
     * @param xPredicate the predicate to check on the current transition
     * @param yPredicate the predicate to check on the next transition
     * @return a temporal property enforcing the described behavior
     */
    static <M> TemporalProperty<M> whenXThenAlwaysImmediatelyY(String name, Predicate<Transition<M>> xPredicate,
                                                               Predicate<Transition<M>> yPredicate) {

        return new TemporalProperty<>(name, list ->
                IntStream.range(0, list.size() - 1)
                        .filter(i -> xPredicate.test(list.get(i)))
                        .allMatch(i -> yPredicate.test(list.get(i + 1)))
        );
    }

    /**
     * Creates a temporal property that requires: after xPredicate holds for a transition, yPredicate must hold for all immediately following transitions where xPredicate was true.
     *
     * @param name the name of the property
     * @param xPredicate the predicate to check on the current transition
     * @param yPredicate the predicate to check on the next transition
     * @return a temporal property enforcing the described behavior
     */
    static <M> TemporalProperty<M> xMustOccurAndBeImmediatelyFollowedByY(String name, Predicate<Transition<M>> xPredicate,
                                                                         Predicate<Transition<M>> yPredicate) {

        return new TemporalProperty<>(name, list -> {
            var indicesOfX = IntStream.range(0, list.size())
                    .filter(i -> xPredicate.test(list.get(i)))
                    .toArray();

            if (indicesOfX.length == 0) {
                return false;
            }

            return Arrays.stream(indicesOfX)
                    .allMatch(i -> (i < list.size() - 1) && yPredicate.test(list.get(i + 1)));
            });
    }

    /**
     * Creates a temporal property that requires: after xPredicate holds for a transition, yPredicate must hold for all immediately following transitions where xPredicate was true.
     *
     * @param name the name of the property
     * @param xPredicate the predicate to check on the current transition
     * @param yPredicate the predicate to check on the next transition
     * @return a temporal property enforcing the described behavior
     */
    static <M> TemporalProperty<M> whenXOccursThenAllTheFollowingXMustSatisfyY(String name, Predicate<Transition<M>> xPredicate,
                                                                         Predicate<Transition<M>> yPredicate) {

        return new TemporalProperty<>(name, list -> {
            var indicesOfX = IntStream.range(0, list.size())
                    .filter(i -> xPredicate.test(list.get(i)))
                    .toArray();

            if (indicesOfX.length == 0) {
                return false;
            }

            return Arrays.stream(indicesOfX)
                    .allMatch(i -> (i < list.size() - 1) && yPredicate.test(list.get(i + 1)));
        });
    }
}
