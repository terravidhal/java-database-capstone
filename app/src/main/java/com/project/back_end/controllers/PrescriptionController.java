package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final ServiceClass service;
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService,
                                  ServiceClass service,
                                  AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    /**
     * Sauvegarder une nouvelle prescription
     * @param prescription Prescription à sauvegarder
     * @param token Token du médecin
     * @return ResponseEntity avec succès ou erreur
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token) {

        // Vérification du token et rôle doctor
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "doctor");
        if (!tokenValidation.getStatusCode().is2xxSuccessful()) {
            return tokenValidation;
        }

        // Mettre à jour le status du rendez-vous associé (0 = Scheduled → 1 = Completed)
        appointmentService.updateAppointmentStatus(prescription.getAppointmentId(), 1);

        // Sauvegarder la prescription
        return prescriptionService.savePrescription(prescription);
    }

    /**
     * Récupérer la prescription par ID de rendez-vous
     * @param appointmentId ID du rendez-vous
     * @param token Token du médecin
     * @return ResponseEntity avec la prescription ou message d'erreur
     */
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        // Vérification du token et rôle doctor
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "doctor");
        if (!tokenValidation.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(tokenValidation.getStatusCode()).body(Map.of("error", "Invalid or expired token"));
        }

        // Récupérer la prescription
        return prescriptionService.getPrescription(appointmentId);
    }
}
