package com.vm.service.claimsreview.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observe")
public class ObservabilityController {
	
	@GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {

        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Service is running");

        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/metrics")
    public ResponseEntity<Map<String, String>> getMetrics() {

        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Service is running");

        return ResponseEntity.ok(response);
    }
	

}
