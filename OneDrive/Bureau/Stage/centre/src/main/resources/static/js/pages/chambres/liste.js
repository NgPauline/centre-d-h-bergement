// Variables globales
let currentView = 'grid';
let chambres = [];
let residents = [];

// Chargement initial
document.addEventListener('DOMContentLoaded', function() {
    loadChambres();
    loadResidents();
    updateStats();
});

// Charger les chambres
async function loadChambres() {
    try {
        // Simulation d'appel API
        const response = await fetch('/api/chambres');
        chambres = await response.json();
        renderChambres();
        updateStats();
    } catch (error) {
        console.error('Erreur lors du chargement des chambres:', error);
        showNotification('Erreur lors du chargement des chambres', 'error');
    }
}

// Charger les résidents pour l'assignation
async function loadResidents() {
    try {
        const response = await fetch('/api/residents');
        residents = await response.json();
        populateResidentSelect();
    } catch (error) {
        console.error('Erreur lors du chargement des résidents:', error);
    }
}

// Mettre à jour les statistiques
function updateStats() {
    const total = chambres.length;
    const available = chambres.filter(c => c.statut === 'DISPONIBLE').length;
    const occupied = chambres.filter(c => c.statut === 'OCCUPEE').length;
    const accessible = chambres.filter(c => c.accessible).length;

    document.getElementById('totalRooms').textContent = total;
    document.getElementById('availableRooms').textContent = available;
    document.getElementById('occupiedRooms').textContent = occupied;
    document.getElementById('accessibleRooms').textContent = accessible;
}

// Rendu des chambres
function renderChambres() {
    const filteredChambres = filterChambres();
    
    if (currentView === 'grid') {
        renderGridView(filteredChambres);
    } else {
        renderListView(filteredChambres);
    }
}

// Filtrage des chambres
function filterChambres() {
    const typeFilter = document.getElementById('typeFilter').value;
    const floorFilter = document.getElementById('floorFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;

    return chambres.filter(chambre => {
        const typeMatch = !typeFilter || chambre.type === typeFilter;
        const floorMatch = !floorFilter || chambre.etage.toString() === floorFilter;
        const statusMatch = statusFilter === 'all' || 
                           (statusFilter === 'available' && chambre.statut === 'DISPONIBLE') ||
                           (statusFilter === 'occupied' && chambre.statut === 'OCCUPEE');
        
        return typeMatch && floorMatch && statusMatch;
    });
}

// Rendu vue grille
function renderGridView(chambres) {
    const gridView = document.getElementById('gridView');
    gridView.innerHTML = '';

    chambres.forEach(chambre => {
        const roomCard = createRoomCard(chambre);
        gridView.appendChild(roomCard);
    });
}

// Rendu vue liste
function renderListView(chambres) {
    const tableBody = document.getElementById('roomsTableBody');
    tableBody.innerHTML = '';

    chambres.forEach(chambre => {
        const row = createTableRow(chambre);
        tableBody.appendChild(row);
    });
}

// Création d'une carte chambre
function createRoomCard(chambre) {
    const card = document.createElement('div');
    card.className = `room-card ${chambre.statut.toLowerCase()}`;
    
    const statusClass = chambre.statut === 'DISPONIBLE' ? 'badge-success' : 
                       chambre.statut === 'OCCUPEE' ? 'badge-danger' : 'badge-warning';
    
    card.innerHTML = `
        <div class="room-header">
            <div class="room-number">${chambre.numero}</div>
            <div class="room-type">${getTypeLabel(chambre.type)}</div>
        </div>
        <div class="room-details">
            <div class="room-detail">
                <span class="label">Étage:</span>
                <span class="value">${chambre.etage}</span>
            </div>
            <div class="room-detail">
                <span class="label">Capacité:</span>
                <span class="value">${chambre.capacite} pers.</span>
            </div>
            <div class="room-detail">
                <span class="label">Occupants:</span>
                <span class="value">${chambre.occupants || 0}/${chambre.capacite}</span>
            </div>
            <div class="room-detail">
                <span class="label">Statut:</span>
                <span class="badge ${statusClass}">${getStatusLabel(chambre.statut)}</span>
            </div>
            ${chambre.accessible ? '<div class="room-detail"><span class="label">♿ Accessible PMR</span></div>' : ''}
        </div>
        <div class="room-actions">
            <button class="btn btn-sm btn-outline" onclick="editChambre(${chambre.id})">
                ✏️ Modifier
            </button>
            ${chambre.statut === 'DISPONIBLE' ? 
                `<button class="btn btn-sm btn-success" onclick="openAssignModal(${chambre.id})">
                    👤 Assigner
                </button>` : 
                `<button class="btn btn-sm btn-warning" onclick="libererChambre(${chambre.id})">
                    🚪 Libérer
                </button>`
            }
            <button class="btn btn-sm btn-danger" onclick="deleteChambre(${chambre.id})">
                🗑️ Supprimer
            </button>
        </div>
    `;
    
    return card;
}

// Création d'une ligne de tableau
function createTableRow(chambre) {
    const row = document.createElement('tr');
    const statusClass = chambre.statut === 'DISPONIBLE' ? 'badge-success' : 
                       chambre.statut === 'OCCUPEE' ? 'badge-danger' : 'badge-warning';
    
    row.innerHTML = `
        <td><strong>${chambre.numero}</strong></td>
        <td>${chambre.etage}</td>
        <td>${getTypeLabel(chambre.type)}</td>
        <td>${chambre.capacite}</td>
        <td>${chambre.occupants || 0}/${chambre.capacite}</td>
        <td><span class="badge ${statusClass}">${getStatusLabel(chambre.statut)}</span></td>
        <td>
            <div class="room-actions">
                <button class="btn btn-sm btn-outline" onclick="editChambre(${chambre.id})">✏️</button>
                ${chambre.statut === 'DISPONIBLE' ? 
                    `<button class="btn btn-sm btn-success" onclick="openAssignModal(${chambre.id})">👤</button>` : 
                    `<button class="btn btn-sm btn-warning" onclick="libererChambre(${chambre.id})">🚪</button>`
                }
                <button class="btn btn-sm btn-danger" onclick="deleteChambre(${chambre.id})">🗑️</button>
            </div>
        </td>
    `;
    
    return row;
}

// Basculer entre les vues
function toggleView() {
    const gridView = document.getElementById('gridView');
    const listView = document.getElementById('listView');
    const viewIcon = document.getElementById('viewIcon');
    const viewText = document.getElementById('viewText');
    
    if (currentView === 'grid') {
        currentView = 'list';
        gridView.style.display = 'none';
        listView.style.display = 'block';
        viewIcon.textContent = '🏠';
        viewText.textContent = 'Vue grille';
    } else {
        currentView = 'grid';
        gridView.style.display = 'grid';
        listView.style.display = 'none';
        viewIcon.textContent = '📋';
        viewText.textContent = 'Vue liste';
    }
    
    renderChambres();
}

// Ouvrir modal création
function openCreateModal() {
    document.getElementById('modalTitle').textContent = 'Nouvelle Chambre';
    document.getElementById('chambreForm').reset();
    document.getElementById('chambreId').value = '';
    document.getElementById('chambreModal').style.display = 'block';
}

// Ouvrir modal édition
function editChambre(id) {
    const chambre = chambres.find(c => c.id === id);
    if (!chambre) return;
    
    document.getElementById('modalTitle').textContent = 'Modifier la Chambre';
    document.getElementById('chambreId').value = chambre.id;
    document.getElementById('numero').value = chambre.numero;
    document.getElementById('etage').value = chambre.etage;
    document.getElementById('type').value = chambre.type;
    document.getElementById('capacite').value = chambre.capacite;
    document.getElementById('description').value = chambre.description || '';
    document.getElementById('accessible').checked = chambre.accessible;
    
    document.getElementById('chambreModal').style.display = 'block';
}

// Soumettre le formulaire chambre
async function submitChambreForm(event) {
    event.preventDefault();
    
    const formData = {
        id: document.getElementById('chambreId').value || null,
        numero: document.getElementById('numero').value,
        etage: parseInt(document.getElementById('etage').value),
        type: document.getElementById('type').value,
        capacite: parseInt(document.getElementById('capacite').value),
        description: document.getElementById('description').value,
        accessible: document.getElementById('accessible').checked
    };
    
    try {
        const url = formData.id ? `/api/chambres/${formData.id}` : '/api/chambres';
        const method = formData.id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            closeModal();
            loadChambres();
            showNotification(
                formData.id ? 'Chambre modifiée avec succès' : 'Chambre créée avec succès', 
                'success'
            );
        } else {
            throw new Error('Erreur lors de la sauvegarde');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la sauvegarde', 'error');
    }
}

// Ouvrir modal d'assignation
function openAssignModal(chambreId) {
    const chambre = chambres.find(c => c.id === chambreId);
    if (!chambre) return;
    
    document.getElementById('assignRoomId').value = chambreId;
    document.getElementById('assignRoomNumber').textContent = `Chambre ${chambre.numero}`;
    document.getElementById('assignCapacity').textContent = `${chambre.capacite} personnes`;
    document.getElementById('assignCurrent').textContent = `${chambre.occupants || 0}/${chambre.capacite}`;
    
    document.getElementById('assignModal').style.display = 'block';
}

// Peupler la liste des résidents
function populateResidentSelect() {
    const select = document.getElementById('residentSelect');
    select.innerHTML = '<option value="">Choisir un résident</option>';
    
    residents.forEach(resident => {
        const option = document.createElement('option');
        option.value = resident.id;
        option.textContent = `${resident.prenom} ${resident.nom}`;
        select.appendChild(option);
    });
}

// Assigner un résident
async function assignResident(event) {
    event.preventDefault();
    
    const chambreId = document.getElementById('assignRoomId').value;
    const residentId = document.getElementById('residentSelect').value;
    
    if (!residentId) {
        showNotification('Veuillez sélectionner un résident', 'warning');
        return;
    }
    
    try {
        const response = await fetch(`/api/chambres/${chambreId}/assign`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ residentId })
        });
        
        if (response.ok) {
            closeAssignModal();
            loadChambres();
            showNotification('Résident assigné avec succès', 'success');
        } else {
            throw new Error('Erreur lors de l\'assignation');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de l\'assignation', 'error');
    }
}

// Libérer une chambre
async function libererChambre(chambreId) {
    if (!confirm('Êtes-vous sûr de vouloir libérer cette chambre ?')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/chambres/${chambreId}/liberer`, {
            method: 'POST'
        });
        
        if (response.ok) {
            loadChambres();
            showNotification('Chambre libérée avec succès', 'success');
        } else {
            throw new Error('Erreur lors de la libération');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la libération', 'error');
    }
}

// Supprimer une chambre
async function deleteChambre(chambreId) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette chambre ? Cette action est irréversible.')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/chambres/${chambreId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            loadChambres();
            showNotification('Chambre supprimée avec succès', 'success');
        } else {
            throw new Error('Erreur lors de la suppression');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la suppression', 'error');
    }
}

// Fermer les modaux
function closeModal() {
    document.getElementById('chambreModal').style.display = 'none';
}

function closeAssignModal() {
    document.getElementById('assignModal').style.display = 'none';
    document.getElementById('residentSelect').value = '';
}

// Utilitaires
function getTypeLabel(type) {
    const types = {
        'SIMPLE': 'Simple',
        'DOUBLE': 'Double',
        'TRIPLE': 'Triple',
        'COLLECTIVE': 'Collective'
    };
    return types[type] || type;
}

function getStatusLabel(status) {
    const statuses = {
        'DISPONIBLE': 'Disponible',
        'OCCUPEE': 'Occupée',
        'MAINTENANCE': 'Maintenance'
    };
    return statuses[status] || status;
}

function showNotification(message, type = 'info') {
    // Implémentation basique de notification
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 1.5rem;
        border-radius: 4px;
        color: white;
        z-index: 10000;
        animation: slideIn 0.3s ease;
    `;
    
    if (type === 'success') notification.style.background = '#28a745';
    else if (type === 'error') notification.style.background = '#dc3545';
    else if (type === 'warning') notification.style.background = '#ffc107';
    else notification.style.background = '#17a2b8';
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Gestion des clics en dehors des modaux
window.onclick = function(event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}