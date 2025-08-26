package com.project.back_end.controllers;

import com.project.back_end.models.Patient;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final ServiceClass service;

    @Autowired
    public PatientController(PatientService patientService, ServiceClass service) {
        this.patientService = patientService;
        this.service = service;
    }

    // Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatientDetails(@PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", tokenCheck.getBody().get("error")));
        }
        return patientService.getPatientDetails(token);
    }

    // Create a New Patient
    @PostMapping()
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        if (!service.validatePatient(patient)) {
            return ResponseEntity.status(409).body(Map.of("error", "Patient with email id or phone no already exist"));
        }
        int created = patientService.createPatient(patient);
        if (created == 1) {
            return ResponseEntity.ok(Map.of("success", "Signup successful"));
        } else {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    // Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    // Get Patient Appointments
    @GetMapping("/{token}/appointments")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(@PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", tokenCheck.getBody().get("error")));
        }
        return patientService.getPatientAppointment(null, token); // `null` car patientService utilise le token pour ID
    }

    // Filter Patient Appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", tokenCheck.getBody().get("error")));
        }

        return service.filterPatient(condition, name, token);
    }
}
