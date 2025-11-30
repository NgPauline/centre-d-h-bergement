// Fonctions pour la gestion des utilisateurs
function desactiverUtilisateur(utilisateurId) {
    if (confirm('Êtes-vous sûr de vouloir désactiver cet utilisateur ?')) {
        fetch('/api/admin/utilisateurs/' + utilisateurId + '/desactiver', {
            method: 'POST'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Erreur lors de la désactivation');
            }
        });
    }
}

function activerUtilisateur(utilisateurId) {
    if (confirm('Êtes-vous sûr de vouloir activer cet utilisateur ?')) {
        fetch('/api/admin/utilisateurs/' + utilisateurId + '/activer', {
            method: 'POST'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Erreur lors de l\'activation');
            }
        });
    }
}

function reinitialiserMotDePasse(utilisateurId) {
    const nouveauMdp = prompt('Entrez le nouveau mot de passe :');
    if (nouveauMdp && nouveauMdp.length >= 6) {
        fetch('/api/admin/utilisateurs/' + utilisateurId + '/reinitialiser-mdp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ nouveauMotDePasse: nouveauMdp })
        })
        .then(response => {
            if (response.ok) {
                alert('Mot de passe réinitialisé avec succès');
                location.reload();
            } else {
                alert('Erreur lors de la réinitialisation');
            }
        });
    } else if (nouveauMdp) {
        alert('Le mot de passe doit contenir au moins 6 caractères');
    }
}

// Fonctions pour la sauvegarde et maintenance
function creerSauvegarde() {
    if (confirm('Créer une sauvegarde complète de la base de données ?')) {
        fetch('/api/admin/sauvegarde', {
            method: 'POST'
        })
        .then(response => {
            if (response.ok) {
                alert('Sauvegarde créée avec succès');
            } else {
                alert('Erreur lors de la sauvegarde');
            }
        });
    }
}

function nettoyerLogs() {
    if (confirm('Supprimer les logs de plus de 6 mois ?')) {
        fetch('/api/admin/nettoyer-logs', {
            method: 'POST'
        })
        .then(response => {
            if (response.ok) {
                alert('Logs nettoyés avec succès');
            } else {
                alert('Erreur lors du nettoyage');
            }
        });
    }
}

function afficherStatistiques() {
    window.location.href = '/admin/statistiques-systeme';
}

// Gestion des paramètres système
document.addEventListener('DOMContentLoaded', function() {
    const parametresForm = document.querySelector('form[th\\:action="@{/admin/parametres}"]');
    if (parametresForm) {
        parametresForm.addEventListener('submit', function(e) {
            const nomCentre = document.querySelector('input[name="nomCentre"]').value;
            const telephone = document.querySelector('input[name="telephone"]').value;
            const email = document.querySelector('input[name="email"]').value;
            
            if (!nomCentre || !telephone || !email) {
                e.preventDefault();
                alert('Veuillez remplir tous les champs obligatoires');
                return;
            }
            
            // Validation de l'email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                e.preventDefault();
                alert('Veuillez entrer une adresse email valide');
                return;
            }
            
            // Validation du téléphone
            const phoneRegex = /^[0-9+\-\s()]{10,}$/;
            if (!phoneRegex.test(telephone)) {
                e.preventDefault();
                alert('Veuillez entrer un numéro de téléphone valide');
                return;
            }
        });
    }
});

// Gestion des logs d'activité
function filtrerLogs(type) {
    const logs = document.querySelectorAll('.log-item');
    logs.forEach(log => {
        if (type === 'all') {
            log.style.display = 'flex';
        } else {
            const logType = log.querySelector('.log-icon').textContent;
            const typeIcons = {
                'CONNEXION': '🔐',
                'MODIFICATION': '✏️',
                'CREATION': '➕',
                'SUPPRESSION': '🗑️'
            };
            
            if (typeIcons[type] === logType) {
                log.style.display = 'flex';
            } else {
                log.style.display = 'none';
            }
        }
    });
}

// Export des données
function exporterDonnees(format) {
    if (confirm(`Exporter les données en format ${format.toUpperCase()} ?`)) {
        fetch(`/api/admin/export/${format}`, {
            method: 'GET'
        })
        .then(response => {
            if (response.ok) {
                return response.blob();
            } else {
                throw new Error('Erreur lors de l\'export');
            }
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = `export-donnees.${format}`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            alert('Export terminé avec succès');
        })
        .catch(error => {
            console.error('Erreur:', error);
            alert('Erreur lors de l\'export des données');
        });
    }
}