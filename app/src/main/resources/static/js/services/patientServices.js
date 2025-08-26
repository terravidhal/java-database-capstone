// patientServices.js - Gestion des interactions API pour les patients

import { API_BASE_URL } from "../config/config.js";

// Point de terminaison de base pour les patients
const PATIENT_API = API_BASE_URL + "/patient";

/**
 * Inscription d'un patient
 * @param {Object} data - Informations du patient (nom, email, mot de passe...)
 * @returns {Object} { success: boolean, message: string }
 */
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    const result = await response.json();

    if (!response.ok) throw new Error(result.message);

    return { success: response.ok, message: result.message };
  } catch (error) {
    console.error("Erreur patientSignup :", error);
    return { success: false, message: error.message };
  }
}

/**
 * Connexion d'un patient
 * @param {Object} data - { email, password }
 * @returns {Response} Réponse complète du fetch
 */
export async function patientLogin(data) {
  // Adapter au backend: { identifier, password }
  const payload = {
    identifier: data.identifier || data.email,
    password: data.password,
  };
  return await fetch(`${PATIENT_API}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
}

/**
 * Récupérer les informations d'un patient à partir de son token
 * @param {string} token - Token d'authentification
 * @returns {Object|null} Objet patient ou null si erreur
 */
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();
    return response.ok ? data.patient : null;
  } catch (error) {
    console.error("Erreur getPatientData :", error);
    return null;
  }
}

/**
 * Récupérer les rendez-vous d'un patient ou médecin
 * @param {string} id - ID du patient
 * @param {string} token - Token d'authentification
 * @param {string} user - "patient" ou "doctor"
 * @returns {Array|null} Liste des rendez-vous ou null
 */
export async function getPatientAppointments(id, token, user) {
  try {
    // Backend endpoint actuel: /patient/{token}/appointments
    const response = await fetch(`${PATIENT_API}/${token}/appointments`);
    const data = await response.json();
    return response.ok ? data.appointments : null;
  } catch (error) {
    console.error("Erreur getPatientAppointments :", error);
    return null;
  }
}

/**
 * Filtrer les rendez-vous selon condition et nom
 * @param {string} condition - Exemple : "pending", "consulted"
 * @param {string} name - Nom du patient
 * @param {string} token - Token d'authentification
 * @returns {Object} { appointments: Array }
 */
export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(
      `${PATIENT_API}/filter/${condition}/${name}/${token}`,
      { method: "GET", headers: { "Content-Type": "application/json" } }
    );

    if (!response.ok) {
      console.error("Échec filterAppointments :", response.statusText);
      return { appointments: [] };
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erreur filterAppointments :", error);
    alert("Une erreur est survenue !");
    return { appointments: [] };
  }
}
