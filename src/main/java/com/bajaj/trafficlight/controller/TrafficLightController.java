package com.bajaj.trafficlight.controller;

import com.bajaj.trafficlight.model.Intersection;
import com.bajaj.trafficlight.service.TrafficLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/traffic-light")
public class TrafficLightController {

    @Autowired
    private TrafficLightService trafficLightService;

    // GET current state of a specific intersection (default = "main")
    // Example: GET /api/traffic-light/state?intersection=MG-Road
    @GetMapping("/state")
    public ResponseEntity<Intersection> getState(
            @RequestParam(defaultValue = "main") String intersection) {
        return ResponseEntity.ok(trafficLightService.getOrCreate(intersection));
    }

    // POST advances the light to its next color: RED -> GREEN -> YELLOW -> RED
    // Example: POST /api/traffic-light/next?intersection=MG-Road
    @PostMapping("/next")
    public ResponseEntity<Intersection> nextState(
            @RequestParam(defaultValue = "main") String intersection) {
        return ResponseEntity.ok(trafficLightService.advance(intersection));
    }

    // POST resets the light back to RED (e.g. emergency vehicle override)
    // Example: POST /api/traffic-light/reset?intersection=MG-Road
    @PostMapping("/reset")
    public ResponseEntity<Intersection> resetState(
            @RequestParam(defaultValue = "main") String intersection) {
        return ResponseEntity.ok(trafficLightService.reset(intersection));
    }

    // GET all intersections currently tracked by the system
    @GetMapping("/all")
    public ResponseEntity<Map<String, Intersection>> getAllIntersections() {
        return ResponseEntity.ok(trafficLightService.getAll());
    }
}
