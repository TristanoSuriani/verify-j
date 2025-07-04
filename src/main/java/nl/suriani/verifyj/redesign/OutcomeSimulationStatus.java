package nl.suriani.verifyj.redesign;

public enum OutcomeSimulationStatus {
    SUCCESS,
    FAILURE,
    TIMEOUT,
    ERROR,
    FAILED_INIT,
    FAILED_POSTCONDITIONS,
    FAILED_INVARIANTS;

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public boolean isFailure() {
        return this == FAILURE;
    }

    public boolean isTimeout() {
        return this == TIMEOUT;
    }

    public boolean isError() {
        return this == ERROR;
    }

    public boolean isFailedInit() {
        return this == FAILED_INIT;
    }

    public boolean isFailedPostconditions() {
        return this == FAILED_POSTCONDITIONS;
    }

    public boolean isFailedInvariants() {
        return this == FAILED_INVARIANTS;
    }
}
