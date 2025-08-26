// doctorCard.js
// Crée et retourne une carte doctor pour les tableaux de bord Admin et Patient

import { showBookingOverlay } from "../loggedPatient.js"; // pour réservation
import { deleteDoctor } from "../services/doctorServices.js";       // pour supprimer un médecin (Admin)
import { getPatientData } from "../services/patientServices.js";    // pour récupérer les infos patient

export function createDoctorCard(doctor) {
  // Création du container principal
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Récupération du rôle courant
  const role = localStorage.getItem("userRole");

  // Section info du docteur
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialty || doctor.specialization || "Not specified"}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const availability = document.createElement("p");
  availability.textContent = `Available: ${doctor.availableTimes ? doctor.availableTimes.join(", ") : "No availability"}`;

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  // Container pour les boutons
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  // Boutons selon rôle
  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Delete";
    removeBtn.addEventListener("click", async () => {
      if (confirm(`Are you sure you want to delete Dr. ${doctor.name}?`)) {
        const token = localStorage.getItem("token");
        try {
          const result = await deleteDoctor(doctor.id, token);
          if (result.success) {
            alert("Doctor deleted successfully.");
            card.remove();
          } else {
            alert("Failed to delete doctor.");
          }
        } catch (error) {
          console.error(error);
          alert("Error occurred while deleting doctor.");
        }
      }
    });
    actionsDiv.appendChild(removeBtn);
  } 
  else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", () => {
      alert("Patient needs to login first.");
    });
    actionsDiv.appendChild(bookNow);
  } 
  else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please login again.");
        return;
      }
      try {
        const patientData = await getPatientData(token);
        showBookingOverlay(e, doctor, patientData);
      } catch (error) {
        console.error(error);
        alert("Failed to fetch patient data.");
      }
    });
    actionsDiv.appendChild(bookNow);
  }

  // Assemblage final de la carte
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
