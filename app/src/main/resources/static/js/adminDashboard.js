// adminDashboard.js - Gestion du tableau de bord Admin (Médecins)

import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

// DOM Elements
const contentDiv = document.getElementById("content");
const searchBar = document.getElementById("searchBar");
const filterTime = document.getElementById("filterTime");
const filterSpecialty = document.getElementById("filterSpecialty");
const addDocBtn = document.getElementById("addDocBtn");

// --- Gestion du modal "Ajouter un Médecin" ---
addDocBtn.addEventListener("click", () => {
  openModal("addDoctor");
});

// --- Chargement initial des cartes de médecins ---
window.addEventListener("DOMContentLoaded", loadDoctorCards);

// --- Fonction pour charger toutes les cartes ---
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Erreur lors du chargement des médecins :", error);
  }
}

// --- Fonction utilitaire pour rendre les cartes ---
function renderDoctorCards(doctors) {
  contentDiv.innerHTML = ""; // Clear previous content
  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = "<p>Aucun médecin disponible.</p>";
    return;
  }
  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// --- Événements de recherche et de filtrage ---
searchBar.addEventListener("input", filterDoctorsOnChange);
filterTime.addEventListener("change", filterDoctorsOnChange);
filterSpecialty.addEventListener("change", filterDoctorsOnChange);

// --- Fonction pour filtrer les médecins ---
async function filterDoctorsOnChange() {
  const name = searchBar.value.trim() || null;
  const time = filterTime.value || null;
  const specialty = filterSpecialty.value || null;

  try {
    const doctors = await filterDoctors(name, time, specialty);
    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = "<p>Aucun médecin trouvé avec les filtres appliqués.</p>";
    } else {
      renderDoctorCards(doctors);
    }
  } catch (error) {
    console.error("Erreur lors du filtrage des médecins :", error);
    alert("Une erreur est survenue lors du filtrage des médecins.");
  }
}

// --- Ajouter un nouveau médecin via le formulaire modal ---
export async function adminAddDoctor(formData) {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Vous devez être connecté pour ajouter un médecin !");
      return;
    }

    // Rassembler les données du formulaire
    const doctor = {
      name: formData.name,
      email: formData.email,
      phone: formData.phone,
      password: formData.password,
      specialty: formData.specialty,
      availability: formData.availability, // array of selected times
    };

    // Appel API pour sauvegarder le médecin
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert("Médecin ajouté avec succès !");
      openModal("addDoctor", false); // fermer le modal
      loadDoctorCards(); // rafraîchir la liste
    } else {
      alert(`Échec de l'ajout du médecin : ${result.message}`);
    }
  } catch (error) {
    console.error("Erreur adminAddDoctor :", error);
    alert("Une erreur est survenue lors de l'ajout du médecin.");
  }
}
