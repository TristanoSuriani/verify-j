package nl.suriani.verifyj.example.lift;

import nl.suriani.verifyj.Specification;
import nl.suriani.verifyj.SimulationOptions;
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
                new Lift(NonDet.withinRange(-4, 10),
                        minFloor,
                        maxFloor,
                        NonDet.oneOf(Set.of(), Set.of(1, 2, 3), new HashSet<>(List.of(NonDet.withinRange(-4, 10))))
                )
        );

        var reachFloor = Action.<Lift>define(
                "reachFloor",
                lift -> lift.reachFloor(NonDet.withinRange(-4, 10))
        );

        var authoriseFloor = Action.<Lift>define(
                "authoriseFloor",
                lift -> lift.authoriseFloor(NonDet.withinRange(-4, 10))
        );

        var step = new Any<>(reachFloor, authoriseFloor);

        var minFloorWillAlwaysBeLessThanOrEqualToMaxFloor = Invariants.<Lift>always(
                "Min floor will always be less than or equal to max floor",
                lift -> lift.minFloor() <= lift.maxFloor());

        var currentFloorIsAlwaysBetweenMinFloorAndMaxFloor = Invariants.<Lift>always(
                "Current floor will always be between min floor and max floor",
                lift -> lift.minFloor() <= lift.currentFloor() &&
                        lift.maxFloor() >= lift.currentFloor());

        var liftWillEventuallyReachAnyFloor = Stream.iterate(minFloor, i -> i + 1)
                .limit(maxFloor)
                .map(floor -> Invariants.<Lift>eventually(
                        "Lift will eventually reach floor " + floor,
                        lift -> lift.currentFloor() == floor))
                .toList();

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

        var invariants = new ArrayList<>(
                List.of(
                        minFloorWillAlwaysBeLessThanOrEqualToMaxFloor,
                        currentFloorIsAlwaysBetweenMinFloorAndMaxFloor,
                        theCurrentFloorCannotBeInTheListOfAuthorisedFloors,
                        thereWillBeEventuallyNoAuthorisedFloors,
                        authorisedFloorsMustAlwaysBeBetweenMinFloorAndMaxFloor
                )
        );

        invariants.addAll(liftWillEventuallyReachAnyFloor);

        var specification = new Specification<>(
                init,
                step,
                new SimulationOptions(6000, 2000),
                invariants
        );

        specification.run();
    }
}
