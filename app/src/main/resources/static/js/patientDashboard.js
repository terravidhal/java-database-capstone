// patientDashboard.js

// === Imports ===
import { createDoctorCard } from './components/doctorCard.js';
import { openModal } from './components/modals.js';
import { getDoctors, filterDoctors } from './services/doctorServices.js';
import { patientLogin, patientSignup } from './services/patientServices.js';

// === Page Load: Load all doctors by default ===
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});

// === Bind Modal Triggers ===
document.addEventListener("DOMContentLoaded", () => {
  const signupBtn = document.getElementById("patientSignup");
  if (signupBtn) signupBtn.addEventListener("click", () => openModal("patientSignup"));

  const loginBtn = document.getElementById("patientLogin");
  if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));
});

// === Load Doctors (default or filtered) ===
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("❌ Failed to load doctors:", error);
    document.getElementById("content").innerHTML = "<p>Error loading doctors. Try again later.</p>";
  }
}

// === Render Doctors Utility ===
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  if (doctors && doctors.length > 0) {
    doctors.forEach(doctor => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });
  } else {
    contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
  }
}

// === Search & Filters ===
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

async function filterDoctorsOnChange() {
  const searchBar = document.getElementById("searchBar").value.trim();
  const filterTime = document.getElementById("filterTime").value;
  const filterSpecialty = document.getElementById("filterSpecialty").value;

  const name = searchBar.length > 0 ? searchBar : null;
  const time = filterTime.length > 0 ? filterTime : null;
  const specialty = filterSpecialty.length > 0 ? filterSpecialty : null;

  try {
    const response = await filterDoctors(name, time, specialty);
    renderDoctorCards(response.doctors);
  } catch (error) {
    console.error("❌ Failed to filter doctors:", error);
    document.getElementById("content").innerHTML = "<p>Error filtering doctors. Try again later.</p>";
  }
}

// === Patient Signup ===
window.signupPatient = async function () {
  try {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const phone = document.getElementById("phone").value;
    const address = document.getElementById("address").value;

    const data = { name, email, password, phone, address };
    const { success, message } = await patientSignup(data);

    if (success) {
      alert(message);
      document.getElementById("modal").style.display = "none";
      window.location.reload();
    } else {
      alert(message);
    }
  } catch (error) {
    console.error("❌ Signup failed:", error);
    alert("❌ An error occurred while signing up.");
  }
};

// === Patient Login ===
window.loginPatient = async function () {
  try {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await patientLogin({ email, password });
    if (response.ok) {
      const result = await response.json();
      localStorage.setItem('token', result.token);
      selectRole('loggedPatient'); // ← assure-toi que cette fonction existe
      window.location.href = '/pages/loggedPatientDashboard.html';
    } else {
      alert('❌ Invalid credentials!');
    }
  } catch (error) {
    console.error("❌ Login failed:", error);
    alert("❌ Failed to login. Please try again later.");
  }
};
