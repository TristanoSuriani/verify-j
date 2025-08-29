package nl.suriani.verifyj.example.lift;

import nl.suriani.verifyj.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class LiftSpecification {
    public static void main(String[] args) {
        var minFloor = -2;
        var maxFloor = 8;

        var init = new Init<>(() ->
                new Lift(NonDet.withinRange(-4, 10),
                        minFloor,
                        maxFloor,
                        NonDet.oneOf(Set.of(), Set.of(1, 2, 3), new HashSet<>(List.of(NonDet.withinRange(-4, 10))))
                )
        );

        var reachFloor = new NamedAction<Lift>(
                "reachFloor",
                lift -> lift.reachFloor(NonDet.withinRange(-4, 10))
        );

        var authoriseFloor = new NamedAction<Lift>(
                "authoriseFloor",
                lift -> lift.authoriseFloor(NonDet.withinRange(-4, 10))
        );

        var step = new Step<Lift>(reachFloor, authoriseFloor);

        var minFloorWillAlwaysBeLessThanOrEqualToMaxFloor = TemporalProperties.<Lift>always(
                "Min floor will always be less than or equal to max floor",
                transition -> transition.minFloor() <= transition.maxFloor());

        var currentFloorIsAlwaysBetweenMinFloorAndMaxFloor = TemporalProperties.<Lift>always(
                "Current floor will always be between min floor and max floor",
                transition -> transition.minFloor() <= transition.currentFloor() &&
                        transition.maxFloor() >= transition.currentFloor());

        var liftWillEventuallyReachAnyFloor = Stream.iterate(minFloor, i -> i + 1)
                .limit(maxFloor)
                .map(floor -> TemporalProperties.<Lift>eventually(
                        "Lift will eventually reach floor " + floor,
                        transition -> transition.currentFloor() == floor))
                .toList();

        var theCurrentFloorCannotBeInTheListOfAuthorisedFloors = TemporalProperties.<Lift>always(
                "The current floor cannot be in the list of authorised floors",
                transition -> !transition.authorisedFloors().contains(transition.currentFloor()));

        var thereWillBeEventuallyNoAuthorisedFloors = TemporalProperties.<Lift>eventually(
                "There will be eventually no authorised floors",
                transition -> transition.authorisedFloors().isEmpty());

        var authorisedFloorsMustAlwaysBeBetweenMinFloorAndMaxFloor = TemporalProperties.<Lift>always(
                "Authorised floors must always be between min floor and max floor",
                transition -> transition.authorisedFloors().stream()
                        .allMatch(floor -> transition.minFloor() <= floor && transition.maxFloor() >= floor));

        var temporalProperties = new ArrayList<>(
                List.of(
                        minFloorWillAlwaysBeLessThanOrEqualToMaxFloor,
                        currentFloorIsAlwaysBetweenMinFloorAndMaxFloor,
                        theCurrentFloorCannotBeInTheListOfAuthorisedFloors,
                        thereWillBeEventuallyNoAuthorisedFloors,
                        authorisedFloorsMustAlwaysBeBetweenMinFloorAndMaxFloor
                )
        );

        temporalProperties.addAll(liftWillEventuallyReachAnyFloor);

        var specification = new Specification<>(
                init,
                step
        );

        var runner = new Simulator<Lift>(new SimulationOptions(1,
                6000,
                2000,
                true));

        var report = runner.run(specification.withTemporalProperties(temporalProperties));
        System.out.println(report);

        System.out.println(new StateSpaceDigraphGenerator<Lift>(
                lift -> "Floor_" + lift.currentFloor()
        ).run(report));
    }
}
