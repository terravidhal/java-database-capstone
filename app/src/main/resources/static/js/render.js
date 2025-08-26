// render.js
import { openModal } from './components/modals.js';

function renderContent() {
  const role = getRole();
  if (!role) {
    window.location.href = "/"; // if no role, send to role selection page
    return;
  }
}

// Exposer openModal globalement pour que util.js puisse l'utiliser
window.openModal = openModal;
