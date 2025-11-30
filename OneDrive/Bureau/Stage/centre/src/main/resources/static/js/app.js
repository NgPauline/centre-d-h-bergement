// Script principal de l'application
document.addEventListener('DOMContentLoaded', function() {
    initialiserApplication();
});

function initialiserApplication() {
    // Initialisation des tooltips
    initialiserTooltips();
    
    // Gestion des formulaires
    initialiserFormulaires();
    
    // Gestion des notifications
    initialiserNotifications();
    
    // Gestion de la navigation mobile
    initialiserNavigationMobile();
    
    // Gestion des modales
    initialiserModales();
}

// Initialisation des tooltips
function initialiserTooltips() {
    const elementsAvecTooltip = document.querySelectorAll('[title]');
    elementsAvecTooltip.forEach(element => {
        element.addEventListener('mouseenter', function(e) {
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = this.title;
            document.body.appendChild(tooltip);
            
            const rect = this.getBoundingClientRect();
            tooltip.style.left = rect.left + 'px';
            tooltip.style.top = (rect.top - tooltip.offsetHeight - 5) + 'px';
            
            this._tooltip = tooltip;
        });
        
        element.addEventListener('mouseleave', function() {
            if (this._tooltip) {
                this._tooltip.remove();
                this._tooltip = null;
            }
        });
    });
}

// Initialisation des formulaires
function initialiserFormulaires() {
    const formulaires = document.querySelectorAll('form');
    formulaires.forEach(form => {
        form.addEventListener('submit', function(e) {
            const boutonSubmit = this.querySelector('button[type="submit"]');
            if (boutonSubmit) {
                boutonSubmit.disabled = true;
                boutonSubmit.innerHTML = '<span class="loading-spinner"></span> Traitement...';
                
                // Réactiver le bouton après 5 secondes au cas où
                setTimeout(() => {
                    boutonSubmit.disabled = false;
                    boutonSubmit.innerHTML = boutonSubmit.textContent.replace('Traitement...', 'Soumettre');
                }, 5000);
            }
        });
    });
}

// Initialisation des notifications
function initialiserNotifications() {
    // Écouter les messages flash du serveur
    const messagesFlash = document.querySelectorAll('.alert');
    messagesFlash.forEach(message => {
        setTimeout(() => {
            message.style.opacity = '0';
            message.style.transition = 'opacity 0.5s ease';
            setTimeout(() => message.remove(), 500);
        }, 5000);
    });
}

// Initialisation de la navigation mobile
function initialiserNavigationMobile() {
    const boutonMenuMobile = document.createElement('button');
    boutonMenuMobile.className = 'menu-mobile-toggle';
    boutonMenuMobile.innerHTML = '☰';
    boutonMenuMobile.style.display = 'none';
    
    const header = document.querySelector('.header-content');
    if (header) {
        header.appendChild(boutonMenuMobile);
        
        boutonMenuMobile.addEventListener('click', function() {
            const navMenu = document.querySelector('.nav-menu');
            navMenu.classList.toggle('mobile-open');
        });
        
        // Cacher/afficher le menu mobile selon la largeur d'écran
        function gererAffichageMobile() {
            if (window.innerWidth <= 768) {
                boutonMenuMobile.style.display = 'block';
                document.querySelector('.nav-menu').classList.add('mobile-hidden');
            } else {
                boutonMenuMobile.style.display = 'none';
                document.querySelector('.nav-menu').classList.remove('mobile-hidden', 'mobile-open');
            }
        }
        
        window.addEventListener('resize', gererAffichageMobile);
        gererAffichageMobile();
    }
}

// Initialisation des modales
function initialiserModales() {
    const boutonsModale = document.querySelectorAll('[data-modal-target]');
    boutonsModale.forEach(bouton => {
        bouton.addEventListener('click', function() {
            const cibleModale = document.querySelector(this.dataset.modalTarget);
            if (cibleModale) {
                ouvrirModale(cibleModale);
            }
        });
    });
    
    // Fermer les modales en cliquant à l'extérieur
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('modal')) {
            fermerModale(e.target);
        }
    });
    
    // Fermer les modales avec la touche Échap
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            const modaleOuverte = document.querySelector('.modal.open');
            if (modaleOuverte) {
                fermerModale(modaleOuverte);
            }
        }
    });
}

function ouvrirModale(modale) {
    modale.classList.add('open');
    document.body.style.overflow = 'hidden';
}

function fermerModale(modale) {
    modale.classList.remove('open');
    document.body.style.overflow = '';
}

// Fonctions utilitaires
function formaterDate(date) {
    return new Date(date).toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

function formaterHeure(date) {
    return new Date(date).toLocaleTimeString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Gestion des erreurs globales
window.addEventListener('error', function(e) {
    console.error('Erreur globale:', e.error);
    
    // Afficher une notification d'erreur à l'utilisateur
    const notification = document.createElement('div');
    notification.className = 'alert alert-error';
    notification.innerHTML = `
        <strong>Erreur</strong>
        <span>Une erreur s'est produite. Veuillez réessayer.</span>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 5000);
});

// Gestion de la déconnexion
function confirmerDeconnexion() {
    return confirm('Êtes-vous sûr de vouloir vous déconnecter ?');
}

// Export global des fonctions utiles
window.App = {
    formaterDate,
    formaterHeure,
    ouvrirModale,
    fermerModale,
    confirmerDeconnexion
};