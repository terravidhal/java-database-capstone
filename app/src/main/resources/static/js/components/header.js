// header.js - Gestion dynamique de l'en-tête selon le rôle et la session

function renderHeader() {
  const headerDiv = document.getElementById("header");

  // Si on est sur la page racine, réinitialiser le rôle et le token
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }

  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // Gestion des sessions invalides
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  // Contenu initial du header (logo + nav)
  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>`;

  // Ajout du contenu selon le rôle
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>`;
  }

  headerContent += `</nav></header>`;

  // Injection dans le DOM
  headerDiv.innerHTML = headerContent;

  // Attacher les écouteurs des boutons dynamiques
  attachHeaderButtonListeners();
}

// Gestion de la déconnexion pour admin et doctor
function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

// Déconnexion spécifique pour patient connecté
function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.setItem("userRole", "patient");
  window.location.href = "/pages/patientDashboard.html";
}

// Ajout des écouteurs pour boutons qui peuvent être présents selon le rôle
function attachHeaderButtonListeners() {
  const patientLoginBtn = document.getElementById("patientLogin");
  const patientSignupBtn = document.getElementById("patientSignup");
  if (patientLoginBtn) patientLoginBtn.addEventListener("click", () => openModal("patientLogin"));
  if (patientSignupBtn) patientSignupBtn.addEventListener("click", () => openModal("patientSignup"));

  const addDocBtn = document.getElementById("addDocBtn");
  if (addDocBtn) addDocBtn.addEventListener("click", () => openModal("addDoctor"));
}

// Initialisation à l'arrivée sur la page
document.addEventListener("DOMContentLoaded", renderHeader);
