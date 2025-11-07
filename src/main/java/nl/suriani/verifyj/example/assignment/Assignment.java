package nl.suriani.verifyj.example.assignment;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public record Assignment(String businessKey, String content, Status status, List<AssignmentKey> keys) {

    public Assignment {
        Objects.requireNonNull(businessKey, "businessKey cannot be null");
        Objects.requireNonNull(content, "content cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
        Objects.requireNonNull(keys, "keys cannot be null");

        if (keys.isEmpty()) {
            throw new IllegalArgumentException("keys cannot be empty");
        }

        if (keys.stream().filter(key -> key.status() == AssignmentKey.Status.CURRENT).count() > 1) {
            throw new IllegalStateException("Only one current assignment key is allowed per businessKey");
        }
    }

    public static Assignment start(String businessKey, String content) {
        return new Assignment(
                businessKey,
                content,
                Status.STARTED,
                List.of(
                        new AssignmentKey(UUID.randomUUID(), AssignmentKey.Status.CURRENT)
                )
        );
    }

    public Assignment modify(String content) {
        return new Assignment(
                businessKey,
                content,
                status,
                keys
        );
    }

    public Assignment stop() {
        return new Assignment(
                businessKey,
                content,
                Status.STOPPED,
                Stream.concat(
                        keys.stream().filter(key -> key.status() == AssignmentKey.Status.REVOKED),
                        keys.stream().filter(key -> key.status() == AssignmentKey.Status.CURRENT).map(AssignmentKey::revoke)
                ).toList()
        );
    }

    public Assignment restart() {
        return new Assignment(businessKey,
                content,
                Status.STARTED,
                Stream.concat(
                        keys.stream(),
                        Stream.of(new AssignmentKey(UUID.randomUUID(), AssignmentKey.Status.CURRENT))
                ).toList()
        );
    }

    public enum Status {
        STARTED, STOPPED, REMOVED
    }
}
