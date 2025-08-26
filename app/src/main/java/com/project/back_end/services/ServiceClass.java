package com.project.back_end.services;

import com.project.back_end.models.*;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.DTO.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;  // ✅ manquait
import java.util.Map;
import java.util.Optional;

@Component
public class ServiceClass {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public ServiceClass(TokenService tokenService,
                        AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository,
                        DoctorService doctorService,
                        PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // -------------------------
    // Validation Token
    // -------------------------
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateToken(token, user)) {
            response.put("error", "Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }
        response.put("message", "Token is valid");
        return ResponseEntity.ok(response);
    }

    // -------------------------
    // Validation Admin
    // -------------------------
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
        String token = tokenService.generateToken(admin.getUsername());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // -------------------------
    // Filtrage des médecins
    // -------------------------
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
    }

    // -------------------------
    // Validation rendez-vous
    // -------------------------
    public int validateAppointment(Appointment appointment) {
        try {
            // Vérifier si le médecin existe
            if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
                return -1; // médecin inexistant
            }
            Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
            if (doctorOpt.isEmpty()) {
                return -1; // médecin inexistant
            }

            // Extraire la date et l'heure du rendez-vous
            LocalDate date = appointment.getAppointmentTime().toLocalDate();
            String time = appointment.getAppointmentTime().toLocalTime().toString();

            // Récupérer les créneaux disponibles du médecin
            List<String> availableSlots = doctorService.getDoctorAvailability(appointment.getDoctor().getId(), date);

            // Vérifier si l'heure demandée est dispo
            if (availableSlots.contains(time)) {
                return 1; // créneau valide
            } else {
                return 0; // créneau déjà pris
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // en cas d’erreur technique
        }
    }

    // -------------------------
    // Validation patient
    // -------------------------
    public boolean validatePatient(Patient patient) {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    // -------------------------
    // Login patient
    // -------------------------
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(login.getIdentifier());
        if (patient == null || !patient.getPassword().equals(login.getPassword())) {
            response.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(response);
        }
        String token = tokenService.generateToken(patient.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // -------------------------
    // Filtrage des rendez-vous patient
    // -------------------------
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Map<String, Object> response = new HashMap<>();
        Long patientId = tokenService.getPatientIdFromToken(token);
        if (patientId == null) {
            response.put("error", "Invalid token");
            return ResponseEntity.status(401).body(response);
        }

        if (condition != null && name != null) {
            return patientService.filterByDoctorAndCondition(condition, name, patientId);
        } else if (condition != null) {
            return patientService.filterByCondition(condition, patientId);
        } else if (name != null) {
            return patientService.filterByDoctor(name, patientId);
        } else {
            return patientService.getPatientAppointment(patientId, token);
        }
    }
}
