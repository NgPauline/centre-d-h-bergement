// Script pour améliorer l'UX de recherche des chambres
document.addEventListener('DOMContentLoaded', function() {
    const searchType = document.querySelector('select[name="searchType"]');
    const searchInput = document.querySelector('input[name="search"]');
    
    // Changer le placeholder selon le type de recherche
    if (searchType && searchInput) {
        searchType.addEventListener('change', function() {
            const placeholders = {
                'numero': 'Ex: 101, 202...',
                'etage': 'Ex: 1, 2, 3...',
                'type': 'Ex: STANDARD, SUPERIEUR...',
                'statut': 'Ex: DISPONIBLE, OCCUPEE...',
                'equipement': 'Ex: lit médicalisé, fauteuil...',
                'occupant': 'Ex: Dupont, Martin...'
            };
            searchInput.placeholder = placeholders[this.value] || 'Rechercher...';
        });
        
        // Déclencher l'événement au chargement
        searchType.dispatchEvent(new Event('change'));
    }
});

// Animation pour les barres de progression dans les détails de chambre
document.addEventListener('DOMContentLoaded', function() {
    const progressBars = document.querySelectorAll('.progress-fill');
    progressBars.forEach(bar => {
        const width = bar.style.width;
        bar.style.width = '0';
        setTimeout(() => {
            bar.style.width = width;
        }, 100);
    });
});

// Confirmation de suppression de chambre
function confirmerSuppressionChambre() {
    return confirm('Êtes-vous sûr de vouloir supprimer cette chambre ?');
}

// Gestion des formulaires de chambre
document.addEventListener('DOMContentLoaded', function() {
    const formulaireChambre = document.querySelector('form[th\\:object="${chambre}"]');
    if (formulaireChambre) {
        formulaireChambre.addEventListener('submit', function(e) {
            const numero = document.getElementById('numero').value;
            const etage = document.getElementById('etage').value;
            const type = document.getElementById('type').value;
            const capacite = document.getElementById('capacite').value;
            
            if (!numero || !etage || !type || !capacite) {
                e.preventDefault();
                alert('Veuillez remplir tous les champs obligatoires');
                return;
            }
            
            if (capacite < 1 || capacite > 4) {
                e.preventDefault();
                alert('La capacité doit être comprise entre 1 et 4');
                return;
            }
        });
    }
});