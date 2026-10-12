package com.ashika.nasapi.controller;

import com.ashika.nasapi.service.NasaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/space")
public class SpaceController {

    private final NasaService nasaService;

    public SpaceController(NasaService nasaService) {
        this.nasaService = nasaService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "NASA Space Explorer"));
    }

    @GetMapping("/apod")
    public ResponseEntity<Map<String, Object>> getApod(@RequestParam(required = false) String date) {
        return ResponseEntity.ok(nasaService.getApod(date));
    }

    @GetMapping("/mars-rover")
    public ResponseEntity<Map<String, Object>> getMarsRover(
            @RequestParam(defaultValue = "1000") String sol,
            @RequestParam(defaultValue = "fhaz") String camera) {
        return ResponseEntity.ok(nasaService.getMarsRover(sol, camera));
    }

    @GetMapping("/neo")
    public ResponseEntity<Map<String, Object>> getNeo(
            @RequestParam String start_date,
            @RequestParam String end_date) {
        return ResponseEntity.ok(nasaService.getNeoFeed(start_date, end_date));
    }
}
