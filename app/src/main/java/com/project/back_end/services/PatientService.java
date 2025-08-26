package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // Créer un nouveau patient
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Récupérer les rendez-vous d’un patient spécifique
    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                response.put("error", "Patient not found");
                return ResponseEntity.status(404).body(response);
            }
            Long patientId = patient.getId();
            List<AppointmentDTO> appointments = appointmentRepository.findByPatientId(patientId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.put("appointments", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Filtrer les rendez-vous par condition (past/future)
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            int status;
            if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else {
                response.put("error", "Invalid condition");
                return ResponseEntity.badRequest().body(response);
            }
            List<AppointmentDTO> appointments = appointmentRepository
                    .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.put("appointments", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Filtrer les rendez-vous par nom du médecin
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AppointmentDTO> appointments = appointmentRepository
                    .filterByDoctorNameAndPatientId(name, patientId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.put("appointments", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Filtrer par nom du médecin et condition (past/future)
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            int status;
            if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else {
                response.put("error", "Invalid condition");
                return ResponseEntity.badRequest().body(response);
            }
            List<AppointmentDTO> appointments = appointmentRepository
                    .filterByDoctorNameAndPatientIdAndStatus(name, patientId, status)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.put("appointments", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Récupérer les détails d’un patient à partir du token
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                response.put("error", "Patient not found");
                return ResponseEntity.status(404).body(response);
            }
            response.put("patient", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Méthode utilitaire pour convertir Appointment en AppointmentDTO
    private AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(
            appointment.getId(),
            appointment.getDoctor().getId(),
            appointment.getDoctor().getName(),
            appointment.getPatient().getId(),
            appointment.getPatient().getName(),
            appointment.getPatient().getEmail(),
            appointment.getPatient().getPhone(),
            appointment.getPatient().getAddress(),
            appointment.getAppointmentTime(),
            appointment.getStatus()
        );
    }
}
