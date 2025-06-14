<?php
require_once 'config.php';

// Verificar si está logueado
if (!isLoggedIn()) {
    header("Location: login.php");
    exit;
}

$user = getCurrentUser();

// Obtener tickets del usuario
$stmt = $pdo->prepare("
    SELECT mt.*, t.contenido, t.tipo, t.fecha 
    FROM mensaje_tickets mt 
    LEFT JOIN tickets t ON mt.ticket_id = t.ticket_id 
    WHERE mt.usuario_id = ? 
    ORDER BY mt.fecha_creacion DESC
");
$stmt->execute([$user['id']]);
$tickets = $stmt->fetchAll();
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Usuario - LibrosFab</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
        .dashboard-header {
            background: #000; /* negro */
            color: #fff; /* blanco */
            padding: 1rem 0;
            margin-bottom: 2rem;
        }

        .dashboard-nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .dashboard-content {
            padding: 2rem 0;
        }

        .dashboard-grid {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 2rem;
            margin-bottom: 3rem;
        }

        .dashboard-card {
            background: #fff; /* blanco */
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .dashboard-card h3 {
            color: #000; /* negro */
            margin-bottom: 1rem;
        }

        .tickets-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
        }

        .tickets-table th,
        .tickets-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .tickets-table th {
            background: #f4f4f4; /* blanco grisáceo */
            font-weight: 600;
        }

        .status-badge {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
        }

        /* Estado de tickets con gama blanco-negro-amarillo */
        .status-abierto {
            background: #fffbea; /* amarillo claro */
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

        .priority-badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: 500;
        }

        /* Prioridad en gama blanco-negro-amarillo */
        .priority-baja {
            background: #f9f9f9;
            color: #000;
        }
        .priority-media {
            background: #fff5b1;
            color: #000;
        }
        .priority-alta {
            background: #fff000;
            color: #000;
        }
        .priority-urgente {
            background: #ffdb00;
            color: #000;
        }

        .logout-btn {
            background: #000;
            color: #FFD700; /* amarillo */
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 5px;
            transition: background 0.3s;
        }

        .logout-btn:hover {
            background: #333;
        }

        .chat-btn {
            background: #FFD700; /* amarillo */
            color: #000;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 0.8rem;
            transition: background 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 0.25rem;
        }

        .chat-btn:hover {
            background: #e6c200;
            color: #000;
        }

        .chat-btn.disabled {
            background: #ccc;
            cursor: not-allowed;
            opacity: 0.6;
        }

        .no-tickets {
            text-align: center;
            color: #555;
            font-style: italic;
            padding: 2rem;
        }

        .actions-cell {
            white-space: nowrap;
        }

        @media (max-width: 768px) {
            .dashboard-grid {
                grid-template-columns: 1fr;
            }

            .dashboard-nav {
                flex-direction: column;
                gap: 1rem;
            }

            .tickets-table {
                font-size: 0.9rem;
            }

            .tickets-table th:nth-child(4),
            .tickets-table td:nth-child(4) {
                display: none;
            }
        }

    </style>
</head>
<body>
    <div class="dashboard-header">
        <div class="container">
            <div class="dashboard-nav">
                <div class="logo">LibrosFab</div>
                <div class="user-info">
                    <span>Bienvenido, <?php echo htmlspecialchars($user['correo']); ?></span>
                    <a href="auth.php?logout=1" class="logout-btn">Cerrar Sesión</a>
                </div>
            </div>
        </div>
    </div>

    <div class="container dashboard-content">
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <h3>Información de Cuenta</h3>
                <p><strong>Email:</strong> <?php echo htmlspecialchars($user['correo']); ?></p>
                <p><strong>Fecha de registro:</strong> <?php echo date('d/m/Y H:i', strtotime($user['fecha_registro'])); ?></p>
                <p><strong>Total de tickets:</strong> <?php echo count($tickets); ?></p>
                
                <div style="margin-top: 2rem;">
                    <a href="../index.php" class="btn">Volver al sitio web</a>
                </div>
            </div>
            
            <div class="dashboard-card">
                <h3>Acciones Rápidas</h3>
                <p>Desde aquí puedes crear tickets.</p>
                
                <div style="margin-top: 1.5rem;">
                    <a href="../index.php#contacto" class="btn" style="margin-right: 1rem;">Nuevo ticket</a>
                </div>
            </div>
        </div>
        
        <div class="dashboard-card" id="tickets">
            <h3>Historial de Tickets</h3>
            
            <?php if (empty($tickets)): ?>
                <div class="no-tickets">
                    <p>No tienes tickets registrados aún.</p>
                    <a href="../index.php#contacto" class="btn">Crear primera solicitud</a>
                </div>
            <?php else: ?>
                <table class="tickets-table">
                    <thead>
                        <tr>
                            <th>Ticket ID</th>
                            <th>Asunto</th>
                            <th>Estado</th>
                            <th>Prioridad</th>
                            <th>Fecha</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($tickets as $ticket): ?>
                        <tr>
                            <td><?php echo htmlspecialchars($ticket['ticket_id']); ?></td>
                            <td><?php echo htmlspecialchars($ticket['asunto']); ?></td>
                            <td>
                                <span class="status-badge status-<?php echo $ticket['estado']; ?>">
                                    <?php echo ucfirst(str_replace('_', ' ', $ticket['estado'])); ?>
                                </span>
                            </td>
                            <td>
                                <span class="priority-badge priority-<?php echo $ticket['prioridad']; ?>">
                                    <?php echo ucfirst($ticket['prioridad']); ?>
                                </span>
                            </td>
                            <td><?php echo date('d/m/Y H:i', strtotime($ticket['fecha_creacion'])); ?></td>
                            <td class="actions-cell">
                                <a href="chat_ticket.php?ticket=<?php echo urlencode($ticket['ticket_id']); ?>" 
                                   class="chat-btn <?php echo $ticket['estado'] === 'cerrado' ? 'disabled' : ''; ?>"
                                   <?php echo $ticket['estado'] === 'cerrado' ? 'onclick="return false;"' : ''; ?>>
                                    💬 Chat
                                </a>
                            </td>
                        </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
                
                <div style="margin-top: 1.5rem; padding: 1rem; background: #f8f9fa; border-radius: 8px; font-size: 0.9rem; color: #666;">
                    <strong>💡 Consejos:</strong>
                    <ul style="margin: 0.5rem 0 0 1.5rem; padding: 0;">
                        <li>Haz clic en "💬 Chat" para enviar mensajes adicionales sobre tu ticket</li>
                        <li>Los tickets cerrados no permiten nuevos mensajes</li>
                        <li>Recibirás respuestas del equipo de soporte directamente en el chat</li>
                    </ul>
                </div>
            <?php endif; ?>
        </div>
    </div>
</body>
</html>