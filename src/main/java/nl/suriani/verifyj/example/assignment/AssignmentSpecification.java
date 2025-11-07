package nl.suriani.verifyj.example.assignment;

import nl.suriani.verifyj.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AssignmentSpecification {
    public static void main(String[] args) {
        Supplier<UUID> givenId = UUID::randomUUID;
        Supplier<String> givenBusinessKey = () -> givenId.get().toString();
        Supplier<String> givenContent = () -> NonDet.oneOf("first",  "second");
        Supplier<Assignment.Status> givenAssignmentStatus = () -> NonDet.oneOf(Assignment.Status.STARTED, Assignment.Status.STOPPED, Assignment.Status.REMOVED);
        Supplier<AssignmentKey.Status> givenKeyStatus = () -> NonDet.oneOf(AssignmentKey.Status.CURRENT, AssignmentKey.Status.REVOKED);
        Supplier<AssignmentKey> givenAssignmentKey = () -> NonDet.oneOf(new AssignmentKey(givenId.get(), givenKeyStatus.get()));
        Supplier<Assignment> givenAssignment = () -> new Assignment(givenBusinessKey.get(), givenContent.get(), givenAssignmentStatus.get(),
                NonDet.oneOf(
                        List.of(),
                        List.of(givenAssignmentKey.get()),
                        List.of(givenAssignmentKey.get(), givenAssignmentKey.get())
                ));

        var init = new Init<>(givenAssignment);

        var start = new NamedAction<>("START", Assignment::restart, assignment -> assignment.keys().size() <= 6);
        var modify = new NamedAction<Assignment>("MODIFY", assignment -> assignment.modify(givenContent.get()));
        var stop = new NamedAction<>("STOP", Assignment::stop);

        var step = new Step<>(start, modify, stop);

        var thereShouldNeverBeMoreThanOneCurrentKey = new StateProperty<Assignment>(
                "thereShouldNeverBeMoreThanOneCurrentKey",
                assignment -> assignment.keys().stream().filter(key -> key.status() == AssignmentKey.Status.CURRENT).count() <= 1);

        var thereShouldAlwaysBeAtLeastOneKey = new StateProperty<Assignment>(
                "thereShouldAlwaysBeAtLeastOneKey",
                assignment -> !assignment.keys().isEmpty());

        var options = new SimulationOptions(1, 2000, 500, true, true);
        var specification = new Specification<>(init, step,
                List.of(
                        thereShouldAlwaysBeAtLeastOneKey,
                        thereShouldNeverBeMoreThanOneCurrentKey),
                List.of());
        var simulator = new Simulator<Assignment>(options);
        var result = simulator.run(specification);

        var printToScreen = result.outcomeSimulations()
                .getFirst()
                .transitions()
                .stream()
                .map(t -> {
                    var maxLen = result.outcomeSimulations()
                            .getFirst()
                            .transitions()
                            .stream()
                            .map(x -> x.actionName().length())
                            .max(Integer::compareTo)
                            .orElse(0);
                    var keys = t.to().keys().stream()
                            .map(k -> "(" + k.assignmentId().toString().substring(0, 8) + "):" + k.status())
                            .collect(Collectors.joining(", "));
                    return String.format("%-" + maxLen + "s - \t[%s]", t.actionName(), keys);
                })
                .collect(Collectors.joining("\n"));

        System.out.println(result.outcomeSimulations().getFirst().status());
        System.out.println(printToScreen);
        System.out.println(result.outcomeSimulations().getFirst().failedStateProperties());
    }
}
