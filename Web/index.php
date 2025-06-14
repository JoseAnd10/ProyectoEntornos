<?php
require_once 'php/config.php';

// Procesar formulario de contacto si viene por POST
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['contacto'])) {
    $response = array();
    
    // VERIFICAR QUE EL USUARIO ESTÉ LOGUEADO
    if (!isLoggedIn()) {
        $response['success'] = false;
        $response['message'] = 'Debes iniciar sesión para enviar una solicitud de cotización.';
        $response['requireLogin'] = true;
        header('Content-Type: application/json');
        echo json_encode($response);
        exit;
    }
    
    $nombre = trim($_POST['nombre']);
    $email = trim($_POST['email']);
    $telefono = trim($_POST['telefono']);
    $asunto = trim($_POST['asunto']);
    $mensaje = trim($_POST['mensaje']);
    
    if (empty($nombre) || empty($email) || empty($asunto) || empty($mensaje)) {
        $response['success'] = false;
        $response['message'] = 'Por favor, complete todos los campos obligatorios.';
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response['success'] = false;
        $response['message'] = 'Por favor, ingrese un email válido.';
    } else {
        try {
            // Generar ticket ID único
            $ticket_id = 'TK-' . date('Y') . '-' . sprintf('%05d', rand(10000, 99999));
            
            // Obtener usuario actual (ya sabemos que está logueado)
            $currentUser = getCurrentUser();
            $usuario_id = $currentUser['id'];
            
            // Insertar en tabla tickets
            $contenido = "Asunto: " . $asunto . "\n\nDescripción: " . $mensaje;
            if (!empty($telefono)) {
                $contenido .= "\n\nTeléfono: " . $telefono;
            }
            
            $stmt = $pdo->prepare("INSERT INTO tickets (ticket_id, contenido, tipo) VALUES (?, ?, ?)");
            $stmt->execute([$ticket_id, $contenido, 'consulta']);
            
            // Insertar en mensaje_tickets
            $stmt = $pdo->prepare("
                INSERT INTO mensaje_tickets (nombre_cliente, email_cliente, asunto, descripcion, usuario_id, ticket_id) 
                VALUES (?, ?, ?, ?, ?, ?)
            ");
            $stmt->execute([
                $nombre, 
                $email, 
                $asunto,  // Usar el asunto del formulario
                $mensaje, 
                $usuario_id, 
                $ticket_id
            ]);
            
            $response['success'] = true;
            $response['message'] = 'Gracias por contactarnos. Te responderemos pronto.';
            $response['ticket_id'] = $ticket_id;
            
        } catch (Exception $e) {
            $response['success'] = false;
            $response['message'] = 'Error al procesar la solicitud. Inténtelo nuevamente.';
        }
    }
    
    header('Content-Type: application/json');
    echo json_encode($response);
    exit;
}

$currentUser = getCurrentUser();
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LibrosFab - Fábrica de Libros</title>
    <link rel="stylesheet" href="css/style.css">
    <style>

        header {
            background-color: #000 !important;
        }

        header .nav-links a {
            color: white; /* Opcional: para que los enlaces se vean sobre fondo negro */
        }
        /* Estilos adicionales para el hero con imagen de fondo */
        .hero {
            background-image: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url('imagenes/fabrica.jpg');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            background-repeat: no-repeat;
            position: relative;
            overflow: hidden;
        }

        /* Fallback para cuando no hay imagen */
        .hero.no-image {
            background: linear-gradient(135deg, #000 0%, #FFD700 100%);
        }

        /* Overlay adicional para mejor legibilidad del texto */
        .hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.2);
            z-index: 1;
        }

        .hero-content {
            position: relative;
            z-index: 2;
        }

        /* Mejora en el texto para mayor contraste */
        .hero h1 {
            text-shadow: 2px 2px 4px rgba(255, 255, 255, 0.2);
            color: #FFD700;
        }

        .hero p {
            text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.2);
            color: #fff;
        }

        /* Efecto parallax suave */
        @media (min-width: 768px) {
            .hero {
                background-attachment: fixed;
            }
        }

        /* Responsive - desactivar parallax en móviles */
        @media (max-width: 767px) {
            .hero {
                background-attachment: scroll;
            }
        }

    </style>
</head>
<body>
    <!-- Header -->
    <header>
        <nav class="container">
            <div class="logo">LibrosFab</div>
            <ul class="nav-links">
                <li><a href="#inicio">Inicio</a></li>
                <li><a href="#servicios">Servicios</a></li>
                <li><a href="#nosotros">Nosotros</a></li>
                <li><a href="#contacto">Contacto</a></li>
                <?php if (isLoggedIn()): ?>
                    <li><a href="php/dashboard.php">Panel</a></li>
                    <li><a href="php/auth.php?logout=1" style="color: #e74c3c;">Cerrar Sesión</a></li>
                <?php else: ?>
                    <li><a href="php/login.php">Iniciar Sesión</a></li>
                <?php endif; ?>
            </ul>
        </nav>
    </header>

    <!-- Hero Section con imagen de fondo -->
    <section id="inicio" class="hero">
        <div class="hero-content">
            <h1>LibrosFab</h1>
            <p>Impresión y encuadernación profesional de libros</p>
            <?php if (isLoggedIn()): ?>
                <p style="margin-bottom: 1rem; font-size: 1rem; opacity: 0.9;">
                    Bienvenido, <?php echo htmlspecialchars($currentUser['correo']); ?>
                </p>
            <?php endif; ?>
            <a href="#contacto" class="btn">Contactanos</a>
        </div>
    </section>

    <!-- Services -->
    <section id="servicios" class="container">
        <h2 class="section-title">Nuestros Servicios</h2>
        <div class="services-grid">
            <div class="service-card">
                <h3>Impresión Digital</h3>
                <p>Impresión de alta calidad para tiradas cortas y medianas. Ideal para autopublicación y proyectos personalizados.</p>
            </div>
            <div class="service-card">
                <h3>Impresión Offset</h3>
                <p>Para grandes tiradas con excelente calidad y costos optimizados. Perfecto para editoriales y distribuidores.</p>
            </div>
            <div class="service-card">
                <h3>Encuadernación</h3>
                <p>Diversos tipos de encuadernación: rústica, tapa dura, espiral y más. Acabados profesionales garantizados.</p>
            </div>
            <div class="service-card">
                <h3>Diseño Editorial</h3>
                <p>Servicio completo de diseño de portadas, maquetación interior y preparación de archivos para impresión.</p>
            </div>
        </div>
    </section>

    <!-- About -->
    <section id="nosotros">
        <div class="container">
            <h2 class="section-title">Sobre Nosotros</h2>
            <p style="text-align: center; max-width: 800px; margin: 0 auto; font-size: 1.1rem; line-height: 1.8;">
                Con más de 15 años de experiencia en el sector editorial, somos una fábrica especializada en la impresión y encuadernación de libros. 
                Trabajamos con autores independientes, editoriales y distribuidores, ofreciendo soluciones integrales desde el diseño hasta el producto final. 
                Nuestro compromiso es la calidad, puntualidad y precios competitivos.
            </p>
        </div>
    </section>

    <!-- Contact -->
    <section id="contacto" class="container">
        <h2 class="section-title">Contacto</h2>
        <div class="contact-content">
            <div class="contact-form">
                <h3>Crear Ticket</h3>
                
                <?php if (!isLoggedIn()): ?>
                    <!-- Mensaje de login requerido -->
                    <div style="background: #fff3cd; padding: 2rem; border-radius: 8px; margin-bottom: 1.5rem; border-left: 4px solid #ffc107; text-align: center;">
                        <div style="font-size: 3rem; margin-bottom: 1rem;">🔒</div>
                        <h4 style="color: #856404; margin-bottom: 1rem;">Inicia Sesión Requerido</h4>
                        <p style="margin: 0 0 1.5rem 0; color: #856404; font-size: 1rem;">
                            Para solicitar crear y hacer seguimiento de tu ticket, necesitas tener una cuenta en nuestro sistema.
                        </p>
                        <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                            <a href="php/login.php" class="btn" style="background: #28a745; text-decoration: none;">
                                Iniciar Sesión
                            </a>
                            <a href="php/login.php" class="btn" style="background: #007bff; text-decoration: none;">
                                Crear Cuenta
                            </a>
                        </div>
                        <p style="margin: 1rem 0 0 0; font-size: 0.9rem; color: #6c757d;">
                            ¿No tienes cuenta? Puedes registrarte de forma gratuita en segundos.
                        </p>
                    </div>
                    
                    <!-- Formulario deshabilitado -->
                    <form id="contactForm" style="opacity: 0.5; pointer-events: none;">
                        <input type="hidden" name="contacto" value="1">
                        <div class="form-group">
                            <label for="nombre">Nombre *</label>
                            <input type="text" id="nombre" name="nombre" disabled>
                        </div>
                        <div class="form-group">
                            <label for="email">Email *</label>
                            <input type="email" id="email" name="email" disabled>
                        </div>
                        <div class="form-group">
                            <label for="asunto">Asunto *</label>
                            <input type="text" id="asunto" name="asunto" placeholder="Ej: Cotización para impresión de 100 libros" disabled>
                        </div>
                        <div class="form-group">
                            <label for="telefono">Teléfono</label>
                            <input type="tel" id="telefono" name="telefono" disabled>
                        </div>
                        <div class="form-group">
                            <label for="mensaje">Detalles del proyecto *</label>
                            <textarea id="mensaje" name="mensaje" placeholder="Describa su proyecto: tipo de libro, cantidad, formato, etc." disabled></textarea>
                        </div>
                        <button type="submit" class="btn" disabled style="background: #6c757d; cursor: not-allowed;">
                            Formulario Bloqueado - Inicia Sesión
                        </button>
                    </form>
                <?php else: ?>
                    <!-- Usuario logueado - mostrar formulario activo -->
                    <div style="background: #d4edda; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem; border-left: 4px solid #28a745;">
                        <p style="margin: 0; font-size: 0.9rem; color: #155724;">
                            ✅ <strong>Sesión activa:</strong> Tu solicitud será vinculada a tu cuenta para mejor seguimiento.
                        </p>
                    </div>
                    
                    <form id="contactForm">
                        <input type="hidden" name="contacto" value="1">
                        <div class="form-group">
                            <label for="nombre">Nombre *</label>
                            <input type="text" id="nombre" name="nombre" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email *</label>
                            <input type="email" id="email" name="email" required 
                                   value="<?php echo htmlspecialchars($currentUser['correo']); ?>" 
                                   readonly style="background: #f8f9fa;">
                        </div>
                        <div class="form-group">
                            <label for="asunto">Asunto *</label>
                            <input type="text" id="asunto" name="asunto" required 
                                   placeholder="Ej: Cotización para impresión de 100 libros"
                                   maxlength="200">
                            <small style="color: #666; font-size: 0.85rem;">
                                Describe brevemente tu solicitud (máximo 200 caracteres)
                            </small>
                        </div>
                        <div class="form-group">
                            <label for="telefono">Teléfono</label>
                            <input type="tel" id="telefono" name="telefono">
                        </div>
                        <div class="form-group">
                            <label for="mensaje">Detalles del proyecto *</label>
                            <textarea id="mensaje" name="mensaje" placeholder="Describa su proyecto: tipo de libro, cantidad, formato, etc." required></textarea>
                        </div>
                        <button type="submit" class="btn">Enviar Solicitud</button>
                    </form>
                <?php endif; ?>
            </div>
            
            <div class="contact-info">
                <h3>Información de Contacto</h3>
                <p><strong>📍 Dirección:</strong><br>Calle Industrial 123, Polígono Norte<br>Ciudad, CP 12345</p>
                <p><strong>📞 Teléfono:</strong> +34 987 654 321</p>
                <p><strong>✉️ Email:</strong> info@librosfab.com</p>
                <p><strong>🕒 Horario:</strong><br>Lunes a Viernes: 8:00 - 18:00<br>Sábados: 9:00 - 14:00</p>
                
                <?php if (isLoggedIn()): ?>
                    <div style="margin-top: 2rem; padding: 1rem; background: #e8f5e8; border-radius: 8px; border-left: 4px solid #4caf50;">
                        <p style="margin: 0; font-size: 0.9rem;">
                            ✅ <strong>Usuario registrado:</strong> Podrás hacer seguimiento de tu solicitud desde tu<a href="php/dashboard.php" style="color: #2e7d32; text-decoration: none; font-weight: 500;">panel de usuario</a>.
                        </p>
                    </div>
                <?php else: ?>
                    <div style="margin-top: 2rem; padding: 1rem; background: #fff3cd; border-radius: 8px; border-left: 4px solid #ffc107;">
                        <p style="margin: 0; font-size: 0.9rem; color: #856404;">
                            ℹ️ <strong>¿Necesitas ayuda urgente?</strong> Llámanos directamente al teléfono indicado arriba.
                        </p>
                    </div>
                <?php endif; ?>
            </div>
        </div>
    </section>

 <!-- Footer -->
    <footer class="footer">
        <div class="footer-content">
            <!-- Navegación -->
            <div class="footer-section">
                <h3>Navegación</h3>
                <ul>
                    <li><a href="#inicio">Inicio</a></li>
                    <li><a href="#servicios">Servicios</a></li>
                    <li><a href="#nosotros">Nosotros</a></li>
                    <li><a href="#contacto">Contacto</a></li>
                </ul>
            </div>

            <!-- Contacto -->
            <div class="footer-section">
                <h3>Contacto</h3>
                <p>📍 Calle Industrial 123<br>Polígono Norte, Ciudad</p>
                <p>📞 +34 987 654 321</p>
                <p>✉️ info@librosfab.com</p>
            </div>
        </div>
    </footer>

    <!-- Modal -->
    <div id="modal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h3 id="modalTitle">¡Mensaje Enviado!</h3>
            <p id="modalMessage">Gracias por contactarnos. Te responderemos pronto a tu ticket.</p>
            <p id="modalTicket" style="margin-top: 1rem; font-size: 0.9rem; color: #666; display: none;">
                <strong>Número de ticket:</strong> <span id="ticketId"></span>
            </p>
            <div id="modalActions" style="margin-top: 1.5rem; text-align: center; display: none;">
                <a href="php/dashboard.php" class="btn" style="text-decoration: none;">Ver mis tickets</a>
            </div>
        </div>
    </div>

    <!-- Modal de Login Requerido -->
    <div id="loginModal" class="modal">
        <div class="modal-content">
            <span class="close-login">&times;</span>
            <div style="text-align: center;">
                <div style="font-size: 4rem; margin-bottom: 1rem;">🔐</div>
                <h3>Inicia Sesión Requerido</h3>
                <p>Para crear un ticket necesitas tener una cuenta en nuestro sistema.</p>
                <div style="margin: 2rem 0; display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                    <a href="php/login.php" class="btn" style="background: #28a745; text-decoration: none;">
                        Iniciar Sesión
                    </a>
                    <a href="php/login.php" class="btn" style="background: #007bff; text-decoration: none;">
                        Crear Cuenta Gratuita
                    </a>
                </div>
                <p style="font-size: 0.9rem; color: #666;">
                    El registro es gratuito y toma menos de 1 minuto.
                </p>
            </div>
        </div>
    </div>

    <script>
        // Detectar si la imagen de fondo existe
        function checkBackgroundImage() {
            const hero = document.querySelector('.hero');
            const img = new Image();
            
            img.onload = function() {
                // La imagen se cargó correctamente
                console.log('Imagen de fondo cargada correctamente');
            };
            
            img.onerror = function() {
                // Error al cargar la imagen, usar fallback
                console.log('No se pudo cargar la imagen de fondo, usando fallback');
                hero.classList.add('no-image');
                hero.style.backgroundImage = 'none';
            };
            
            // Extraer la URL de la imagen del CSS
            const bgImage = window.getComputedStyle(hero).backgroundImage;
            const imageUrl = bgImage.match(/url\(["']?([^"']*)["']?\)/);
            
            if (imageUrl && imageUrl[1]) {
                img.src = imageUrl[1];
            } else {
                hero.classList.add('no-image');
            }
        }

        // Smooth scrolling for navigation links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });

        // Contador de caracteres para el asunto
        const asuntoInput = document.getElementById('asunto');
        if (asuntoInput) {
            asuntoInput.addEventListener('input', function() {
                const maxLength = 200;
                const currentLength = this.value.length;
                const remaining = maxLength - currentLength;
                
                // Buscar o crear el contador
                let counter = this.parentNode.querySelector('.char-counter');
                if (!counter) {
                    counter = document.createElement('small');
                    counter.className = 'char-counter';
                    counter.style.cssText = 'display: block; text-align: right; margin-top: 0.25rem; font-size: 0.8rem;';
                    this.parentNode.appendChild(counter);
                }
                
                counter.textContent = `${currentLength}/${maxLength} caracteres`;
                counter.style.color = remaining < 20 ? '#e74c3c' : '#666';
            });
        }

        // Form handling
        document.getElementById('contactForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData(this);
            
            try {
                const response = await fetch('index.php', {
                    method: 'POST',
                    body: formData
                });
                
                const result = await response.json();
                
                // Si requiere login, mostrar modal de login
                if (result.requireLogin) {
                    showLoginModal();
                    return;
                }
                
                if (result.success) {
                    document.getElementById('modalTitle').textContent = '¡Solicitud Enviada!';
                    document.getElementById('modalMessage').textContent = result.message;
                    
                    if (result.ticket_id) {
                        document.getElementById('ticketId').textContent = result.ticket_id;
                        document.getElementById('modalTicket').style.display = 'block';
                        document.getElementById('modalActions').style.display = 'block';
                    }
                    
                    showModal();
                    this.reset();
                    
                    // Pre-rellenar el email si el usuario está logueado
                    <?php if (isLoggedIn()): ?>
                    document.getElementById('email').value = '<?php echo htmlspecialchars($currentUser['correo']); ?>';
                    <?php endif; ?>
                } else {
                    document.getElementById('modalTitle').textContent = 'Error';
                    document.getElementById('modalMessage').textContent = result.message;
                    document.getElementById('modalTicket').style.display = 'none';
                    document.getElementById('modalActions').style.display = 'none';
                    showModal();
                }
            } catch (error) {
                document.getElementById('modalTitle').textContent = 'Error';
                document.getElementById('modalMessage').textContent = 'Error de conexión. Inténtelo nuevamente.';
                document.getElementById('modalTicket').style.display = 'none';
                document.getElementById('modalActions').style.display = 'none';
                showModal();
            }
        });

        // Modal functionality
        function showModal() {
            document.getElementById('modal').style.display = 'block';
        }

        function showLoginModal() {
            document.getElementById('loginModal').style.display = 'block';
        }

        document.querySelector('.close').addEventListener('click', function() {
            document.getElementById('modal').style.display = 'none';
        });

        document.querySelector('.close-login').addEventListener('click', function() {
            document.getElementById('loginModal').style.display = 'none';
        });

        window.addEventListener('click', function(e) {
            const modal = document.getElementById('modal');
            const loginModal = document.getElementById('loginModal');
            if (e.target === modal) {
                modal.style.display = 'none';
            }
            if (e.target === loginModal) {
                loginModal.style.display = 'none';
            }
        });

        // Header scroll effect
        window.addEventListener('scroll', function() {
            const header = document.querySelector('header');
            if (window.scrollY > 100) {
                header.style.background = 'rgba(44, 62, 80, 0.95)';
            } else {
                header.style.background = '#2c3e50';
            }
        });

        // Simple animation on scroll
        function animateOnScroll() {
            const elements = document.querySelectorAll('.service-card');
            elements.forEach(element => {
                const elementTop = element.getBoundingClientRect().top;
                const elementVisible = 150;
                
                if (elementTop < window.innerHeight - elementVisible) {
                    element.style.opacity = '1';
                    element.style.transform = 'translateY(0)';
                }
            });
        }

        // Initialize animations
        document.querySelectorAll('.service-card').forEach(card => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(20px)';
            card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        });

        window.addEventListener('scroll', animateOnScroll);
        
        // Run animation on load
        window.addEventListener('load', function() {
            animateOnScroll();
            checkBackgroundImage(); // Verificar imagen de fondo al cargar
        });
    </script>
</body>
</html>