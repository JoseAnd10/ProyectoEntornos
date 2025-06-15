/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana de chat individual para tickets de soporte
 * Permite ver detalles completos, enviar mensajes y cambiar estados
 */
public class VentanaChatTicket extends JFrame {
    private String ticketId;
    private GestorSoporte ticketDAO;
    private TicketDetalle ticketDetalle;
    
    // Componentes de la interfaz
    private JTextArea areaChatHistorial;
    private JTextField campoNuevoMensaje;
    private JButton botonEnviar;
    private JButton botonCerrarTicket;
    private JButton botonCambiarEstado;
    private JButton botonVolver;
    private JLabel labelInfoTicket;
    private JLabel labelEstadoTicket;
    private JComboBox<String> comboNuevoEstado;
    
    /**
     * Constructor principal que inicializa la ventana de chat
     * @param ticketId ID del ticket a mostrar en el chat
     */
    public VentanaChatTicket(String ticketId) {
        this.ticketId = ticketId;
        this.ticketDAO = new GestorSoporte();
        
        inicializarComponentes();
        cargarDatosTicket();
        configurarDiseno();
        configurarEventos();
    }
    
    /**
     * Inicializa todos los componentes de la ventana
     * Configura propiedades básicas de ventana y componentes
     */
    private void inicializarComponentes() {
        setTitle("Chat Soporte - Ticket: " + ticketId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        
        crearAreaChatHistorial();
        crearCampoNuevoMensaje();
        crearBotones();
        crearLabelsInformacion();
        crearComboEstados();
    }
    
    /**
     * Crea y configura el área de historial de chat
     */
    private void crearAreaChatHistorial() {
        areaChatHistorial = new JTextArea();
        areaChatHistorial.setEditable(false);
        areaChatHistorial.setFont(new Font("Arial", Font.PLAIN, 12));
        areaChatHistorial.setBackground(new Color(248, 249, 250));
        areaChatHistorial.setLineWrap(true);
        areaChatHistorial.setWrapStyleWord(true);
    }
    
    /**
     * Crea y configura el campo para escribir nuevos mensajes
     */
    private void crearCampoNuevoMensaje() {
        campoNuevoMensaje = new JTextField();
        campoNuevoMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    /**
     * Crea todos los botones de la ventana
     */
    private void crearBotones() {
        botonEnviar = new JButton("Enviar");
        botonCerrarTicket = new JButton("Cerrar Ticket");
        botonCambiarEstado = new JButton("Cambiar Estado");
        botonVolver = new JButton("Volver");
    }
    
    /**
     * Crea las etiquetas de información del ticket
     */
    private void crearLabelsInformacion() {
        labelInfoTicket = new JLabel();
        labelEstadoTicket = new JLabel();
    }
    
    /**
     * Crea el combo box para cambiar estados del ticket
     */
    private void crearComboEstados() {
        String[] estados = {"abierto", "en_proceso", "resuelto", "cerrado"};
        comboNuevoEstado = new JComboBox<>(estados);
    }
    
    /**
     * Carga los datos completos del ticket desde la base de datos
     * Actualiza la interfaz con la información obtenida
     */
    private void cargarDatosTicket() {
        ticketDetalle = ticketDAO.obtenerDetalleTicket(ticketId);
        
        if (ticketDetalle != null) {
            actualizarInformacionTicket();
            actualizarEstadoTicket();
            seleccionarEstadoActual();
            cargarHistorialChat();
        } else {
            mostrarErrorYCerrar();
        }
    }
    
    /**
     * Actualiza la información básica del ticket en la interfaz
     */
    private void actualizarInformacionTicket() {
        String infoTexto = String.format(
            "Cliente: %s (%s) | Asunto: %s | Prioridad: %s | Tipo: %s",
            ticketDetalle.getNombreCliente(),
            ticketDetalle.getEmailCliente(),
            ticketDetalle.getAsunto(),
            ticketDetalle.getPrioridad().toUpperCase(),
            ticketDetalle.getTipo().toUpperCase()
        );
        labelInfoTicket.setText(infoTexto);
    }
    
    /**
     * Actualiza la visualización del estado del ticket
     */
    private void actualizarEstadoTicket() {
        String estadoTexto = "Estado: " + ticketDetalle.getEstado().replace("_", " ").toUpperCase();
        labelEstadoTicket.setText(estadoTexto);
        labelEstadoTicket.setOpaque(true);
        labelEstadoTicket.setBackground(obtenerColorEstado(ticketDetalle.getEstado()));
        labelEstadoTicket.setForeground(Color.WHITE);
    }
    
    /**
     * Selecciona el estado actual del ticket en el combo box
     */
    private void seleccionarEstadoActual() {
        comboNuevoEstado.setSelectedItem(ticketDetalle.getEstado());
    }
    
    /**
     * Muestra error y cierra la ventana si no se pueden cargar los datos
     */
    private void mostrarErrorYCerrar() {
        JOptionPane.showMessageDialog(this,
            "No se pudo cargar la información del ticket.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        dispose();
    }
    
    /**
     * Configura el diseño visual completo de la ventana
     * Organiza componentes en paneles con BorderLayout
     */
    private void configurarDiseno() {
        setLayout(new BorderLayout());
        
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();
        configurarBotones();
    }
    
    /**
     * Crea el panel superior con información del ticket
     */
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(60, 60, 60));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel etiquetaTitulo = new JLabel("TICKET: " + ticketId, SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaTitulo.setForeground(Color.WHITE);
        
        configurarLabelsInformacion();
        
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(60, 60, 60));
        panelInfo.add(labelInfoTicket, BorderLayout.CENTER);
        panelInfo.add(labelEstadoTicket, BorderLayout.EAST);
        
        panelSuperior.add(etiquetaTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelInfo, BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    /**
     * Configura el aspecto visual de las etiquetas de información
     */
    private void configurarLabelsInformacion() {
        labelInfoTicket.setFont(new Font("Arial", Font.PLAIN, 12));
        labelInfoTicket.setForeground(Color.WHITE);
        
        labelEstadoTicket.setFont(new Font("Arial", Font.BOLD, 12));
        labelEstadoTicket.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    
    /**
     * Crea el panel central con el área de chat
     */
    private void crearPanelCentral() {
        JScrollPane scrollChat = new JScrollPane(areaChatHistorial);
        scrollChat.setBorder(BorderFactory.createTitledBorder("Historial de Conversación"));
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(scrollChat, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel inferior con entrada de mensaje y botones
     */
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        crearPanelMensaje(panelInferior);
        crearPanelBotonesControl(panelInferior);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Crea el panel de entrada de mensaje
     * @param panelPadre Panel padre donde agregar el panel de mensaje
     */
    private void crearPanelMensaje(JPanel panelPadre) {
        JPanel panelMensaje = new JPanel(new BorderLayout(10, 0));
        panelMensaje.add(new JLabel("Nuevo mensaje:"), BorderLayout.WEST);
        panelMensaje.add(campoNuevoMensaje, BorderLayout.CENTER);
        panelMensaje.add(botonEnviar, BorderLayout.EAST);
        
        panelPadre.add(panelMensaje, BorderLayout.NORTH);
    }
    
    /**
     * Crea el panel de botones de control
     * @param panelPadre Panel padre donde agregar los botones
     */
    private void crearPanelBotonesControl(JPanel panelPadre) {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.add(new JLabel("Cambiar a:"));
        panelBotones.add(comboNuevoEstado);
        panelBotones.add(botonCambiarEstado);
        panelBotones.add(botonCerrarTicket);
        panelBotones.add(botonVolver);
        
        panelPadre.add(panelBotones, BorderLayout.SOUTH);
        
    }
    
    /**
     * Configura el aspecto visual de todos los botones
     */
    private void configurarBotones() {
        configurarBotonEnviar();
        configurarBotonCambiarEstado();
        configurarBotonCerrarTicket();
        configurarBotonVolver();
    }
    
    /**
     * Configura el botón de enviar mensaje
     */
    private void configurarBotonEnviar() {
        botonEnviar.setBackground(new Color(0, 123, 255));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);
    }
    
    /**
     * Configura el botón de cambiar estado
     */
    private void configurarBotonCambiarEstado() {
        botonCambiarEstado.setBackground(new Color(255, 193, 7));
        botonCambiarEstado.setForeground(Color.BLACK);
        botonCambiarEstado.setFocusPainted(false);
    }
    
    /**
     * Configura el botón de cerrar ticket
     */
    private void configurarBotonCerrarTicket() {
        botonCerrarTicket.setBackground(new Color(220, 53, 69));
        botonCerrarTicket.setForeground(Color.WHITE);
        botonCerrarTicket.setFocusPainted(false);
    }
    
    /**
     * Configura el botón de volver
     */
    private void configurarBotonVolver() {
        botonVolver.setBackground(new Color(108, 117, 125));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
    }
    
    /**
     * Configura todos los eventos de la ventana
     * Asocia listeners a botones y campos
     */
    private void configurarEventos() {
        configurarEventoEnviarMensaje();
        configurarEventoCambiarEstado();
        configurarEventoCerrarTicket();
        configurarEventoVolver();
    }
    
    /**
     * Configura los eventos relacionados con enviar mensajes
     */
    private void configurarEventoEnviarMensaje() {
        // Botón enviar mensaje
        botonEnviar.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón enviar
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
        
        // Enter en campo de mensaje
        campoNuevoMensaje.addActionListener(new ActionListener() {
            /**
             * Maneja la tecla Enter en el campo de mensaje
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
    }
    
    /**
     * Configura el evento del botón cambiar estado
     */
    private void configurarEventoCambiarEstado() {
        botonCambiarEstado.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón cambiar estado
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoTicket();
            }
        });
    }
    
    /**
     * Configura el evento del botón cerrar ticket
     */
    private void configurarEventoCerrarTicket() {
        botonCerrarTicket.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón cerrar ticket
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarTicket();
            }
        });
    }
    
    /**
     * Configura el evento del botón volver
     */
    private void configurarEventoVolver() {
        botonVolver.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón volver
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Carga y muestra el historial completo del chat
     * Incluye información inicial y mensajes del ticket
     */
    private void cargarHistorialChat() {
        StringBuilder historial = new StringBuilder();
        
        agregarInformacionTicket(historial);
        agregarDescripcionInicial(historial);
        agregarHistorialConversacion(historial);
        simularMensajesEjemplo(historial);
        
        areaChatHistorial.setText(historial.toString());
        scrollAlFinal();
    }
    
    /**
     * Agrega la información básica del ticket al historial
     * @param historial StringBuilder donde agregar la información
     */
    private void agregarInformacionTicket(StringBuilder historial) {
        historial.append("=== INFORMACIÓN DEL TICKET ===\n");
        historial.append("Fecha de creación: ").append(ticketDetalle.getFechaFormateada()).append("\n");
        historial.append("Cliente: ").append(ticketDetalle.getNombreCliente()).append("\n");
        historial.append("Email: ").append(ticketDetalle.getEmailCliente()).append("\n");
        historial.append("Asunto: ").append(ticketDetalle.getAsunto()).append("\n");
        historial.append("Prioridad: ").append(ticketDetalle.getPrioridad().toUpperCase()).append("\n");
        historial.append("Tipo: ").append(ticketDetalle.getTipo().toUpperCase()).append("\n\n");
    }
    
    /**
     * Agrega la descripción inicial del ticket al historial
     * @param historial StringBuilder donde agregar la descripción
     */
    private void agregarDescripcionInicial(StringBuilder historial) {
        historial.append("=== DESCRIPCIÓN INICIAL ===\n");
        historial.append(ticketDetalle.getDescripcion()).append("\n\n");
    }
    
    /**
     * Agrega la sección de historial de conversación
     * @param historial StringBuilder donde agregar la sección
     */
    private void agregarHistorialConversacion(StringBuilder historial) {
        historial.append("=== HISTORIAL DE CONVERSACIÓN ===\n");
        historial.append("(Aquí se mostrarían los mensajes del chat cuando se implemente esa funcionalidad)\n\n");
    }
    
    /**
     * Simula algunos mensajes de ejemplo en el historial
     * @param historial StringBuilder donde agregar los mensajes
     */
    private void simularMensajesEjemplo(StringBuilder historial) {
        historial.append("[").append(ticketDetalle.getFechaFormateada()).append("] ");
        historial.append(ticketDetalle.getNombreCliente()).append(": ");
        historial.append(ticketDetalle.getDescripcion()).append("\n\n");
    }
    
    /**
     * Hace scroll automático al final del área de chat
     */
    private void scrollAlFinal() {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Ejecuta el scroll al final en el hilo de eventos
             */
            @Override
            public void run() {
                areaChatHistorial.setCaretPosition(areaChatHistorial.getDocument().getLength());
            }
        });
    }
    
    /**
     * Procesa el envío de un nuevo mensaje
     * Valida el contenido y lo agrega al historial
     */
    private void enviarMensaje() {
        String mensaje = campoNuevoMensaje.getText().trim();
        
        if (!validarMensaje(mensaje)) {
            return;
        }
        
        agregarMensajeAlHistorial(mensaje);
        limpiarCampoMensaje();
        scrollAlFinal();
        mostrarConfirmacionEnvio();
    }
    
    /**
     * Valida que el mensaje no esté vacío
     * @param mensaje Mensaje a validar
     * @return true si el mensaje es válido, false si está vacío
     */
    private boolean validarMensaje(String mensaje) {
        if (mensaje.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Escriba un mensaje antes de enviar.",
                "Mensaje vacío",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Agrega un nuevo mensaje al historial de chat
     * @param mensaje Contenido del mensaje a agregar
     */
    private void agregarMensajeAlHistorial(String mensaje) {
        String timestamp = obtenerTimestampActual();
        String nuevoMensaje = String.format("[%s] Soporte: %s\n\n", timestamp, mensaje);
        areaChatHistorial.append(nuevoMensaje);
    }
    
    /**
     * Obtiene el timestamp actual formateado
     * @return String con fecha y hora actual en formato dd/MM/yyyy HH:mm
     */
    private String obtenerTimestampActual() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
    }
    
    /**
     * Limpia el campo de entrada de mensaje
     */
    private void limpiarCampoMensaje() {
        campoNuevoMensaje.setText("");
    }
    
    /**
     * Muestra confirmación de que el mensaje fue enviado
     */
    private void mostrarConfirmacionEnvio() {
        JOptionPane.showMessageDialog(this,
            "Mensaje enviado correctamente.\n(Funcionalidad de guardado pendiente de implementar)",
            "Mensaje Enviado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Procesa el cambio de estado del ticket
     * Valida el cambio y actualiza la base de datos
     */
    private void cambiarEstadoTicket() {
        String nuevoEstado = (String) comboNuevoEstado.getSelectedItem();
        String estadoActual = ticketDetalle.getEstado();
        
        if (!validarCambioEstado(nuevoEstado, estadoActual)) {
            return;
        }
        
        if (confirmarCambioEstado(estadoActual, nuevoEstado)) {
            ejecutarCambioEstado(nuevoEstado, estadoActual);
        } else {
            revertirSeleccionCombo(estadoActual);
        }
    }
    
    /**
     * Valida que el nuevo estado sea diferente al actual
     * @param nuevoEstado Estado seleccionado
     * @param estadoActual Estado actual del ticket
     * @return true si el cambio es válido, false si es el mismo estado
     */
    private boolean validarCambioEstado(String nuevoEstado, String estadoActual) {
        if (nuevoEstado.equals(estadoActual)) {
            JOptionPane.showMessageDialog(this,
                "El ticket ya se encuentra en ese estado.",
                "Sin cambios",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Solicita confirmación para el cambio de estado
     * @param estadoActual Estado actual del ticket
     * @param nuevoEstado Nuevo estado propuesto
     * @return true si el usuario confirma, false en caso contrario
     */
    private boolean confirmarCambioEstado(String estadoActual, String nuevoEstado) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Cambiar el estado del ticket de '%s' a '%s'?",
                estadoActual.replace("_", " "),
                nuevoEstado.replace("_", " ")),
            "Confirmar cambio de estado",
            JOptionPane.YES_NO_OPTION);
        
        return confirmacion == JOptionPane.YES_OPTION;
    }
    
    /**
     * Ejecuta el cambio de estado en la base de datos y actualiza la interfaz
     * @param nuevoEstado Nuevo estado del ticket
     * @param estadoActual Estado actual del ticket
     */
    private void ejecutarCambioEstado(String nuevoEstado, String estadoActual) {
        boolean exito = ticketDAO.actualizarEstadoTicket(ticketId, nuevoEstado);
        
        if (exito) {
            procesarCambioExitoso(nuevoEstado);
        } else {
            procesarErrorCambio(estadoActual);
        }
    }
    
    /**
     * Procesa un cambio de estado exitoso
     * @param nuevoEstado Nuevo estado del ticket
     */
    private void procesarCambioExitoso(String nuevoEstado) {
        ticketDetalle.setEstado(nuevoEstado);
        actualizarVisualizacionEstado(nuevoEstado);
        agregarMensajeCambioEstado(nuevoEstado);
        scrollAlFinal();
        mostrarConfirmacionCambio();
    }
    
    /**
     * Actualiza la visualización del estado en la interfaz
     * @param nuevoEstado Nuevo estado del ticket
     */
    private void actualizarVisualizacionEstado(String nuevoEstado) {
        String estadoTexto = "Estado: " + nuevoEstado.replace("_", " ").toUpperCase();
        labelEstadoTicket.setText(estadoTexto);
        labelEstadoTicket.setBackground(obtenerColorEstado(nuevoEstado));
    }
    
    /**
     * Agrega un mensaje del sistema sobre el cambio de estado
     * @param nuevoEstado Nuevo estado del ticket
     */
    private void agregarMensajeCambioEstado(String nuevoEstado) {
        String timestamp = obtenerTimestampActual();
        String mensajeEstado = String.format("[%s] SISTEMA: Estado cambiado a '%s'\n\n", 
            timestamp, nuevoEstado.replace("_", " ").toUpperCase());
        areaChatHistorial.append(mensajeEstado);
    }
    
    /**
     * Muestra confirmación de cambio de estado exitoso
     */
    private void mostrarConfirmacionCambio() {
        JOptionPane.showMessageDialog(this,
            "Estado actualizado correctamente.",
            "Estado Actualizado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Procesa un error en el cambio de estado
     * @param estadoActual Estado actual para revertir la selección
     */
    private void procesarErrorCambio(String estadoActual) {
        JOptionPane.showMessageDialog(this,
            "Error al actualizar el estado del ticket.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        revertirSeleccionCombo(estadoActual);
    }
    
    /**
     * Revierte la selección del combo al estado anterior
     * @param estadoActual Estado al que revertir
     */
    private void revertirSeleccionCombo(String estadoActual) {
        comboNuevoEstado.setSelectedItem(estadoActual);
    }
    
    /**
     * Procesa el cierre definitivo del ticket
     * Cambia el estado a "cerrado" con confirmación
     */
    private void cerrarTicket() {
        if (ticketYaCerrado()) {
            return;
        }
        
        if (confirmarCierreTicket()) {
            ejecutarCierreTicket();
        }
    }
    
    /**
     * Verifica si el ticket ya está cerrado
     * @return true si ya está cerrado, false en caso contrario
     */
    private boolean ticketYaCerrado() {
        if (ticketDetalle.getEstado().equals("cerrado")) {
            JOptionPane.showMessageDialog(this,
                "El ticket ya se encuentra cerrado.",
                "Ticket ya cerrado",
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    /**
     * Solicita confirmación para cerrar el ticket
     * @return true si el usuario confirma, false en caso contrario
     */
    private boolean confirmarCierreTicket() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea cerrar este ticket?\n" +
            "Esta acción cambiará el estado a 'CERRADO'.",
            "Confirmar cierre de ticket",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        return confirmacion == JOptionPane.YES_OPTION;
    }
    
    /**
     * Ejecuta el cierre del ticket actualizando la base de datos
     */
    private void ejecutarCierreTicket() {
        boolean exito = ticketDAO.actualizarEstadoTicket(ticketId, "cerrado");
        
        if (exito) {
            procesarCierreExitoso();
        } else {
            mostrarErrorCierre();
        }
    }
    
    /**
     * Procesa un cierre de ticket exitoso
     */
    private void procesarCierreExitoso() {
        ticketDetalle.setEstado("cerrado");
        comboNuevoEstado.setSelectedItem("cerrado");
        
        actualizarVisualizacionCierre();
        agregarMensajeCierre();
        scrollAlFinal();
        mostrarConfirmacionCierre();
    }
    
    /**
     * Actualiza la visualización para mostrar el ticket como cerrado
     */
    private void actualizarVisualizacionCierre() {
        labelEstadoTicket.setText("Estado: CERRADO");
        labelEstadoTicket.setBackground(obtenerColorEstado("cerrado"));
    }
    
    /**
     * Agrega un mensaje del sistema sobre el cierre del ticket
     */
    private void agregarMensajeCierre() {
        String timestamp = obtenerTimestampActual();
        String mensajeCierre = String.format("[%s] SISTEMA: Ticket cerrado por el soporte\n\n", timestamp);
        areaChatHistorial.append(mensajeCierre);
    }
    
    /**
     * Muestra confirmación de cierre exitoso
     */
    private void mostrarConfirmacionCierre() {
        JOptionPane.showMessageDialog(this,
            "Ticket cerrado correctamente.",
            "Ticket Cerrado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Muestra error en el cierre del ticket
     */
    private void mostrarErrorCierre() {
        JOptionPane.showMessageDialog(this,
            "Error al cerrar el ticket.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Obtiene el color apropiado según el estado del ticket
     * @param estado Estado del ticket
     * @return Color correspondiente al estado
     */
    
    private Color obtenerColorEstado(String estado) {
        switch (estado.toLowerCase()) {
            case "abierto": return new Color(40, 167, 69);      // Verde
            case "en_proceso": return new Color(255, 193, 7);   // Amarillo
            case "resuelto": return new Color(0, 123, 255);     // Azul
            case "cerrado": return new Color(108, 117, 125);    // Gris
            default: return new Color(108, 117, 125);           // Gris por defecto
        }
    }
}