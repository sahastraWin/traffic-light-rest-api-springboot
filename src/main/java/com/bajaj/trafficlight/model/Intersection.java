package com.bajaj.trafficlight.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single traffic light installed at an intersection.
 * Keeps track of its current color and a history log of transitions.
 */
public class Intersection {

    private final String name;
    private LightColor currentColor;
    private LocalDateTime lastChangedAt;
    private final List<String> history = new ArrayList<>();

    public Intersection(String name) {
        this.name = name;
        this.currentColor = LightColor.RED; // every light starts at RED for safety
        this.lastChangedAt = LocalDateTime.now();
        history.add("Initialized at RED");
    }

    public String getName() {
        return name;
    }

    public LightColor getCurrentColor() {
        return currentColor;
    }

    public LocalDateTime getLastChangedAt() {
        return lastChangedAt;
    }

    public List<String> getHistory() {
        return history;
    }

    /**
     * Advances the light to its next state and logs the transition.
     */
    public void advance() {
        LightColor previous = this.currentColor;
        this.currentColor = this.currentColor.next();
        this.lastChangedAt = LocalDateTime.now();
        history.add(previous + " -> " + this.currentColor + " at " + lastChangedAt);
    }

    /**
     * Resets the light back to RED (e.g., emergency override).
     */
    public void reset() {
        this.currentColor = LightColor.RED;
        this.lastChangedAt = LocalDateTime.now();
        history.add("RESET -> RED at " + lastChangedAt);
    }
}
