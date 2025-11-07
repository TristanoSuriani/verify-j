package nl.suriani.verifyj.example.assignment;

import java.util.UUID;

public record AssignmentKey(UUID assignmentId, Status status) {

    public AssignmentKey revoke() {
        return new AssignmentKey(assignmentId, Status.REVOKED);
    }

    public enum Status {
        CURRENT, REVOKED
    }
}
