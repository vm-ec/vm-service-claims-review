package com.vm.service.claimsreview.controllers;

import java.util.HashMap;
import java.util.Map;

import com.vm.service.claimsreview.service.ObservabilityService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observe")
@AllArgsConstructor
public class ObservabilityController {
    private final ObservabilityService observabilityService;
	
	@GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {

        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Service is running");

        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/metrics")
    public ResponseEntity<Map<String, String>> getMetrics() {
        Map<String, String> map = observabilityService.getObservabilityContext();

        Map<String, String> response = new HashMap<>();
        response.put("status", "DOWN");
        response.put("message", "Service is not reporting metrics");

        return ResponseEntity.ok(map.get("metrics") != null ? map : response);
    }
	

}
