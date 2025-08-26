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
            // Vérifie si le créneau est déjà pris pour le même médecin
            if (appointmentRepository.existsByDoctorIdAndAppointmentTime(
                    appointment.getDoctor().getId(),
                    appointment.getAppointmentTime())) {
                return 0; // Créneau déjà pris
            }
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

        // Vérifie si le rendez-vous existe
        Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());
        if (existingOpt.isEmpty()) {
            response.put("error", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment existing = existingOpt.get();

        // Vérifie que le patient correspond
        if (!existing.getPatient().getId().equals(appointment.getPatient().getId())) {
            response.put("error", "Patient ID mismatch");
            return ResponseEntity.status(403).body(response);
        }

        // Vérifie si le nouveau créneau est déjà pris pour ce médecin
        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentTimeAndIdNot(
                existing.getDoctor().getId(),
                appointment.getAppointmentTime(),
                existing.getId());
        if (conflict) {
            response.put("error", "Time slot already booked for this doctor");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Mettre à jour le rendez-vous
            existing.setAppointmentTime(appointment.getAppointmentTime());
            existing.setStatus(appointment.getStatus()); // int, pas besoin de conversion
            existing.setReasonForVisit(appointment.getReasonForVisit());
            existing.setNotes(appointment.getNotes());
            appointmentRepository.save(existing);

            response.put("success", "Appointment updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
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

        // Vérifie que le patient qui annule est bien le propriétaire
        Long patientId = tokenService.getPatientIdFromToken(token);
        if (patientId == null || !existing.getPatient().getId().equals(patientId)) {
            response.put("error", "Unauthorized cancellation");
            return ResponseEntity.status(403).body(response);
        }

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

        Long doctorId = tokenService.getDoctorIdFromToken(token);
        if (doctorId == null) {
            result.put("error", "Invalid token or doctor not found");
            return result;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments;
        if (pname != null && !pname.isEmpty()) {
            appointments = appointmentRepository
                    .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                            doctorId, pname, start, end);
        } else {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        }

        result.put("appointments", appointments);
        return result;
    }

    //
    @Transactional
    public void updateAppointmentStatus(Long appointmentId, int status) {
        Optional<Appointment> existingOpt = appointmentRepository.findById(appointmentId);
        existingOpt.ifPresent(appointment -> {
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
        });
    }

}
