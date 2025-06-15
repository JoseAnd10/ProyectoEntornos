/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import java.sql.Timestamp;

/**
 * Clase para información básica de tickets (para mostrar en tabla)
 * Contiene los datos principales de un ticket de soporte
 */
public class TicketInfo {
    protected int id;
    protected String ticketId;
    protected String nombreCliente;
    protected String emailCliente;
    protected String asunto;
    protected String prioridad;
    protected String estado;
    protected Timestamp fechaCreacion;
    protected String tipo;
    
    /**
     * Constructor vacío por defecto
     * Inicializa un objeto TicketInfo sin datos
     */
    public TicketInfo() {}
    
    /**
     * Constructor completo con todos los parámetros
     * @param id ID único del ticket en la base de datos
     * @param ticketId Identificador público del ticket (ej: TK-2024-001)
     * @param nombreCliente Nombre del cliente que creó el ticket
     * @param emailCliente Email del cliente
     * @param asunto Asunto del ticket
     * @param prioridad Prioridad del ticket (baja, media, alta, urgente)
     * @param estado Estado actual del ticket (abierto, en_proceso, resuelto, cerrado)
     * @param fechaCreacion Fecha y hora de creación del ticket
     * @param tipo Tipo de ticket (consulta, problema, sugerencia, urgente)
     */
    public TicketInfo(int id, String ticketId, String nombreCliente, String emailCliente, 
                     String asunto, String prioridad, String estado, Timestamp fechaCreacion, String tipo) {
        this.id = id;
        this.ticketId = ticketId;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.asunto = asunto;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.tipo = tipo;
    }
    
    /**
     * Obtiene el ID único del ticket en la base de datos
     * @return ID numérico del ticket
     */
    public int getId() { return id; }
    
    /**
     * Establece el ID único del ticket
     * @param id ID numérico del ticket
     */
    public void setId(int id) { this.id = id; }
    
    /**
     * Obtiene el identificador público del ticket
     * @return String con formato TK-YYYY-NNN
     */
    public String getTicketId() { return ticketId; }
    
    /**
     * Establece el identificador público del ticket
     * @param ticketId Identificador en formato TK-YYYY-NNN
     */
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    
    /**
     * Obtiene el nombre del cliente que creó el ticket
     * @return Nombre completo del cliente
     */
    public String getNombreCliente() { return nombreCliente; }
    
    /**
     * Establece el nombre del cliente
     * @param nombreCliente Nombre completo del cliente
     */
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    
    /**
     * Obtiene el email del cliente
     * @return Dirección de email del cliente
     */
    public String getEmailCliente() { return emailCliente; }
    
    /**
     * Establece el email del cliente
     * @param emailCliente Dirección de email del cliente
     */
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    
    /**
     * Obtiene el asunto del ticket
     * @return Asunto o título del ticket
     */
    public String getAsunto() { return asunto; }
    
    /**
     * Establece el asunto del ticket
     * @param asunto Asunto o título del ticket
     */
    public void setAsunto(String asunto) { this.asunto = asunto; }
    
    /**
     * Obtiene la prioridad del ticket
     * @return Prioridad (baja, media, alta, urgente)
     */
    public String getPrioridad() { return prioridad; }
    
    /**
     * Establece la prioridad del ticket
     * @param prioridad Nivel de prioridad del ticket
     */
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    
    /**
     * Obtiene el estado actual del ticket
     * @return Estado (abierto, en_proceso, resuelto, cerrado)
     */
    public String getEstado() { return estado; }
    
    /**
     * Establece el estado del ticket
     * @param estado Nuevo estado del ticket
     */
    public void setEstado(String estado) { this.estado = estado; }
    
    /**
     * Obtiene la fecha de creación del ticket
     * @return Timestamp con fecha y hora de creación
     */
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    
    /**
     * Establece la fecha de creación del ticket
     * @param fechaCreacion Timestamp de creación
     */
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    /**
     * Obtiene el tipo de ticket
     * @return Tipo (consulta, problema, sugerencia, urgente)
     */
    public String getTipo() { return tipo; }
    
    /**
     * Establece el tipo de ticket
     * @param tipo Categoría del ticket
     */
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    /**
     * Obtiene el color apropiado según la prioridad del ticket
     * Utilizado para colorear elementos de la interfaz
     * @return Color correspondiente a la prioridad
     */
    public java.awt.Color getColorPrioridad() {
        switch (prioridad.toLowerCase()) {
            case "urgente": return new java.awt.Color(220, 53, 69);  // Rojo
            case "alta": return new java.awt.Color(255, 193, 7);     // Amarillo
            case "media": return new java.awt.Color(0, 123, 255);    // Azul
            case "baja": return new java.awt.Color(40, 167, 69);     // Verde
            default: return new java.awt.Color(108, 117, 125);       // Gris
        }
    }
    
    /**
     * Obtiene el color apropiado según el estado del ticket
     * Utilizado para colorear elementos de la interfaz
     * @return Color correspondiente al estado
     */
    public java.awt.Color getColorEstado() {
        switch (estado.toLowerCase()) {
            case "abierto": return new java.awt.Color(40, 167, 69);     // Verde
            case "en_proceso": return new java.awt.Color(255, 193, 7);  // Amarillo
            case "resuelto": return new java.awt.Color(0, 123, 255);    // Azul
            case "cerrado": return new java.awt.Color(108, 117, 125);   // Gris
            default: return new java.awt.Color(108, 117, 125);          // Gris por defecto
        }
    }
}