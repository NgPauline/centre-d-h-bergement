// Initialisation du dashboard
document.addEventListener('DOMContentLoaded', function() {
    initialiserDashboard();
    chargerDonneesTempsReel();
});

// Initialisation des composants du dashboard
function initialiserDashboard() {
    // Animation des cartes de statistiques
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Initialisation des graphiques (si Chart.js est disponible)
    if (typeof Chart !== 'undefined') {
        initialiserGraphiques();
    }
    
    // Mise à jour de l'heure actuelle
    actualiserHeure();
    setInterval(actualiserHeure, 60000);
}

// Chargement des données en temps réel
function chargerDonneesTempsReel() {
    // Charger les statistiques mises à jour
    fetch('/api/dashboard/stats')
        .then(response => response.json())
        .then(stats => {
            actualiserAffichageStats(stats);
        })
        .catch(error => {
            console.error('Erreur chargement stats:', error);
        });
    
    // Charger les alertes récentes
    fetch('/api/dashboard/alertes')
        .then(response => response.json())
        .then(alertes => {
            afficherAlertes(alertes);
        })
        .catch(error => {
            console.error('Erreur chargement alertes:', error);
        });
}

// Actualisation de l'affichage des statistiques
function actualiserAffichageStats(stats) {
    // Mettre à jour les cartes de statistiques
    const elementsStats = {
        'totalResidents': '.stat-card:nth-child(1) .stat-number',
        'totalChambres': '.stat-card:nth-child(2) .stat-number',
        'totalPersonnel': '.stat-card:nth-child(3) .stat-number',
        'facturesEnRetard': '.stat-card:nth-child(4) .stat-number'
    };
    
    Object.keys(elementsStats).forEach(statKey => {
        const element = document.querySelector(elementsStats[statKey]);
        if (element && stats[statKey] !== undefined) {
            animerChiffre(element, stats[statKey]);
        }
    });
}

// Animation des chiffres (compteur)
function animerChiffre(element, nouvelleValeur) {
    const ancienneValeur = parseInt(element.textContent) || 0;
    const difference = nouvelleValeur - ancienneValeur;
    const duree = 1000; // 1 seconde
    const steps = 60;
    const stepTime = duree / steps;
    const increment = difference / steps;
    
    let current = ancienneValeur;
    const timer = setInterval(() => {
        current += increment;
        if ((increment > 0 && current >= nouvelleValeur) || (increment < 0 && current <= nouvelleValeur)) {
            clearInterval(timer);
            current = nouvelleValeur;
        }
        element.textContent = Math.round(current);
    }, stepTime);
}

// Affichage des alertes
function afficherAlertes(alertes) {
    const containerAlertes = document.querySelector('.alert-container');
    if (!containerAlertes) return;
    
    containerAlertes.innerHTML = '';
    
    alertes.forEach(alerte => {
        const divAlerte = document.createElement('div');
        divAlerte.className = `alert alert-${alerte.niveau}`;
        divAlerte.innerHTML = `
            <strong>${alerte.titre}</strong>
            <span>${alerte.message}</span>
            <small>${new Date(alerte.date).toLocaleTimeString()}</small>
        `;
        containerAlertes.appendChild(divAlerte);
        
        // Auto-suppression après 10 secondes pour les alertes non critiques
        if (alerte.niveau !== 'danger') {
            setTimeout(() => {
                divAlerte.remove();
            }, 10000);
        }
    });
}

// Actualisation de l'heure
function actualiserHeure() {
    const elementsHeure = document.querySelectorAll('.current-time');
    const maintenant = new Date();
    const heureFormattee = maintenant.toLocaleTimeString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit'
    });
    
    elementsHeure.forEach(element => {
        element.textContent = heureFormattee;
    });
}

// Gestion des tâches urgentes
function marquerTacheTerminee(tacheId) {
    fetch(`/api/taches/${tacheId}/terminer`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            document.querySelector(`[data-tache-id="${tacheId}"]`).remove();
        } else {
            alert('Erreur lors de la mise à jour de la tâche');
        }
    })
    .catch(error => {
        console.error('Erreur:', error);
        alert('Erreur lors de la mise à jour de la tâche');
    });
}

// Recherche en temps réel dans les tableaux
document.addEventListener('DOMContentLoaded', function() {
    const champsRecherche = document.querySelectorAll('input[type="search"], .search-input');
    
    champsRecherche.forEach(champ => {
        champ.addEventListener('input', function() {
            const termeRecherche = this.value.toLowerCase();
            const tableau = this.closest('.card').querySelector('table');
            
            if (tableau) {
                const lignes = tableau.querySelectorAll('tbody tr');
                
                lignes.forEach(ligne => {
                    const texteLigne = ligne.textContent.toLowerCase();
                    if (texteLigne.includes(termeRecherche)) {
                        ligne.style.display = '';
                    } else {
                        ligne.style.display = 'none';
                    }
                });
            }
        });
    });
});

// Initialisation des graphiques (exemple avec Chart.js)
function initialiserGraphiques() {
    // Graphique d'occupation des chambres
    const ctxOccupation = document.getElementById('chart-occupation');
    if (ctxOccupation) {
        new Chart(ctxOccupation, {
            type: 'doughnut',
            data: {
                labels: ['Occupées', 'Disponibles', 'En maintenance'],
                datasets: [{
                    data: [65, 25, 10],
                    backgroundColor: ['#e74c3c', '#27ae60', '#f39c12']
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
    
    // Graphique d'activité mensuelle
    const ctxActivite = document.getElementById('chart-activite');
    if (ctxActivite) {
        new Chart(ctxActivite, {
            type: 'line',
            data: {
                labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun'],
                datasets: [{
                    label: 'Admissions',
                    data: [12, 19, 3, 5, 2, 3],
                    borderColor: '#3498db',
                    tension: 0.1
                }, {
                    label: 'Sorties',
                    data: [8, 15, 7, 12, 6, 9],
                    borderColor: '#e74c3c',
                    tension: 0.1
                }]
            },
            options: {
                responsive: true
            }
        });
    }
}

// Rafraîchissement automatique des données
setInterval(chargerDonneesTempsReel, 30000); // Toutes les 30 secondes

// Gestion du mode sombre/clair
function basculerModeAffichage() {
    document.body.classList.toggle('dark-mode');
    localStorage.setItem('darkMode', document.body.classList.contains('dark-mode'));
}

// Restaurer le mode sauvegardé
document.addEventListener('DOMContentLoaded', function() {
    if (localStorage.getItem('darkMode') === 'true') {
        document.body.classList.add('dark-mode');
    }
});