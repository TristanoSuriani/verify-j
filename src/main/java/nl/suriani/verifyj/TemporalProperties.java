package nl.suriani.verifyj;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public interface TemporalProperties {
    static <M> TemporalProperty<M> always(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList = list -> list.stream()
                .map(Transition::to)
                .allMatch(predicate);
        return new TemporalProperty<>(name, predicateOnList);
    }

    static <M> TemporalProperty<M> never(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList = list -> list.stream()
                .map(Transition::to)
                .noneMatch(predicate);
        return new TemporalProperty<>(name, predicateOnList);
    }

    static <M> TemporalProperty<M> eventually(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList = list -> list.stream()
                .map(Transition::to)
                .anyMatch(predicate);
        return new TemporalProperty<>(name, predicateOnList);
    }

    static <M> TemporalProperty<M> initially(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList =
                list -> !list.isEmpty() && predicate.test(list.getFirst().to());

        return new TemporalProperty<>(name, predicateOnList);
    }

    static <M> TemporalProperty<M> atLast(String name, Predicate<M> predicate) {
        Predicate<List<Transition<M>>> predicateOnList =
                list -> !list.isEmpty() && predicate.test(list.getLast().to());

        return new TemporalProperty<>(name, predicateOnList);
    }

    static <M> TemporalProperty<M> exactlyOnce(String name, Predicate<M> predicate) {
        return new TemporalProperty<>(name, list -> list.stream()
                .map(Transition::to)
                .filter(predicate).count() == 1);
    }

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

    static <M> TemporalProperty<M> whenXThenAlwaysImmediatelyY(String name, Predicate<Transition<M>> xPredicate,
                                                               Predicate<Transition<M>> yPredicate) {

        return new TemporalProperty<>(name, list ->
                IntStream.range(0, list.size() - 1)
                        .filter(i -> xPredicate.test(list.get(i)))
                        .allMatch(i -> yPredicate.test(list.get(i + 1)))
        );
    }

    static <M> TemporalProperty<M> xAndThenAlwaysImmediatelyY(String name, Predicate<Transition<M>> xPredicate,
                                                               Predicate<Transition<M>> yPredicate) {

        return new TemporalProperty<>(name, list -> {
                var transitionWhereXIsTrue = IntStream.range(0, list.size() - 1)
                        .filter(i -> xPredicate.test(list.get(i)))
                        .toArray();

                if (transitionWhereXIsTrue.length == 0) {
                    return false;
                }

                return Arrays.stream(transitionWhereXIsTrue)
                        .mapToObj(i -> list.get(i + 1))
                        .allMatch(yPredicate);
            });
    }
}
