package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Doctor is required")
    private Doctor doctor;

    @ManyToOne
    @NotNull(message = "Patient is required")
    private Patient patient;

    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    @NotNull(message = "Status is required")
    private int status; // 0 = Scheduled, 1 = Completed, 2 = Cancelled

    @Size(max = 200, message = "Reason for visit cannot exceed 200 characters")
    private String reasonForVisit;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @JsonIgnore
    private String internalNotes;

    // Constructors
    public Appointment() {
    }

    public Appointment(Long id, Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status,
                       String reasonForVisit, String notes) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    // Helper Methods
    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }

    @Transient
    public LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }

    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }

    @Transient
    public String getStatusDescription() {
        switch (status) {
            case 0: return "Scheduled";
            case 1: return "Completed";
            case 2: return "Cancelled";
            default: return "Unknown";
        }
    }
}
