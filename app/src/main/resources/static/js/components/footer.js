// footer.js - Rendu dynamique du footer

function renderFooter() {
  const footer = document.getElementById("footer");

  if (!footer) return; // Sécurité : s'assurer que l'élément existe

  footer.innerHTML = `
    <footer class="footer">
      <div class="footer-container">
        <!-- Branding -->
        <div class="footer-logo">
          <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo">
          <p>© Copyright 2025. All Rights Reserved by Hospital CMS.</p>
        </div>

        <!-- Links Sections -->
        <div class="footer-links">
          <!-- Company -->
          <div class="footer-column">
            <h4>Company</h4>
            <a href="#">About</a>
            <a href="#">Careers</a>
            <a href="#">Press</a>
          </div>

          <!-- Support -->
          <div class="footer-column">
            <h4>Support</h4>
            <a href="#">Account</a>
            <a href="#">Help Center</a>
            <a href="#">Contact Us</a>
          </div>

          <!-- Legals -->
          <div class="footer-column">
            <h4>Legals</h4>
            <a href="#">Terms & Conditions</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Licensing</a>
          </div>
        </div>
      </div>
    </footer>
  `;
}

// Appel de la fonction pour rendre le footer dès le chargement du script
document.addEventListener("DOMContentLoaded", renderFooter);
