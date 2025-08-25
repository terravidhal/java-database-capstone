// doctorDashboard.js – Gestion des Rendez-vous

import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

// --- Variables globales ---
const tableBody = document.getElementById("patientTableBody");
const searchBar = document.getElementById("searchBar");
const todayButton = document.getElementById("todayButton");
const datePicker = document.getElementById("datePicker");

let selectedDate = new Date().toISOString().split("T")[0]; // format YYYY-MM-DD
let token = localStorage.getItem("token");
let patientName = null;

// --- Gestion de la barre de recherche ---
searchBar.addEventListener("input", () => {
  const inputValue = searchBar.value.trim();
  patientName = inputValue !== "" ? inputValue : "null";
  loadAppointments();
});

// --- Bouton "Rendez-vous d'aujourd'hui" ---
todayButton.addEventListener("click", () => {
  selectedDate = new Date().toISOString().split("T")[0];
  datePicker.value = selectedDate;
  loadAppointments();
});

// --- Sélecteur de date ---
datePicker.addEventListener("change", () => {
  selectedDate = datePicker.value;
  loadAppointments();
});

// --- Fonction pour charger les rendez-vous ---
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    // Nettoyer le tableau avant de recharger
    tableBody.innerHTML = "";

    if (!appointments || appointments.length === 0) {
      const row = document.createElement("tr");
      row.innerHTML = `<td colspan="4">Aucun rendez-vous trouvé pour aujourd'hui.</td>`;
      tableBody.appendChild(row);
      return;
    }

    // Parcourir les rendez-vous et créer les lignes
    appointments.forEach((appt) => {
      const patient = {
        id: appt.patient_id,
        name: appt.patient_name,
        phone: appt.patient_phone,
        email: appt.patient_email,
      };

      const row = createPatientRow(patient, appt);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Erreur lors du chargement des rendez-vous :", error);
    tableBody.innerHTML = `<tr><td colspan="4">Erreur lors du chargement des rendez-vous. Réessayez plus tard.</td></tr>`;
  }
}

// --- Chargement initial ---
window.addEventListener("DOMContentLoaded", () => {
  if (typeof renderContent === "function") {
    renderContent(); // si dispo pour layout
  }
  loadAppointments(); // afficher les rendez-vous du jour par défaut
});
