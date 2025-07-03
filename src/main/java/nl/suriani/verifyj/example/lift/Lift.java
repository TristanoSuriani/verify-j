package nl.suriani.verifyj.example.lift;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record Lift(int currentFloor,
                   int minFloor,
                   int maxFloor,
                   Set<Integer> authorisedFloors) {

    public Lift {
        if (currentFloor < minFloor || currentFloor > maxFloor) {
            throw new IllegalArgumentException("Floor must be between minFloor and maxFloor");
        }

        if (maxFloor < minFloor) {
            throw new IllegalArgumentException("minFloor cannot be above maxFloor");
        }

        if (authorisedFloors.contains(currentFloor)) {
            throw new IllegalArgumentException("Current floor cannot be in the list of authorised floors");
        }

        if (authorisedFloors.stream().anyMatch(floor -> floor < minFloor || floor > maxFloor)) {
            throw new IllegalArgumentException("Authorised floors must be between minFloor and maxFloor");
        }
    }

    public Lift reachFloor(int floor) {
        if (!authorisedFloors.contains(floor)) {
            throw new IllegalArgumentException("Floor " + floor + " is not authorised");
        }

        var authorisedFloors = this.authorisedFloors.stream()
                .filter(authorisedFloor -> authorisedFloor != floor)
                .collect(Collectors.toSet());

        return new Lift(floor, minFloor, maxFloor, authorisedFloors);
    }

    public Lift authoriseFloor(int targetFloor) {
        var authorisedFloors = new HashSet<>(this.authorisedFloors);
        authorisedFloors.add(targetFloor);
        return new Lift(currentFloor, minFloor, maxFloor, authorisedFloors);
    }

    public enum LiftStatus {
        IDLE, GOING_UP, GOING_DOWN
    }
}
