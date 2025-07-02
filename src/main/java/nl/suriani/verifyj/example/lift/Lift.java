package nl.suriani.verifyj.example.lift;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record Lift(int currentFloor,
                   int targetFloor,
                   int minFloor,
                   int maxFloor,
                   Set<Integer> authorisedFloors,
                   LiftStatus status) {

    public Lift {
        if (currentFloor < minFloor || currentFloor > maxFloor) {
            throw new IllegalArgumentException("Floor must be between minFloor and maxFloor");
        }

        if (targetFloor < minFloor || targetFloor > maxFloor) {
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

        switch (status) {
            case IDLE -> {
                if (currentFloor != targetFloor) {
                    throw new IllegalArgumentException("Lift is idle but current floor does not match target floor");
                }
            }
            case GOING_UP -> {
                if (currentFloor >= targetFloor) {
                    throw new IllegalArgumentException("Lift is going up but current floor is not below target floor");
                }
            }
            case GOING_DOWN -> {
                if (currentFloor <= targetFloor) {
                    throw new IllegalArgumentException("Lift is going down but current floor is not above target floor");
                }
            }
        }
    }
    
    public static Lift init(int currentFloor,
                     int targetFloor,
                     int minFloor,
                     int maxFloor,
                     Set<Integer> authorisedFloors) {
        
        var status = updateStatus(authorisedFloors, currentFloor, targetFloor);
        
        return new Lift(currentFloor, targetFloor, minFloor, maxFloor, authorisedFloors, status);
    }

    public Lift reachFloor(int floor) {
        if (!authorisedFloors.contains(floor)) {
            throw new IllegalArgumentException("Floor " + floor + " is not authorised");
        }
        return init(floor, targetFloor, minFloor, maxFloor, authorisedFloors.stream()
                .filter(f -> f != floor)
                .collect(Collectors.toSet()));
    }

    private static LiftStatus updateStatus(Set<Integer> authorisedFloors, int floor, int targetFloor) {
        if (authorisedFloors.isEmpty()) {
             return  LiftStatus.IDLE;
        }

        if (floor < targetFloor) {
            return  LiftStatus.GOING_UP;
        }

        return LiftStatus.GOING_DOWN;

    }

    public Lift authoriseFloor(int targetFloor) {
        var authorisedFloors = new HashSet<>(this.authorisedFloors);
        authorisedFloors.add(targetFloor);
        return new Lift(currentFloor, targetFloor, minFloor, maxFloor, authorisedFloors, status);
    }

    public enum LiftStatus {
        IDLE, GOING_UP, GOING_DOWN
    }
}
