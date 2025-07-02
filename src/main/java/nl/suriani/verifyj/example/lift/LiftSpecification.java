package nl.suriani.verifyj.example.lift;

import nl.suriani.verifyj.Specification;
import nl.suriani.verifyj.SpecificationOptions;
import nl.suriani.verifyj.action.Action;
import nl.suriani.verifyj.action.Any;
import nl.suriani.verifyj.action.NonDet;
import nl.suriani.verifyj.invariant.Invariants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class LiftSpecification {
    public static void main(String[] args) {
        var minFloor = -2;
        var maxFloor = 8;

        var init = Action.<Lift>define(ignored ->
                Lift.init(NonDet.withinRange(-4, 10),
                        NonDet.withinRange(-4, 10),
                        minFloor,
                        maxFloor,
                        NonDet.oneOf(Set.of(), Set.of(1, 2, 3), new HashSet<>(List.of(NonDet.withinRange(-4, 10))))));

        var reachFloor = Action.<Lift>define(
                "reachFloor",
                lift -> lift.reachFloor(NonDet.withinRange(-10, 30)));

        var authoriseFloor = Action.<Lift>define(
                "authoriseFloor",
                lift -> lift.authoriseFloor(NonDet.withinRange(-10, 30)));

        var step = new Any<>(reachFloor, authoriseFloor);

        var minFloorWillAlwaysBeLessThanOrEqualToMaxFloor = Invariants.<Lift>always(
                "Min floor will always be less than or equal to max floor",
                lift -> lift.minFloor() <= lift.maxFloor());

        var currentFloorIsAlwaysBetweenMinFloorAndMaxFloor = Invariants.<Lift>always(
                "Current floor will always be between min floor and max floor",
                lift -> lift.minFloor() <= lift.currentFloor() &&
                        lift.maxFloor() >= lift.currentFloor());

        var targetFloorIsAlwaysBetweenMinFloorAndMaxFloor = Invariants.<Lift>always(
                "Target floor will always be between min floor and max floor",
                lift -> lift.minFloor() <= lift.targetFloor() &&
                        lift.maxFloor() >= lift.targetFloor());

        var liftWillEventuallyReachAnyFloor = Stream.iterate(minFloor, i -> i + 1)
                .limit(maxFloor)
                .map(floor -> Invariants.<Lift>eventually(
                        "Lift will eventually reach floor " + floor,
                        lift -> lift.currentFloor() == floor))
                .toList();

        var whenGoingUpThenCurrentFloorWillAlwaysBeLessThanTargetFloor = Invariants.<Lift>always(
                "When going up, current floor will always be less than target floor",
                lift -> switch (lift.status()) {
                    case GOING_UP -> lift.currentFloor() < lift.targetFloor();
                    default -> true;
                });

        var whenGoingDownThenCurrentFloorWillAlwaysBeMoreThanTargetFloor = Invariants.<Lift>always(
                "When going up, current floor will always be less than target floor",
                lift -> switch (lift.status()) {
                    case GOING_DOWN -> lift.currentFloor() > lift.targetFloor();
                    default -> true;
                });

        var whenIdleThenCurrentFloorWillAlwaysTheSameAsTargetFloor = Invariants.<Lift>always(
                "When going up, current floor will always be less than target floor",
                lift -> switch (lift.status()) {
                    case IDLE -> lift.currentFloor() == lift.targetFloor();
                    default -> true;
                });

        var theCurrentFloorCannotBeInTheListOfAuthorisedFloors = Invariants.<Lift>always(
                "The current floor cannot be in the list of authorised floors",
                lift -> !lift.authorisedFloors().contains(lift.currentFloor()));

        var thereWillBeEventuallyNoAuthorisedFloors = Invariants.<Lift>eventually(
                "There will be eventually no authorised floors",
                lift -> lift.authorisedFloors().isEmpty());

        var authorisedFloorsMustAlwaysBeBetweenMinFloorAndMaxFloor = Invariants.<Lift>always(
                "Authorised floors must always be between min floor and max floor",
                lift -> lift.authorisedFloors().stream()
                        .allMatch(floor -> lift.minFloor() <= floor && lift.maxFloor() >= floor));

        var whenThereAreNoAuthorisedFloorsThenLiftWillBeIdle = Invariants.<Lift>always(
                "When there are no authorised floors, then lift will be idle",
                lift -> lift.authorisedFloors().isEmpty() == (lift.status() == Lift.LiftStatus.IDLE));

        var invariants = new ArrayList<>(
                List.of(
                        minFloorWillAlwaysBeLessThanOrEqualToMaxFloor,
                        currentFloorIsAlwaysBetweenMinFloorAndMaxFloor,
                        targetFloorIsAlwaysBetweenMinFloorAndMaxFloor,
                        whenGoingUpThenCurrentFloorWillAlwaysBeLessThanTargetFloor,
                        whenGoingDownThenCurrentFloorWillAlwaysBeMoreThanTargetFloor,
                        whenIdleThenCurrentFloorWillAlwaysTheSameAsTargetFloor,
                        theCurrentFloorCannotBeInTheListOfAuthorisedFloors,
                        thereWillBeEventuallyNoAuthorisedFloors,
                        authorisedFloorsMustAlwaysBeBetweenMinFloorAndMaxFloor,
                        whenThereAreNoAuthorisedFloorsThenLiftWillBeIdle
                )
        );

        invariants.addAll(liftWillEventuallyReachAnyFloor);

        var specification = new Specification<>(
                init,
                step,
                new SpecificationOptions(2000, 300),
                invariants
        );

        specification.run();
    }
}
