package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final ServiceClass service;

    @Autowired
    public DoctorController(DoctorService doctorService, ServiceClass service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // 1️⃣ Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, user);
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            response.put("error", tokenCheck.getBody().get("error"));
            return ResponseEntity.status(401).body(response);
        }

        List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, LocalDate.parse(date));
        response.put("availableSlots", availableSlots);
        return ResponseEntity.ok(response);
    }

    // 2️⃣ Get List of Doctors
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.getDoctors();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    // 3️⃣ Add New Doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "admin");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            response.put("error", tokenCheck.getBody().get("error"));
            return ResponseEntity.status(401).body(response);
        }

        int result = doctorService.saveDoctor(doctor);
        switch (result) {
            case 1 -> response.put("success", "Doctor added to db");
            case -1 -> response.put("error", "Doctor already exists");
            default -> response.put("error", "Some internal error occurred");
        }
        return ResponseEntity.ok(response);
    }

    // 4️⃣ Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // 5️⃣ Update Doctor Details
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "admin");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            response.put("error", tokenCheck.getBody().get("error"));
            return ResponseEntity.status(401).body(response);
        }

        int result = doctorService.updateDoctor(doctor);
        switch (result) {
            case 1 -> response.put("success", "Doctor updated");
            case -1 -> response.put("error", "Doctor not found");
            default -> response.put("error", "Some internal error occurred");
        }
        return ResponseEntity.ok(response);
    }

    // 6️⃣ Delete Doctor
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "admin");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            response.put("error", tokenCheck.getBody().get("error"));
            return ResponseEntity.status(401).body(response);
        }

        int result = doctorService.deleteDoctor(id);
        switch (result) {
            case 1 -> response.put("success", "Doctor deleted successfully");
            case -1 -> response.put("error", "Doctor not found with id");
            default -> response.put("error", "Some internal error occurred");
        }
        return ResponseEntity.ok(response);
    }

    // 7️⃣ Filter Doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        Map<String, Object> filteredDoctors = service.filterDoctor(name, speciality, time);
        return ResponseEntity.ok(filteredDoctors);
    }
}
