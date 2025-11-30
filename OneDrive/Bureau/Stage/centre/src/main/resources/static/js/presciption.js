// Fonction pour charger le dossier médical quand on sélectionne un résident
function loadResidentDossier() {
    const residentId = document.getElementById('prescriptionResidentId').value;
    if (residentId) {
        // Appel API pour récupérer le dossier médical du résident
        fetch(`/api/residents/${residentId}/dossier-medical`)
            .then(response => response.json())
            .then(dossier => {
                if (dossier && dossier.id) {
                    document.getElementById('dossierMedicalId').value = dossier.id;
                } else {
                    alert('Ce résident n\'a pas de dossier médical. Veuillez en créer un d\'abord.');
                    document.getElementById('prescriptionResidentId').value = '';
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                alert('Erreur lors du chargement du dossier médical');
            });
    }
}

// Fonction pour soumettre le formulaire avec toutes les données
function submitPrescriptionForm(event) {
    event.preventDefault();
    
    const prescriptionData = {
        id: document.getElementById('prescriptionId').value || null,
        residentId: document.getElementById('prescriptionResidentId').value,
        dossierMedicalId: document.getElementById('dossierMedicalId').value,
        medicamentId: document.getElementById('medicamentId').value,
        medecinExterne: document.getElementById('medecinExterne').value,
        numeroInami: document.getElementById('numeroInami').value,
        specialite: document.getElementById('specialite').value,
        posologie: document.getElementById('posologie').value,
        dateDebut: document.getElementById('dateDebut').value,
        dateFin: document.getElementById('dateFin').value,
        instructions: document.getElementById('instructions').value,
        motif: document.getElementById('motif').value,
        active: document.getElementById('active').checked
    };
    
    // Envoyer les données au serveur
    savePrescription(prescriptionData);
}

// Fonction pour sauvegarder la prescription
function savePrescription(prescriptionData) {
    const url = prescriptionData.id ? `/api/prescriptions/${prescriptionData.id}` : '/api/prescriptions';
    const method = prescriptionData.id ? 'PUT' : 'POST';
    
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(prescriptionData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Erreur lors de la sauvegarde');
        }
    })
    .then(data => {
        alert('Prescription sauvegardée avec succès');
        window.location.href = '/infirmier/prescriptions';
    })
    .catch(error => {
        console.error('Erreur:', error);
        alert('Erreur lors de la sauvegarde de la prescription');
    });
}

// Validation des dates de prescription
document.addEventListener('DOMContentLoaded', function() {
    const dateDebut = document.getElementById('dateDebut');
    const dateFin = document.getElementById('dateFin');
    
    if (dateDebut && dateFin) {
        dateDebut.addEventListener('change', function() {
            if (dateFin.value && new Date(dateFin.value) < new Date(this.value)) {
                alert('La date de fin doit être postérieure à la date de début');
                this.value = '';
            }
        });
        
        dateFin.addEventListener('change', function() {
            if (dateDebut.value && new Date(this.value) < new Date(dateDebut.value)) {
                alert('La date de fin doit être postérieure à la date de début');
                this.value = '';
            }
        });
    }
});

// Gestion de l'état actif/inactif de la prescription
function togglePrescriptionActive(prescriptionId, isActive) {
    if (confirm(`Êtes-vous sûr de vouloir ${isActive ? 'désactiver' : 'activer'} cette prescription ?`)) {
        fetch(`/api/prescriptions/${prescriptionId}/toggle-active`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ active: !isActive })
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Erreur lors de la modification');
            }
        })
        .catch(error => {
            console.error('Erreur:', error);
            alert('Erreur lors de la modification');
        });
    }
}