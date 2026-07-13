package com.bajaj.trafficlight.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("🚦 Traffic Light System API is running! Try GET /api/traffic-light/state");
    }

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP ✅");
    }
}
