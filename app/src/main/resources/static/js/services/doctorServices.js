// doctorServices.js - Gestion des interactions API pour les médecins

import { API_BASE_URL } from "../config/config.js";

// Point de terminaison de l'API pour les docteurs
const DOCTOR_API = API_BASE_URL + "/doctor";

/**
 * Obtenir tous les médecins
 * @returns {Array} Liste des médecins
 */
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error("Erreur lors de la récupération des médecins :", error);
    return [];
  }
}

/**
 * Supprimer un médecin
 * @param {string} id - ID du médecin
 * @param {string} token - Token d'authentification
 * @returns {Object} { success: boolean, message: string }
 */
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}/${token}`, {
      method: "DELETE",
    });
    const data = await response.json();
    return {
      success: data.success || false,
      message: data.message || "",
    };
  } catch (error) {
    console.error("Erreur lors de la suppression du médecin :", error);
    return { success: false, message: "Erreur lors de la suppression" };
  }
}

/**
 * Ajouter un nouveau médecin
 * @param {Object} doctor - Détails du médecin
 * @param {string} token - Token d'authentification
 * @returns {Object} { success: boolean, message: string }
 */
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${token}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });
    const data = await response.json();
    return {
      success: data.success || false,
      message: data.message || "",
    };
  } catch (error) {
    console.error("Erreur lors de l'ajout du médecin :", error);
    return { success: false, message: "Erreur lors de l'ajout du médecin" };
  }
}

/**
 * Filtrer les médecins
 * @param {string} name - Nom du médecin
 * @param {string} time - Disponibilité (AM/PM)
 * @param {string} specialty - Spécialité
 * @returns {Object} { doctors: Array }
 */
export async function filterDoctors(name = "", time = "", specialty = "") {
  try {
    const n = name && name.trim() !== "" ? encodeURIComponent(name) : "-";
    const t = time && time.trim() !== "" ? encodeURIComponent(time) : "-";
    const s = specialty && specialty.trim() !== "" ? encodeURIComponent(specialty) : "-";

    const response = await fetch(
      `${DOCTOR_API}/filter/${n}/${t}/${s}`
    );
    if (!response.ok) {
      console.error("Erreur lors de la récupération des médecins filtrés");
      return { doctors: [] };
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erreur lors du filtrage des médecins :", error);
    alert("Impossible de filtrer les médecins pour le moment.");
    return { doctors: [] };
  }
}
