package com.bajaj.trafficlight.service;

import com.bajaj.trafficlight.model.Intersection;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages traffic lights across multiple intersections (in-memory).
 * Supports designing a "system" of several independent traffic lights,
 * each identified by a unique intersection name — e.g. "MG-Road", "Ring-Road".
 */
@Service
public class TrafficLightService {

    // Thread-safe map since multiple requests could hit different intersections concurrently
    private final Map<String, Intersection> intersections = new ConcurrentHashMap<>();

    /**
     * Fetches an existing intersection, or creates a new one (defaulting to RED)
     * if it doesn't exist yet. This means clients never get a 404 for a new name —
     * the system self-registers new intersections on first use.
     */
    public Intersection getOrCreate(String name) {
        return intersections.computeIfAbsent(name, Intersection::new);
    }

    public Intersection advance(String name) {
        Intersection intersection = getOrCreate(name);
        intersection.advance();
        return intersection;
    }

    public Intersection reset(String name) {
        Intersection intersection = getOrCreate(name);
        intersection.reset();
        return intersection;
    }

    public Map<String, Intersection> getAll() {
        return intersections;
    }
}
