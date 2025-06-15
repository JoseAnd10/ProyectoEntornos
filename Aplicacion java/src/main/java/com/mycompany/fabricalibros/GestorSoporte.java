/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor para manejar operaciones de tickets y mensajes de soporte
 * Clase responsable de toda la lógica de acceso a datos del sistema de soporte
 */
public class GestorSoporte {
    
    /**
     * Obtiene todos los tickets con información básica para mostrar en tabla
     * Los tickets se ordenan por prioridad (urgente primero) y luego por fecha
     * @return Lista de objetos TicketInfo con información básica de todos los tickets
     */
    public List<TicketInfo> obtenerTodosLosTickets() {
        List<TicketInfo> tickets = new ArrayList<>();
        String consultaSQL = """
            SELECT 
                mt.id,
                t.ticket_id,
                mt.nombre_cliente,
                mt.email_cliente,
                mt.asunto,
                mt.prioridad,
                mt.estado,
                mt.fecha_creacion,
                t.tipo
            FROM mensaje_tickets mt
            JOIN tickets t ON mt.ticket_id = t.ticket_id
            ORDER BY 
                CASE mt.prioridad 
                    WHEN 'urgente' THEN 1 
                    WHEN 'alta' THEN 2 
                    WHEN 'media' THEN 3 
                    WHEN 'baja' THEN 4 
                END,
                mt.fecha_creacion DESC
        """;
        
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consultaSQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                TicketInfo ticket = new TicketInfo();
                ticket.setId(rs.getInt("id"));
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setNombreCliente(rs.getString("nombre_cliente"));
                ticket.setEmailCliente(rs.getString("email_cliente"));
                ticket.setAsunto(rs.getString("asunto"));
                ticket.setPrioridad(rs.getString("prioridad"));
                ticket.setEstado(rs.getString("estado"));
                ticket.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                ticket.setTipo(rs.getString("tipo"));
                
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tickets: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tickets;
    }
    
    /**
     * Obtiene los detalles completos de un ticket específico para el chat
     * Incluye toda la información necesaria para mostrar en la ventana de chat
     * @param ticketId Identificador único del ticket (formato TK-YYYY-NNN)
     * @return Objeto TicketDetalle con información completa o null si no existe
     */
    public TicketDetalle obtenerDetalleTicket(String ticketId) {
        TicketDetalle detalle = null;
        String consultaSQL = """
            SELECT 
                mt.id,
                t.ticket_id,
                mt.nombre_cliente,
                mt.email_cliente,
                mt.asunto,
                mt.descripcion,
                mt.prioridad,
                mt.estado,
                mt.fecha_creacion,
                t.contenido,
                t.tipo,
                t.fecha as fecha_ticket
            FROM mensaje_tickets mt
            JOIN tickets t ON mt.ticket_id = t.ticket_id
            WHERE t.ticket_id = ?
        """;
        
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consultaSQL)) {
            
            stmt.setString(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                detalle = new TicketDetalle();
                detalle.setId(rs.getInt("id"));
                detalle.setTicketId(rs.getString("ticket_id"));
                detalle.setNombreCliente(rs.getString("nombre_cliente"));
                detalle.setEmailCliente(rs.getString("email_cliente"));
                detalle.setAsunto(rs.getString("asunto"));
                detalle.setDescripcion(rs.getString("descripcion"));
                detalle.setPrioridad(rs.getString("prioridad"));
                detalle.setEstado(rs.getString("estado"));
                detalle.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                detalle.setContenido(rs.getString("contenido"));
                detalle.setTipo(rs.getString("tipo"));
                detalle.setFechaTicket(rs.getTimestamp("fecha_ticket"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalle del ticket: " + e.getMessage());
            e.printStackTrace();
        }
        
        return detalle;
    }
    
    /**
     * Crea un nuevo ticket completo con su mensaje asociado
     * Utiliza transacciones para asegurar consistencia de datos
     * @param ticketId Identificador único del ticket
     * @param contenido Contenido principal del ticket
     * @param tipo Tipo de ticket (consulta, problema, sugerencia, urgente)
     * @param nombreCliente Nombre completo del cliente
     * @param emailCliente Email del cliente
     * @param asunto Asunto del ticket
     * @param descripcion Descripción detallada del problema
     * @param prioridad Prioridad del ticket (baja, media, alta, urgente)
     * @param usuarioId ID del usuario que crea el ticket
     * @return true si el ticket se creó exitosamente, false en caso contrario
     */
    public boolean crearNuevoTicket(String ticketId, String contenido, String tipo,
                                   String nombreCliente, String emailCliente, 
                                   String asunto, String descripcion, String prioridad, int usuarioId) {
        Connection conexion = null;
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            conexion.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar en tabla tickets
            boolean ticketCreado = insertarTicket(conexion, ticketId, contenido, tipo);
            if (!ticketCreado) {
                throw new SQLException("Error al insertar ticket");
            }
            
            // 2. Insertar en tabla mensaje_tickets
            boolean mensajeCreado = insertarMensajeTicket(conexion, nombreCliente, emailCliente, 
                asunto, descripcion, prioridad, usuarioId, ticketId);
            if (!mensajeCreado) {
                throw new SQLException("Error al insertar mensaje del ticket");
            }
            
            conexion.commit(); // Confirmar transacción
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al crear nuevo ticket: " + e.getMessage());
            e.printStackTrace();
            if (conexion != null) {
                try {
                    conexion.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            cerrarConexionTransaccional(conexion);
        }
    }
    
    /**
     * Inserta un nuevo ticket en la tabla tickets
     * Método auxiliar para crearNuevoTicket
     * @param conexion Conexión de base de datos activa
     * @param ticketId ID del ticket
     * @param contenido Contenido del ticket
     * @param tipo Tipo del ticket
     * @return true si se insertó correctamente, false en caso contrario
     * @throws SQLException si hay error en la inserción
     */
    private boolean insertarTicket(Connection conexion, String ticketId, String contenido, String tipo) throws SQLException {
        String sqlTicket = "INSERT INTO tickets (ticket_id, contenido, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement stmtTicket = conexion.prepareStatement(sqlTicket)) {
            stmtTicket.setString(1, ticketId);
            stmtTicket.setString(2, contenido);
            stmtTicket.setString(3, tipo);
            return stmtTicket.executeUpdate() > 0;
        }
    }
    
    /**
     * Inserta un mensaje de ticket en la tabla mensaje_tickets
     * Método auxiliar para crearNuevoTicket
     * @param conexion Conexión de base de datos activa
     * @param nombreCliente Nombre del cliente
     * @param emailCliente Email del cliente
     * @param asunto Asunto del ticket
     * @param descripcion Descripción del ticket
     * @param prioridad Prioridad del ticket
     * @param usuarioId ID del usuario
     * @param ticketId ID del ticket
     * @return true si se insertó correctamente, false en caso contrario
     * @throws SQLException si hay error en la inserción
     */
    private boolean insertarMensajeTicket(Connection conexion, String nombreCliente, String emailCliente,
                                         String asunto, String descripcion, String prioridad, 
                                         int usuarioId, String ticketId) throws SQLException {
        String sqlMensaje = """
            INSERT INTO mensaje_tickets 
            (nombre_cliente, email_cliente, asunto, descripcion, prioridad, usuario_id, ticket_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmtMensaje = conexion.prepareStatement(sqlMensaje)) {
            stmtMensaje.setString(1, nombreCliente);
            stmtMensaje.setString(2, emailCliente);
            stmtMensaje.setString(3, asunto);
            stmtMensaje.setString(4, descripcion);
            stmtMensaje.setString(5, prioridad);
            stmtMensaje.setInt(6, usuarioId);
            stmtMensaje.setString(7, ticketId);
            return stmtMensaje.executeUpdate() > 0;
        }
    }
    
    /**
     * Actualiza el estado de un ticket específico
     * @param ticketId ID del ticket a actualizar
     * @param nuevoEstado Nuevo estado del ticket (abierto, en_proceso, resuelto, cerrado)
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarEstadoTicket(String ticketId, String nuevoEstado) {
        String consultaSQL = "UPDATE mensaje_tickets SET estado = ? WHERE ticket_id = ?";
        
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consultaSQL)) {
            
            stmt.setString(1, nuevoEstado);
            stmt.setString(2, ticketId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Genera un ID único para un nuevo ticket
     * Utiliza formato TK-YYYY-NNN donde NNN es un número secuencial
     * @return String con el nuevo ID del ticket
     */
    public String generarNuevoTicketId() {
        String consultaSQL = "SELECT COUNT(*) + 1 as siguiente FROM tickets WHERE ticket_id LIKE 'TK-2024-%'";
        
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consultaSQL);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int numero = rs.getInt("siguiente");
                return String.format("TK-2024-%03d", numero);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar ID de ticket: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback si hay error - usar timestamp
        return "TK-2024-" + System.currentTimeMillis();
    }
    
    /**
     * Cuenta el número de tickets que tienen un estado específico
     * Útil para generar estadísticas del sistema de soporte
     * @param estado Estado a contar (abierto, en_proceso, resuelto, cerrado)
     * @return Número de tickets con ese estado
     */
    public int contarTicketsPorEstado(String estado) {
        String consultaSQL = "SELECT COUNT(*) as total FROM mensaje_tickets WHERE estado = ?";
        
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consultaSQL)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar tickets por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Cierra una conexión transaccional de forma segura
     * Restaura el autocommit y cierra la conexión
     * @param conexion Conexión a cerrar
     */
    private void cerrarConexionTransaccional(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.setAutoCommit(true); // Restaurar autocommit
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión transaccional: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Obtiene tickets filtrados por prioridad
     * @param prioridad Prioridad a filtrar
     * @return Lista de tickets con la prioridad especificada
     */
    public List<TicketInfo> obtenerTicketsPorPrioridad(String prioridad) {
        List<TicketInfo> tickets = new ArrayList<>();
        String consultaSQL = """
            SELECT mt.id, t.ticket_id, mt.nombre_cliente, mt.email_cliente, 
                   mt.asunto, mt.prioridad, mt.estado, mt.fecha_creacion, t.tipo
            FROM mensaje_tickets mt
            JOIN tickets t ON mt.ticket_id = t.ticket_id
            WHERE mt.prioridad = ?
            ORDER BY mt.fecha_creacion DESC
        """;
        
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consultaSQL)) {
            
            stmt.setString(1, prioridad);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TicketInfo ticket = new TicketInfo();
                ticket.setId(rs.getInt("id"));
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setNombreCliente(rs.getString("nombre_cliente"));
                ticket.setEmailCliente(rs.getString("email_cliente"));
                ticket.setAsunto(rs.getString("asunto"));
                ticket.setPrioridad(rs.getString("prioridad"));
                ticket.setEstado(rs.getString("estado"));
                ticket.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                ticket.setTipo(rs.getString("tipo"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tickets por prioridad: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tickets;
    }
}