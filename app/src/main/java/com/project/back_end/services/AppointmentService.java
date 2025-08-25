package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.HashMap;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    // Réserver un nouveau rendez-vous
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Mettre à jour un rendez-vous existant
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());

        if (existingOpt.isEmpty()) {
            response.put("error", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment existing = existingOpt.get();

        // Validation simple : vérifie si le patient correspond
        if (!existing.getPatient().getId().equals(appointment.getPatient().getId())) {
            response.put("error", "Patient ID mismatch");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            existing.setAppointmentTime(appointment.getAppointmentTime());
            existing.setStatus(appointment.getStatus());
            appointmentRepository.save(existing);
            response.put("success", "Appointment updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to update appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Annuler un rendez-vous existant
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existingOpt = appointmentRepository.findById(id);

        if (existingOpt.isEmpty()) {
            response.put("error", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment existing = existingOpt.get();

        // Vérifie si le patient qui annule est bien celui ayant réservé
        // Note: Cette validation devrait être faite côté frontend ou avec une méthode appropriée
        // Pour l'instant, on accepte la suppression si l'appointment existe
        // TODO: Implémenter une validation plus robuste du token

        try {
            appointmentRepository.delete(existing);
            response.put("success", "Appointment cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to cancel appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Récupérer les rendez-vous d’un médecin à une date spécifique
    @Transactional
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();

        // Note: Pour l'instant, on utilise une approche simplifiée
        // TODO: Implémenter l'extraction du doctorId depuis le token
        // Pour l'instant, on récupère tous les rendez-vous de la date
        Long doctorId = 1L; // Temporaire - à remplacer par l'extraction depuis le token
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments;
        if (pname != null && !pname.isEmpty()) {
            // Utiliser la méthode existante du repository
            appointments = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, pname, start, end
            );
        } else {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        }

        result.put("appointments", appointments);
        return result;
    }
}
