document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('.medical-form');
            const nomInput = document.getElementById('nom');
            const dateDiagnosticInput = document.getElementById('dateDiagnostic');
            
            // Validation en temps réel
            if (nomInput) {
                nomInput.addEventListener('blur', validateNom);
            }
            
            if (dateDiagnosticInput) {
                dateDiagnosticInput.addEventListener('change', validateDate);
            }
            
            function validateNom() {
                const value = nomInput.value.trim();
                if (value === '') {
                    showError(nomInput, 'Le nom de la pathologie est obligatoire');
                } else {
                    clearError(nomInput);
                }
            }
            
            function validateDate() {
                const value = dateDiagnosticInput.value;
                if (value) {
                    const selectedDate = new Date(value);
                    const today = new Date();
                    if (selectedDate > today) {
                        showError(dateDiagnosticInput, 'La date de diagnostic ne peut pas être dans le futur');
                    } else {
                        clearError(dateDiagnosticInput);
                    }
                }
            }
            
            function showError(input, message) {
                clearError(input);
                input.style.borderColor = '#dc3545';
                const errorDiv = document.createElement('div');
                errorDiv.className = 'form-error';
                errorDiv.style.color = '#dc3545';
                errorDiv.style.fontSize = '0.875rem';
                errorDiv.style.marginTop = '0.25rem';
                errorDiv.textContent = message;
                input.parentNode.appendChild(errorDiv);
            }
            
            function clearError(input) {
                input.style.borderColor = '#ddd';
                const existingError = input.parentNode.querySelector('.form-error');
                if (existingError) {
                    existingError.remove();
                }
            }
            
            // Validation avant soumission
            if (form) {
                form.addEventListener('submit', function(e) {
                    let isValid = true;
                    
                    if (nomInput && nomInput.value.trim() === '') {
                        validateNom();
                        isValid = false;
                    }
                    
                    if (dateDiagnosticInput && dateDiagnosticInput.value) {
                        const selectedDate = new Date(dateDiagnosticInput.value);
                        const today = new Date();
                        if (selectedDate > today) {
                            validateDate();
                            isValid = false;
                        }
                    }
                    
                    if (!isValid) {
                        e.preventDefault();
                        const firstError = form.querySelector('.form-error');
                        if (firstError) {
                            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        }
                    }
                });
            }
            
            // Set max date to today for date input
            if (dateDiagnosticInput) {
                const today = new Date().toISOString().split('T')[0];
                dateDiagnosticInput.max = today;
            }
        });