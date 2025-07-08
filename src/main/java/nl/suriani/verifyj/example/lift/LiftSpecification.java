package nl.suriani.verifyj.example.lift;

import nl.suriani.verifyj.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class LiftSpecification {
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
                transition -> transition.to().minFloor() <= transition.to().maxFloor());

        var currentFloorIsAlwaysBetweenMinFloorAndMaxFloor = TemporalProperties.<Lift>always(
                "Current floor will always be between min floor and max floor",
                transition -> transition.to().minFloor() <= transition.to().currentFloor() &&
                        transition.to().maxFloor() >= transition.to().currentFloor());

        var liftWillEventuallyReachAnyFloor = Stream.iterate(minFloor, i -> i + 1)
                .limit(maxFloor)
                .map(floor -> TemporalProperties.<Lift>eventually(
                        "Lift will eventually reach floor " + floor,
                        transition -> transition.to().currentFloor() == floor))
                .toList();

        var theCurrentFloorCannotBeInTheListOfAuthorisedFloors = TemporalProperties.<Lift>always(
                "The current floor cannot be in the list of authorised floors",
                transition -> !transition.to().authorisedFloors().contains(transition.to().currentFloor()));

        var thereWillBeEventuallyNoAuthorisedFloors = TemporalProperties.<Lift>eventually(
                "There will be eventually no authorised floors",
                transition -> transition.to().authorisedFloors().isEmpty());

        var authorisedFloorsMustAlwaysBeBetweenMinFloorAndMaxFloor = TemporalProperties.<Lift>always(
                "Authorised floors must always be between min floor and max floor",
                transition -> transition.to().authorisedFloors().stream()
                        .allMatch(floor -> transition.to().minFloor() <= floor && transition.to().maxFloor() >= floor));

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

        var runner = new DefaultRunner<Lift>(new SimulationOptions(1,
                6000,
                2000,
                true));

        var report = runner.run(specification.withTemporalProperties(temporalProperties));
        System.out.println(report);
    }
}
