package nl.suriani.verifyj.redesign;

public record SimulationOptions(int numberOfSimulations, int maxAttempts, int maxTransitions, boolean stopOnConstraintViolation) {

    public static final SimulationOptions DEFAULT = new SimulationOptions(1, 2000, 500, true);

    public SimulationOptions {
        if (numberOfSimulations < 1) {
            throw new IllegalArgumentException("numberOfSimulations must be at least 1");
        }
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        if (maxTransitions < 1) {
            throw new IllegalArgumentException("maxTransitions must be at least 1");
        }
    }
}
