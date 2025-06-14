<?php
require_once 'config.php';

// Verificar si está logueado
if (!isLoggedIn()) {
    header("Location: login.php");
    exit;
}

$user = getCurrentUser();

// Obtener ticket ID desde la URL
$ticket_id = $_GET['ticket'] ?? '';

if (empty($ticket_id)) {
    header("Location: dashboard.php");
    exit;
}

// Verificar que el ticket pertenece al usuario
$stmt = $pdo->prepare("
    SELECT mt.*, t.contenido, t.tipo, t.fecha 
    FROM mensaje_tickets mt 
    LEFT JOIN tickets t ON mt.ticket_id = t.ticket_id 
    WHERE mt.ticket_id = ? AND mt.usuario_id = ?
");
$stmt->execute([$ticket_id, $user['id']]);
$ticket = $stmt->fetch();

if (!$ticket) {
    header("Location: dashboard.php");
    exit;
}

// Procesar nuevo mensaje del chat
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['nuevo_mensaje'])) {
    $nuevo_mensaje = trim($_POST['mensaje']);
    
    $response = array();
    
    if (empty($nuevo_mensaje)) {
        $response['success'] = false;
        $response['message'] = 'El mensaje no puede estar vacío';
    } else {
        // Obtener el historial actual del chat
        $chat_actual = [];
        if (!empty($ticket['descripcion'])) {
            // Intentar decodificar como JSON, si falla, es el mensaje original
            $decoded = json_decode($ticket['descripcion'], true);
            if (json_last_error() === JSON_ERROR_NONE && is_array($decoded)) {
                $chat_actual = $decoded;
            } else {
                // Es el mensaje original, convertirlo al formato de chat
                $chat_actual = [
                    [
                        'autor' => 'cliente',
                        'mensaje' => $ticket['descripcion'],
                        'fecha' => $ticket['fecha_creacion']
                    ]
                ];
            }
        }
        
        // Agregar nuevo mensaje
        $chat_actual[] = [
            'autor' => 'cliente',
            'mensaje' => $nuevo_mensaje,
            'fecha' => date('Y-m-d H:i:s')
        ];
        
        // Guardar el chat actualizado
        $chat_json = json_encode($chat_actual, JSON_UNESCAPED_UNICODE);
        
        $stmt = $pdo->prepare("UPDATE mensaje_tickets SET descripcion = ? WHERE ticket_id = ? AND usuario_id = ?");
        
        if ($stmt->execute([$chat_json, $ticket_id, $user['id']])) {
            $response['success'] = true;
            $response['message'] = 'Mensaje enviado correctamente';
        } else {
            $response['success'] = false;
            $response['message'] = 'Error al enviar el mensaje';
        }
    }
    
    header('Content-Type: application/json');
    echo json_encode($response);
    exit;
}

// Obtener mensajes del chat
$mensajes_chat = [];
if (!empty($ticket['descripcion'])) {
    $decoded = json_decode($ticket['descripcion'], true);
    if (json_last_error() === JSON_ERROR_NONE && is_array($decoded)) {
        $mensajes_chat = $decoded;
    } else {
        // Es el mensaje original
        $mensajes_chat = [
            [
                'autor' => 'cliente',
                'mensaje' => $ticket['descripcion'],
                'fecha' => $ticket['fecha_creacion']
            ]
        ];
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat - Ticket <?php echo htmlspecialchars($ticket_id); ?> - LibrosFab</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
        .chat-container {
            max-width: 1200px;
            margin: 2rem auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .chat-header {
            background: #000;
            color: white;
            padding: 1.5rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .ticket-info {
            flex: 1;
        }

        .ticket-info h2 {
            margin: 0 0 0.5rem 0;
            font-size: 1.2rem;
        }

        .ticket-info p {
            margin: 0;
            opacity: 0.8;
            font-size: 0.9rem;
        }

        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
        }

        .status-abierto {
            background: #fffbea;
            color: #000;
        }
        .status-en_proceso {
            background: #fff3b0;
            color: #000;
        }
        .status-resuelto {
            background: #fdfdfd;
            color: #000;
        }
        .status-cerrado {
            background: #eeeeee;
            color: #000;
        }

        .chat-messages {
            height: 600px;
            overflow-y: auto;
            padding: 1rem;
            background: #fcfcfc;
        }

        .mensaje {
            margin-bottom: 1rem;
            display: flex;
            flex-direction: column;
        }

        .mensaje.cliente {
            align-items: flex-end;
        }

        .mensaje.admin {
            align-items: flex-start;
        }

        .mensaje-bubble {
            max-width: 70%;
            padding: 12px 16px;
            border-radius: 18px;
            word-wrap: break-word;
            position: relative;
        }

        .mensaje.cliente .mensaje-bubble {
            background: #FFD700;
            color: #000;
            border-bottom-right-radius: 4px;
        }

        .mensaje.admin .mensaje-bubble {
            background: white;
            color: #111;
            border: 1px solid #ccc;
            border-bottom-left-radius: 4px;
        }

        .mensaje-info {
            font-size: 0.75rem;
            color: #444;
            margin-top: 4px;
            padding: 0 4px;
        }

        .mensaje.cliente .mensaje-info {
            text-align: right;
        }

        .mensaje.admin .mensaje-info {
            text-align: left;
        }

        .chat-input {
            padding: 1rem;
            background: white;
            border-top: 1px solid #ddd;
        }

        .input-form {
            display: flex;
            gap: 1rem;
            align-items: flex-end;
        }

        .input-mensaje {
            flex: 1;
            min-height: 40px;
            max-height: 100px;
            padding: 10px 15px;
            border: 2px solid #ccc;
            border-radius: 20px;
            resize: none;
            font-family: inherit;
            font-size: 0.9rem;
            line-height: 1.4;
        }

        .input-mensaje:focus {
            outline: none;
            border-color: #FFD700;
        }

        .btn-enviar {
            background: #FFD700;
            color: #000;
            border: none;
            padding: 10px 20px;
            border-radius: 20px;
            cursor: pointer;
            font-weight: 500;
            transition: background 0.3s;
        }

        .btn-enviar:hover:not(:disabled) {
            background: #e6c200;
        }

        .btn-enviar:disabled {
            background: #999;
            cursor: not-allowed;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            color: white;
            text-decoration: none;
            opacity: 0.8;
            transition: opacity 0.3s;
        }

        .back-link:hover {
            opacity: 1;
            color: white;
        }

        .no-messages {
            text-align: center;
            color: #666;
            font-style: italic;
            padding: 2rem;
        }

        .alert {
            padding: 12px 16px;
            margin: 1rem;
            border-radius: 8px;
            display: none;
        }

        .alert.success {
            background: #fff9db;
            color: #444;
            border: 1px solid #fff2ac;
        }

        .alert.error {
            background: #fff0f0;
            color: #000;
            border: 1px solid #ffcccc;
        }

        @media (max-width: 768px) {
            .chat-container {
                margin: 1rem;
                height: calc(100vh - 2rem);
                display: flex;
                flex-direction: column;
            }

            .chat-messages {
                flex: 1;
                height: auto;
            }

            .chat-header {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }

            .mensaje-bubble {
                max-width: 85%;
            }
        }

    </style>
</head>
<body>
    <div class="container">
        <div class="chat-container">
            <div class="chat-header">
                <div class="ticket-info">
                    <h2><?php echo htmlspecialchars($ticket['asunto']); ?></h2>
                    <p>Ticket: <?php echo htmlspecialchars($ticket_id); ?> • 
                       Creado: <?php echo date('d/m/Y H:i', strtotime($ticket['fecha_creacion'])); ?></p>
                </div>
                <div style="display: flex; align-items: center; gap: 1rem;">
                    <span class="status-badge status-<?php echo $ticket['estado']; ?>">
                        <?php echo ucfirst(str_replace('_', ' ', $ticket['estado'])); ?>
                    </span>
                    <a href="dashboard.php" class="back-link">
                        ← Volver
                    </a>
                </div>
            </div>
            
            <div class="alert" id="alert"></div>
            
            <div class="chat-messages" id="chatMessages">
                <?php if (empty($mensajes_chat)): ?>
                    <div class="no-messages">
                        <p>No hay mensajes en este ticket aún.</p>
                    </div>
                <?php else: ?>
                    <?php foreach ($mensajes_chat as $mensaje): ?>
                        <div class="mensaje <?php echo $mensaje['autor']; ?>">
                            <div class="mensaje-bubble">
                                <?php echo nl2br(htmlspecialchars($mensaje['mensaje'])); ?>
                            </div>
                            <div class="mensaje-info">
                                <?php 
                                $fecha = new DateTime($mensaje['fecha']);
                                echo ($mensaje['autor'] === 'cliente' ? 'Tú' : 'Soporte') . ' • ' . 
                                     $fecha->format('d/m/Y H:i');
                                ?>
                            </div>
                        </div>
                    <?php endforeach; ?>
                <?php endif; ?>
            </div>
            
            <?php if ($ticket['estado'] !== 'cerrado'): ?>
            <div class="chat-input">
                <form class="input-form" id="chatForm">
                    <textarea 
                        class="input-mensaje" 
                        id="mensajeInput" 
                        placeholder="Escribe tu mensaje..."
                        rows="1"
                        maxlength="1000"></textarea>
                    <button type="submit" class="btn-enviar" id="btnEnviar">
                        Enviar
                    </button>
                </form>
                <div style="margin-top: 0.5rem; font-size: 0.8rem; color: #666; text-align: right;">
                    <span id="charCount">0</span>/1000 caracteres
                </div>
            </div>
            <?php else: ?>
            <div class="chat-input" style="text-align: center; background: #f8f9fa; color: #666;">
                <p style="margin: 0; font-style: italic;">
                    🔒 Este ticket está cerrado. No se pueden enviar más mensajes.
                </p>
            </div>
            <?php endif; ?>
        </div>
    </div>

    <script>
        const textarea = document.getElementById('mensajeInput');
        const charCount = document.getElementById('charCount');
        const btnEnviar = document.getElementById('btnEnviar');
        
        textarea.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 100) + 'px';
            
            const count = this.value.length;
            charCount.textContent = count;
            charCount.style.color = count > 950 ? '#e74c3c' : '#666';
            
            btnEnviar.disabled = count === 0 || count > 1000;
        });
        
        textarea.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                if (!btnEnviar.disabled) {
                    document.getElementById('chatForm').dispatchEvent(new Event('submit'));
                }
            }
        });
        
        document.getElementById('chatForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const mensaje = textarea.value.trim();
            if (!mensaje) return;
            
            btnEnviar.disabled = true;
            btnEnviar.textContent = 'Enviando...';
            
            const formData = new FormData();
            formData.append('nuevo_mensaje', '1');
            formData.append('mensaje', mensaje);
            
            try {
                const response = await fetch(window.location.href, {
                    method: 'POST',
                    body: formData
                });
                
                const result = await response.json();
                
                if (result.success) {
                    // Agregar mensaje al chat visualmente
                    agregarMensajeAlChat(mensaje);
                    textarea.value = '';
                    textarea.style.height = 'auto';
                    charCount.textContent = '0';
                    hideAlert();
                } else {
                    showAlert(result.message, 'error');
                }
            } catch (error) {
                showAlert('Error de conexión. Inténtalo de nuevo.', 'error');
            } finally {
                btnEnviar.disabled = false;
                btnEnviar.textContent = 'Enviar';
                textarea.focus();
            }
        });
        
        function agregarMensajeAlChat(mensaje) {
            const chatMessages = document.getElementById('chatMessages');
            
            // Remover mensaje de "no hay mensajes" si existe
            const noMessages = chatMessages.querySelector('.no-messages');
            if (noMessages) {
                noMessages.remove();
            }
            
            const now = new Date();
            const fechaFormateada = now.toLocaleDateString('es-ES') + ' ' + 
                                  now.toTimeString().slice(0, 5);
            
            const mensajeDiv = document.createElement('div');
            mensajeDiv.className = 'mensaje cliente';
            mensajeDiv.innerHTML = `
                <div class="mensaje-bubble">
                    ${mensaje.replace(/\n/g, '<br>')}
                </div>
                <div class="mensaje-info">
                    Tú • ${fechaFormateada}
                </div>
            `;
            
            chatMessages.appendChild(mensajeDiv);
            
            // Scroll al final
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
        
        function showAlert(message, type) {
            const alert = document.getElementById('alert');
            alert.textContent = message;
            alert.className = 'alert ' + type;
            alert.style.display = 'block';
            
            setTimeout(() => {
                hideAlert();
            }, 5000);
        }
        
        function hideAlert() {
            document.getElementById('alert').style.display = 'none';
        }
        
        // Scroll al final al cargar
        document.addEventListener('DOMContentLoaded', function() {
            const chatMessages = document.getElementById('chatMessages');
            chatMessages.scrollTop = chatMessages.scrollHeight;
            textarea.focus();
        });
        
        // Auto-refresh cada 30 segundos para ver nuevas respuestas del admin
        setInterval(async function() {
            try {
                const response = await fetch(window.location.href);
                const html = await response.text();
                
                // Parsear la respuesta y actualizar solo los mensajes si hay cambios
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const newMessages = doc.getElementById('chatMessages').innerHTML;
                const currentMessages = document.getElementById('chatMessages').innerHTML;
                
                if (newMessages !== currentMessages) {
                    const wasAtBottom = isScrollAtBottom();
                    document.getElementById('chatMessages').innerHTML = newMessages;
                    if (wasAtBottom) {
                        scrollToBottom();
                    }
                }
            } catch (error) {
                console.log('Error al actualizar mensajes:', error);
            }
        }, 300);
        
        function isScrollAtBottom() {
            const chatMessages = document.getElementById('chatMessages');
            return chatMessages.scrollTop >= chatMessages.scrollHeight - chatMessages.clientHeight - 50;
        }
        
        function scrollToBottom() {
            const chatMessages = document.getElementById('chatMessages');
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    </script>
</body>
</html>