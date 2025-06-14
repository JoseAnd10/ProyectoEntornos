<?php
require_once 'config.php';

// Manejar solicitudes POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    // PROCESO DE REGISTRO
    if (isset($_POST['registro'])) {
        $email = trim($_POST['email']);
        $password = $_POST['password'];
        $confirm_password = $_POST['confirm_password'];
        
        $response = array();
        
        // Validaciones
        if (empty($email) || empty($password) || empty($confirm_password)) {
            $response['success'] = false;
            $response['message'] = 'Todos los campos son obligatorios';
        } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $response['success'] = false;
            $response['message'] = 'El email no es válido';
        } elseif (strlen($password) < 6) {
            $response['success'] = false;
            $response['message'] = 'La contraseña debe tener al menos 6 caracteres';
        } elseif ($password !== $confirm_password) {
            $response['success'] = false;
            $response['message'] = 'Las contraseñas no coinciden';
        } else {
            // Verificar si el email ya existe
            $stmt = $pdo->prepare("SELECT id FROM usuarios WHERE correo = ?");
            $stmt->execute([$email]);
            
            if ($stmt->rowCount() > 0) {
                $response['success'] = false;
                $response['message'] = 'Este email ya está registrado';
            } else {
                // Registrar usuario
                $hashed_password = password_hash($password, PASSWORD_DEFAULT);
                $stmt = $pdo->prepare("INSERT INTO usuarios (correo, contrasena) VALUES (?, ?)");
                
                if ($stmt->execute([$email, $hashed_password])) {
                    $response['success'] = true;
                    $response['message'] = 'Registro exitoso. Ya puedes iniciar sesión.';
                } else {
                    $response['success'] = false;
                    $response['message'] = 'Error al registrar usuario';
                }
            }
        }
        
        header('Content-Type: application/json');
        echo json_encode($response);
        exit;
    }
    
    // PROCESO DE LOGIN
    if (isset($_POST['login'])) {
        $email = trim($_POST['email']);
        $password = $_POST['password'];
        
        $response = array();
        
        if (empty($email) || empty($password)) {
            $response['success'] = false;
            $response['message'] = 'Email y contraseña son obligatorios';
        } else {
            $stmt = $pdo->prepare("SELECT * FROM usuarios WHERE correo = ?");
            $stmt->execute([$email]);
            $user = $stmt->fetch();
            
            if ($user) {
                // Verificar contraseña (compatible con contraseñas sin hash)
                if (password_verify($password, $user['contrasena']) || $password === $user['contrasena']) {
                    $_SESSION['usuario_id'] = $user['id'];
                    $_SESSION['usuario_email'] = $user['correo'];
                    
                    $response['success'] = true;
                    $response['message'] = 'Login exitoso';
                    $response['redirect'] = 'dashboard.php';
                } else {
                    $response['success'] = false;
                    $response['message'] = 'Contraseña incorrecta';
                }
            } else {
                $response['success'] = false;
                $response['message'] = 'Usuario no encontrado';
            }
        }
        
        header('Content-Type: application/json');
        echo json_encode($response);
        exit;
    }
}

// CERRAR SESIÓN
if (isset($_GET['logout'])) {
    logout();
}
?>