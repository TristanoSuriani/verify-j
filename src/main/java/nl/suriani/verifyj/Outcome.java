package nl.suriani.verifyj;

public sealed interface Outcome {
    record Failure(String message) implements Outcome {
    }

    record Success() implements Outcome {

    }
}
