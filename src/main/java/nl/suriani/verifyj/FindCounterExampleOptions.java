package nl.suriani.verifyj;

public record FindCounterExampleOptions(int numberOfSimulations, int maxAttempts, int maxTransitions) {

    public static final FindCounterExampleOptions DEFAULT = new FindCounterExampleOptions(100, 2000, 1000);

    public FindCounterExampleOptions {
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
