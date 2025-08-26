// util.js
  function setRole(role) {
    localStorage.setItem("userRole", role);
  }
  
  function getRole() {
    return localStorage.getItem("userRole");
  }
  
  function clearRole() {
    localStorage.removeItem("userRole");
  }

  // Fonction pour gérer la sélection de rôle
  function selectRole(role) {
    setRole(role);
    const token = localStorage.getItem('token');
    
    if (role === "admin") {
      if (token) {
        window.location.href = `/adminDashboard/${token}`;
      } else {
        // Si pas de token, ouvrir le modal de login admin
        if (typeof openModal === 'function') {
          openModal('adminLogin');
        } else {
          // Fallback si openModal n'est pas disponible
          alert('Veuillez vous connecter en tant qu\'admin');
        }
      }
    } else if (role === "doctor") {
      if (token) {
        window.location.href = `/doctorDashboard/${token}`;
      } else {
        // Si pas de token, ouvrir le modal de login doctor
        if (typeof openModal === 'function') {
          openModal('doctorLogin');
        } else {
          // Fallback si openModal n'est pas disponible
          alert('Veuillez vous connecter en tant que docteur');
        }
      }
    } else if (role === "patient") {
      window.location.href = "/pages/patientDashboard.html";
    } else if (role === "loggedPatient") {
      window.location.href = "loggedPatientDashboard.html";
    }
  }

  // Exposer les fonctions globalement
  window.setRole = setRole;
  window.getRole = getRole;
  window.clearRole = clearRole;
  window.selectRole = selectRole;
  
