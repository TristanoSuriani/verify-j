package nl.suriani.verifyj;

public record SpecificationOptions(int maxAttempts, int numberOfSteps) {
    public SpecificationOptions {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        if (numberOfSteps < 1) {
            throw new IllegalArgumentException("numberOfSteps must be at least 1");
        }
    }
}
