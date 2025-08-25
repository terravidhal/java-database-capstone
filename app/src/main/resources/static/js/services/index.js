// index.js - Gestion des connexions Admin et Doctor

// Import des modules nécessaires
import { openModal } from "../components/modals.js"; // pour afficher les popups
import { API_BASE_URL } from "../config/config.js";  // URL de base de l’API

// Définition des endpoints
const ADMIN_API = API_BASE_URL + "/admin";
const DOCTOR_API = API_BASE_URL + "/doctor/login";

// Assure que le DOM est chargé avant d’attacher les listeners
window.onload = function () {
  // Boutons de connexion
  const adminBtn = document.getElementById("adminLogin");
  const doctorBtn = document.getElementById("doctorLogin");

  // Listener pour le bouton Admin
  if (adminBtn) {
    adminBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  // Listener pour le bouton Doctor
  if (doctorBtn) {
    doctorBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

// ========================
// Gestionnaire de connexion Admin
// ========================
window.adminLoginHandler = async function () {
  try {
    // Récupération des champs du formulaire
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    const admin = { username, password };

    // Requête POST vers l’API Admin
    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(admin)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole("admin"); // Fonction d’assistance pour enregistrer le rôle
    } else {
      alert("Identifiants invalides !");
    }
  } catch (error) {
    console.error("Erreur lors de la connexion Admin :", error);
    alert("Une erreur est survenue. Veuillez réessayer.");
  }
};

// ========================
// Gestionnaire de connexion Doctor
// ========================
window.doctorLoginHandler = async function () {
  try {
    // Récupération des champs du formulaire
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    const doctor = { email, password };

    // Requête POST vers l’API Doctor
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole("doctor"); // Fonction d’assistance pour enregistrer le rôle
    } else {
      alert("Identifiants invalides !");
    }
  } catch (error) {
    console.error("Erreur lors de la connexion Doctor :", error);
    alert("Une erreur est survenue. Veuillez réessayer.");
  }
};
