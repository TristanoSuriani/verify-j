package nl.suriani.verifyj;

public enum OutcomeSimulationStatus {
    SUCCESS("Success"),
    FAILURE("Failure"),
    TIMEOUT("Timeout"),
    ERROR("Error"),
    FAILED_INIT("Failed initialization"),
    FAILED_STATE_PROPERTIES("Failed state properties"),
    FAILED_TEMPORAL_PROPERTIES("Failed temporal properties");

    private String value;

    OutcomeSimulationStatus(String value) {
        this.value = value;
    }

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

    public String value() {
        return value;
    }
}
