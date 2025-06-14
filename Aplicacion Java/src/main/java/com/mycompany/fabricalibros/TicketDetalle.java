/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import java.sql.Timestamp;

/**
 * Clase para detalles completos de un ticket (para el chat de soporte)
 * Extiende TicketInfo agregando campos adicionales para información detallada
 */
public class TicketDetalle extends TicketInfo {
    private String descripcion;
    private String contenido;
    private Timestamp fechaTicket;
    
    /**
     * Constructor vacío que llama al constructor padre
     * Inicializa un objeto TicketDetalle sin datos
     */
    public TicketDetalle() {
        super();
    }
    
    /**
     * Obtiene la descripción detallada del ticket
     * @return Descripción completa del problema o consulta
     */
    public String getDescripcion() { return descripcion; }
    
    /**
     * Establece la descripción detallada del ticket
     * @param descripcion Texto detallado del ticket
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    /**
     * Obtiene el contenido completo del ticket
     * @return Contenido que incluye asunto y descripción combinados
     */
    public String getContenido() { return contenido; }
    
    /**
     * Establece el contenido completo del ticket
     * @param contenido Texto completo del ticket
     */
    public void setContenido(String contenido) { this.contenido = contenido; }
    
    /**
     * Obtiene la fecha original de creación del ticket
     * @return Timestamp de cuando se creó inicialmente el ticket
     */
    public Timestamp getFechaTicket() { return fechaTicket; }
    
    /**
     * Establece la fecha original de creación del ticket
     * @param fechaTicket Timestamp de creación inicial
     */
    public void setFechaTicket(Timestamp fechaTicket) { this.fechaTicket = fechaTicket; }
    
    /**
     * Formatea la fecha de creación para mostrar en la interfaz
     * Utiliza el formato dd/MM/yyyy HH:mm para mayor legibilidad
     * @return Fecha formateada como string o "Sin fecha" si es null
     */
    public String getFechaFormateada() {
        if (fechaCreacion != null) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formato.format(fechaCreacion);
        }
        return "Sin fecha";
    }
    
    /**
     * Verifica si el ticket tiene información completa
     * @return true si tiene descripción y contenido, false en caso contrario
     */
    public boolean tieneInformacionCompleta() {
        return descripcion != null && !descripcion.trim().isEmpty() &&
               contenido != null && !contenido.trim().isEmpty();
    }
    
    /**
     * Obtiene un resumen del ticket para mostrar en interfaces
     * @return String con resumen del ticket (ID, cliente, estado)
     */
    public String getResumen() {
        return String.format("Ticket %s - %s (%s)", 
            ticketId, nombreCliente, estado.toUpperCase());
    }
}