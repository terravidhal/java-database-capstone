package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Trouver un patient par son adresse email
    Patient findByEmail(String email);

    // Trouver un patient par email ou numéro de téléphone
    Patient findByEmailOrPhone(String email, String phone);
}
