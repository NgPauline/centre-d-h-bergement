document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.medical-form');
    const substanceInput = document.getElementById('substance');
    const graviteSelect = document.getElementById('gravite');

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
        if (existingError) existingError.remove();
    }

    function validate() {
        let valid = true;
        if (!substanceInput.value.trim()) {
            showError(substanceInput, 'La substance allergène est obligatoire');
            valid = false;
        } else clearError(substanceInput);

        if (!graviteSelect.value) {
            showError(graviteSelect, 'Le niveau de gravité est obligatoire');
            valid = false;
        } else clearError(graviteSelect);

        return valid;
    }

    form.addEventListener('submit', function(e) {
        if (!validate()) {
            e.preventDefault();
            form.querySelector('.form-error').scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    });
});