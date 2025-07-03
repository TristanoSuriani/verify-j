package nl.suriani.verifyj;

public record SimulationOptions(int maxAttempts, int numberOfSteps) {
    public SimulationOptions {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        if (numberOfSteps < 1) {
            throw new IllegalArgumentException("numberOfSteps must be at least 1");
        }
    }
}
