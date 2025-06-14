<?php
require_once 'config.php';

// Si ya está logueado, redirigir al dashboard
if (isLoggedIn()) {
    header("Location: dashboard.php");
    exit;
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - LibrosFab</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
        .auth-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #000, #FFD700); /* negro a amarillo */
            padding: 20px;
        }

        .auth-box {
            background: #fff; /* blanco */
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }

        .auth-tabs {
            display: flex;
            margin-bottom: 2rem;
            border-radius: 10px;
            background: #f4f4f4; /* blanco grisáceo */
            padding: 4px;
        }

        .auth-tab {
            flex: 1;
            text-align: center;
            padding: 12px;
            cursor: pointer;
            border-radius: 8px;
            transition: all 0.3s;
            font-weight: 500;
            color: #000;
        }

        .auth-tab.active {
            background: #FFD700; /* amarillo */
            color: #000; /* negro */
        }

        .auth-form {
            display: none;
        }

        .auth-form.active {
            display: block;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #000; /* negro */
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #ccc;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s;
            background-color: #fff;
            color: #000;
        }

        .form-group input:focus {
            outline: none;
            border-color: #FFD700; /* amarillo */
        }

        .auth-btn {
            width: 100%;
            background: #FFD700; /* amarillo */
            color: #000;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            transition: background 0.3s;
            font-weight: 500;
        }

        .auth-btn:hover {
            background: #e6c200; /* amarillo oscuro */
        }

        .auth-btn:disabled {
            background: #eee; /* gris claro */
            color: #999;
            cursor: not-allowed;
        }

        .alert {
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 1rem;
            display: none;
        }

        .alert.success {
            background: #fffbe6; /* fondo claro amarillento */
            color: #856404;
            border: 1px solid #ffeeba;
        }

        .alert.error {
            background: #fff1f2; /* fondo blanco rosado */
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .back-link {
            text-align: center;
            margin-top: 2rem;
        }

        .back-link a {
            color: #FFD700; /* amarillo */
            text-decoration: none;
            font-weight: 500;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>
    <div class="auth-container">
        <div class="auth-box">
            <div class="logo" style="text-align: center; font-size: 2rem; color: #2c3e50; margin-bottom: 2rem;">
                LibrosFab
            </div>
            
            <div class="auth-tabs">
                <div class="auth-tab active" data-tab="login">Iniciar Sesión</div>
                <div class="auth-tab" data-tab="register">Registrarse</div>
            </div>
            
            <div class="alert" id="alert"></div>
            
            <!-- Formulario de Login -->
            <form class="auth-form active" id="loginForm">
                <div class="form-group">
                    <label for="loginEmail">Email</label>
                    <input type="email" id="loginEmail" name="email" required>
                </div>
                <div class="form-group">
                    <label for="loginPassword">Contraseña</label>
                    <input type="password" id="loginPassword" name="password" required>
                </div>
                <button type="submit" class="auth-btn">Iniciar Sesión</button>
            </form>
            
            <!-- Formulario de Registro -->
            <form class="auth-form" id="registerForm">
                <div class="form-group">
                    <label for="registerEmail">Email</label>
                    <input type="email" id="registerEmail" name="email" required>
                </div>
                <div class="form-group">
                    <label for="registerPassword">Contraseña</label>
                    <input type="password" id="registerPassword" name="password" required minlength="6">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirmar Contraseña</label>
                    <input type="password" id="confirmPassword" name="confirm_password" required minlength="6">
                </div>
                <button type="submit" class="auth-btn">Registrarse</button>
            </form>
            
            <div class="back-link">
                <a href="../index.php">← Volver al inicio</a>
            </div>
        </div>
    </div>

    <script>
        // Cambiar entre tabs
        document.querySelectorAll('.auth-tab').forEach(tab => {
            tab.addEventListener('click', function() {
                const targetTab = this.dataset.tab;
                
                // Cambiar tab activo
                document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
                this.classList.add('active');
                
                // Cambiar formulario activo
                document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));
                document.getElementById(targetTab + 'Form').classList.add('active');
                
                // Limpiar alertas
                hideAlert();
            });
        });
        
        // Manejar login
        document.getElementById('loginForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData();
            formData.append('login', '1');
            formData.append('email', document.getElementById('loginEmail').value);
            formData.append('password', document.getElementById('loginPassword').value);
            
            try {
                const response = await fetch('auth.php', {
                    method: 'POST',
                    body: formData
                });
                
                const result = await response.json();
                
                if (result.success) {
                    showAlert(result.message, 'success');
                    setTimeout(() => {
                        window.location.href = result.redirect || 'dashboard.php';
                    }, 1000);
                } else {
                    showAlert(result.message, 'error');
                }
            } catch (error) {
                showAlert('Error de conexión', 'error');
            }
        });
        
        // Manejar registro
        document.getElementById('registerForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData();
            formData.append('registro', '1');
            formData.append('email', document.getElementById('registerEmail').value);
            formData.append('password', document.getElementById('registerPassword').value);
            formData.append('confirm_password', document.getElementById('confirmPassword').value);
            
            try {
                const response = await fetch('auth.php', {
                    method: 'POST',
                    body: formData
                });
                
                const result = await response.json();
                
                if (result.success) {
                    showAlert(result.message, 'success');
                    this.reset();
                    // Cambiar a tab de login después de registro exitoso
                    setTimeout(() => {
                        document.querySelector('.auth-tab[data-tab="login"]').click();
                    }, 2000);
                } else {
                    showAlert(result.message, 'error');
                }
            } catch (error) {
                showAlert('Error de conexión', 'error');
            }
        });
        
        function showAlert(message, type) {
            const alert = document.getElementById('alert');
            alert.textContent = message;
            alert.className = 'alert ' + type;
            alert.style.display = 'block';
        }
        
        function hideAlert() {
            document.getElementById('alert').style.display = 'none';
        }
    </script>
</body>
</html>