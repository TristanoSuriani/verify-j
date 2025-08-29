package nl.suriani.verifyj;

/**
 * Enum representing the possible statuses of a simulation outcome.
 */
public enum OutcomeSimulationStatus {
    /** Simulation completed successfully. */
    SUCCESS("Success"),
    /** Simulation failed. */
    FAILURE("Failure"),
    /** Simulation timed out. */
    TIMEOUT("Timeout"),
    /** Simulation encountered an error. */
    ERROR("Error"),
    /** Simulation failed during initialization. */
    FAILED_INIT("Failed initialization"),
    /** Simulation failed due to state property violation. */
    FAILED_STATE_PROPERTIES("Failed state properties"),
    /** Simulation failed due to temporal property violation. */
    FAILED_TEMPORAL_PROPERTIES("Failed temporal properties");

    private String value;

    OutcomeSimulationStatus(String value) {
        this.value = value;
    }

    /**
     * Returns true if the status is SUCCESS.
     * @return true if success
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }

    /**
     * Returns true if the status is FAILURE.
     * @return true if failure
     */
    public boolean isFailure() {
        return this == FAILURE;
    }

    /**
     * Returns true if the status is TIMEOUT.
     * @return true if timeout
     */
    public boolean isTimeout() {
        return this == TIMEOUT;
    }

    /**
     * Returns true if the status is ERROR.
     * @return true if error
     */
    public boolean isError() {
        return this == ERROR;
    }

    /**
     * Returns true if the status is FAILED_INIT.
     * @return true if failed initialization
     */
    public boolean isFailedInit() {
        return this == FAILED_INIT;
    }

    /**
     * Returns true if the status is FAILED_STATE_PROPERTIES.
     * @return true if failed state properties
     */
    public boolean isFailedStateProperties() {
        return this == FAILED_STATE_PROPERTIES;
    }

    /**
     * Returns true if the status is FAILED_TEMPORAL_PROPERTIES.
     * @return true if failed temporal properties
     */
    public boolean isFailedInvariants() {
        return this == FAILED_TEMPORAL_PROPERTIES;
    }

    /**
     * Returns the string value of the status.
     * @return the status value
     */
    public String value() {
        return value;
    }
}
