package nl.suriani.verifyj;

/**
 * Represents simulation options such as number of simulations, max attempts, max transitions, and constraint violation behavior.
 *
 * @param numberOfSimulations the number of simulations to run
 * @param maxAttempts the maximum number of attempts per simulation
 * @param maxTransitions the maximum number of transitions per simulation
 * @param stopOnConstraintViolation whether to stop on constraint violation
 */
public record SimulationOptions(int numberOfSimulations, int maxAttempts, int maxTransitions, boolean stopOnConstraintViolation) {
    /**
     * The default simulation options.
     */
    public static final SimulationOptions DEFAULT = new SimulationOptions(1, 2000, 500, true);

    /**
     * Constructs a SimulationOptions record with validation.
     *
     * @param numberOfSimulations the number of simulations
     * @param maxAttempts the maximum number of attempts
     * @param maxTransitions the maximum number of transitions
     * @param stopOnConstraintViolation whether to stop on constraint violation
     * @throws IllegalArgumentException if any parameter is less than 1 (except stopOnConstraintViolation)
     */
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
