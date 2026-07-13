package com.bajaj.trafficlight.model;

/**
 * Represents the state of a traffic light.
 * Standard traffic light cycle: RED -> GREEN -> YELLOW -> RED -> ...
 */
public enum LightColor {
    RED(20),    // stays red for 20 seconds
    GREEN(15),  // stays green for 15 seconds
    YELLOW(5);  // stays yellow for 5 seconds

    private final int durationSeconds;

    LightColor(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Returns the next state in the traffic light cycle.
     * This is the core "state machine" logic of the system.
     */
    public LightColor next() {
        return switch (this) {
            case RED -> GREEN;
            case GREEN -> YELLOW;
            case YELLOW -> RED;
        };
    }
}
