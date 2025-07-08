package nl.suriani.verifyj.redesign;

public enum OutcomeSimulationStatus {
    SUCCESS,
    FAILURE,
    TIMEOUT,
    ERROR,
    FAILED_INIT,
    FAILED_STATE_PROPERTIES,
    FAILED_TEMPORAL_PROPERTIES;

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

    public boolean isFailedStateProperties() {
        return this == FAILED_STATE_PROPERTIES;
    }

    public boolean isFailedInvariants() {
        return this == FAILED_TEMPORAL_PROPERTIES;
    }
}
