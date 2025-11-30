// Gestion de l'affichage/masquage du mot de passe
document.addEventListener('DOMContentLoaded', function() {
    // Toggle mot de passe
    const passwordToggle = document.getElementById('passwordToggle');
    const passwordInput = document.getElementById('password');
    
    if (passwordToggle && passwordInput) {
        const toggleIcon = passwordToggle.querySelector('.toggle-icon');
        
        passwordToggle.addEventListener('click', function() {
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleIcon.textContent = '🙈';
            } else {
                passwordInput.type = 'password';
                toggleIcon.textContent = '👁️';
            }
        });
    }
    
    // Animation de chargement sur le bouton de connexion
    const loginForm = document.querySelector('form[th\\:action="@{/login}"]');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            const submitButton = this.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.innerHTML = '<i>⏳</i> Connexion...';
                submitButton.disabled = true;
            }
        });
    }
});

// Validation du formulaire d'inscription
document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.querySelector('form[th\\:action="@{/register}"]');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            
            if (password && confirmPassword && password.value !== confirmPassword.value) {
                e.preventDefault();
                alert('Les mots de passe ne correspondent pas');
                return;
            }
            
            if (password && password.value.length < 6) {
                e.preventDefault();
                alert('Le mot de passe doit contenir au moins 6 caractères');
                return;
            }
        });
    }
});

// Validation du changement de mot de passe
document.addEventListener('DOMContentLoaded', function() {
    const changePasswordForm = document.getElementById('changePasswordForm');
    if (changePasswordForm) {
        changePasswordForm.addEventListener('submit', function(e) {
            const newPassword = this.querySelector('input[name="newPassword"]');
            const confirmPassword = this.querySelector('input[name="confirmPassword"]');
            
            if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
                e.preventDefault();
                alert('Les mots de passe ne correspondent pas');
                return;
            }
            
            if (newPassword && newPassword.value.length < 6) {
                e.preventDefault();
                alert('Le mot de passe doit contenir au moins 6 caractères');
                return;
            }
        });
    }
});