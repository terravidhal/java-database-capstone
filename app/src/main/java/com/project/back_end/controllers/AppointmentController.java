package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ServiceClass service;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, ServiceClass service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // -------------------------
    // GET appointments (doctor only)
    // -------------------------
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {
        // Validation token (doctor)
        if (!service.validateToken(token, "doctor").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }

        LocalDate appointmentDate = LocalDate.parse(date);
        Map<String, Object> result = appointmentService.getAppointment(patientName, appointmentDate, token);

        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    // -------------------------
    // POST - book appointment (patient only)
    // -------------------------
    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@PathVariable String token,
                                             @RequestBody Appointment appointment) {
        // Validation token (patient)
        if (!service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == -1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid doctor ID"));
        } else if (result == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Appointment slot not available"));
        }

        return ResponseEntity.status(201).body(Map.of("success", "Appointment booked successfully"));
    }

    // -------------------------
    // PUT - update appointment (patient only)
    // -------------------------
    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@PathVariable String token,
                                               @RequestBody Appointment appointment) {
        // Validation token (patient)
        if (!service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }

        return appointmentService.updateAppointment(appointment);
    }

    // -------------------------
    // DELETE - cancel appointment (patient only)
    // -------------------------
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id,
                                               @PathVariable String token) {
        // Validation token (patient)
        if (!service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }

        return appointmentService.cancelAppointment(id, token);
    }
}
