package com.mycompany.fabricalibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana para crear nuevos tickets de soporte con estilo corporativo
 * Formulario completo para registrar solicitudes de soporte técnico
 */
public class VentanaNuevoTicket extends JDialog {
    private JTextField campoNombreCliente;
    private JTextField campoEmailCliente;
    private JTextField campoAsunto;
    private JTextArea areaDescripcion;
    private JComboBox<String> comboPrioridad;
    private JComboBox<String> comboTipo;
    private JButton botonCrear;
    private JButton botonCancelar;
    private GestorSoporte ticketDAO;
    private VentanaSoporte ventanaPadre;
    
    // Colores corporativos de la empresa
    private static final Color AMARILLO_CORPORATIVO = new Color(255, 215, 0);
    private static final Color NEGRO_CORPORATIVO = new Color(35, 35, 35);
    private static final Color BLANCO_CORPORATIVO = new Color(250, 250, 250);
    private static final Color AMARILLO_CLARO = new Color(255, 245, 157);
    
    /**
     * Constructor principal del formulario de nuevo ticket
     * @param padre Ventana padre que abrió este formulario
     */
    public VentanaNuevoTicket(VentanaSoporte padre) {
        super(padre, "Nuevo Ticket de Soporte", true);
        this.ventanaPadre = padre;
        this.ticketDAO = new GestorSoporte();
        
        inicializarComponentes();
        configurarDiseno();
        configurarEventos();
    }
    
    /**
     * Inicializa todos los componentes del formulario
     */
    private void inicializarComponentes() {
        configurarVentana();
        crearCamposTexto();
        crearComboBoxes();
        crearBotones();
    }
    
    /**
     * Configura las propiedades básicas de la ventana
     */
    private void configurarVentana() {
        setSize(650, 550);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    /**
     * Crea todos los campos de texto del formulario
     */
    private void crearCamposTexto() {
        campoNombreCliente = new JTextField(30);
        campoEmailCliente = new JTextField(30);
        campoAsunto = new JTextField(30);
        
        areaDescripcion = new JTextArea(8, 30);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
    }
    
    /**
     * Crea los ComboBoxes para prioridad y tipo de ticket
     */
    private void crearComboBoxes() {
        // ComboBox para prioridad
        String[] prioridades = {"baja", "media", "alta", "urgente"};
        comboPrioridad = new JComboBox<>(prioridades);
        comboPrioridad.setSelectedItem("media"); // Por defecto
        
        // ComboBox para tipo
        String[] tipos = {"consulta", "problema", "sugerencia", "urgente"};
        comboTipo = new JComboBox<>(tipos);
        comboTipo.setSelectedItem("consulta"); // Por defecto
    }
    
    /**
     * Crea los botones de acción del formulario
     */
    private void crearBotones() {
        botonCrear = new JButton("Crear Ticket");
        botonCancelar = new JButton("Cancelar");
    }
    
    /**
     * Configura el diseño visual completo del formulario
     */
    private void configurarDiseno() {
        setLayout(new BorderLayout());
        crearPanelPrincipal();
        crearPanelBotones();
    }
    
    /**
     * Crea el panel principal con el formulario
     */
    private void crearPanelPrincipal() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelPrincipal.setBackground(BLANCO_CORPORATIVO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        agregarTitulo(panelPrincipal, gbc);
        agregarCamposFormulario(panelPrincipal, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    /**
     * Agrega el título al formulario
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarTitulo(JPanel panel, GridBagConstraints gbc) {
        JLabel etiquetaTitulo = new JLabel("CREAR NUEVO TICKET DE SOPORTE");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaTitulo.setForeground(NEGRO_CORPORATIVO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 30, 10);
        panel.add(etiquetaTitulo, gbc);
    }
    
    /**
     * Agrega todos los campos del formulario
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCamposFormulario(JPanel panel, GridBagConstraints gbc) {
        // Restaurar configuración normal
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        agregarCampoNombreCliente(panel, gbc);
        agregarCampoEmailCliente(panel, gbc);
        agregarCampoAsunto(panel, gbc);
        agregarComboTipo(panel, gbc);
        agregarComboPrioridad(panel, gbc);
        agregarAreaDescripcion(panel, gbc);
    }
    
    /**
     * Agrega el campo nombre del cliente
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoNombreCliente(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(crearEtiqueta("Nombre del Cliente:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        configurarCampoTexto(campoNombreCliente);
        panel.add(campoNombreCliente, gbc);
    }
    
    /**
     * Agrega el campo email del cliente
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoEmailCliente(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Email del Cliente:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        configurarCampoTexto(campoEmailCliente);
        panel.add(campoEmailCliente, gbc);
    }
    
    /**
     * Agrega el campo asunto del ticket
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoAsunto(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Asunto:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        configurarCampoTexto(campoAsunto);
        panel.add(campoAsunto, gbc);
    }
    
    /**
     * Agrega el combo de tipo de ticket
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarComboTipo(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Tipo:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        configurarComboBox(comboTipo);
        panel.add(comboTipo, gbc);
    }
    
    /**
     * Agrega el combo de prioridad del ticket
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarComboPrioridad(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Prioridad:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        configurarComboBox(comboPrioridad);
        panel.add(comboPrioridad, gbc);
    }
    
    /**
     * Agrega el área de descripción del ticket
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarAreaDescripcion(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(crearEtiqueta("Descripción:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane scrollDescripcion = new JScrollPane(areaDescripcion);
        configurarAreaTexto(scrollDescripcion);
        panel.add(scrollDescripcion, gbc);
    }
    
    /**
     * Crea una etiqueta con estilo corporativo
     * @param texto Texto de la etiqueta
     * @return JLabel configurada
     */
    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        etiqueta.setForeground(NEGRO_CORPORATIVO);
        return etiqueta;
    }
    
    /**
     * Configura el aspecto de un campo de texto
     * @param campo Campo a configurar
     */
    private void configurarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setBackground(BLANCO_CORPORATIVO);
    }
    
    /**
     * Configura el aspecto de un ComboBox
     * @param combo ComboBox a configurar
     */
    private void configurarComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
        combo.setBackground(BLANCO_CORPORATIVO);
        combo.setBorder(BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 1));
    }
    
    /**
     * Configura el aspecto del área de texto con scroll
     * @param scrollPane ScrollPane que contiene el área de texto
     */
    private void configurarAreaTexto(JScrollPane scrollPane) {
        areaDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        areaDescripcion.setBackground(BLANCO_CORPORATIVO);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    /**
     * Crea el panel de botones con estilo corporativo
     */
    private void crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotones.setBackground(AMARILLO_CLARO);
        panelBotones.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, AMARILLO_CORPORATIVO));
        
        // Configurar botones
        configurarBotonPrincipal(botonCrear);
        configurarBotonSecundario(botonCancelar);
        
        panelBotones.add(botonCrear);
        panelBotones.add(botonCancelar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Configura un botón principal con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonPrincipal(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(AMARILLO_CORPORATIVO);
        boton.setForeground(NEGRO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
    }
    
    /**
     * Configura un botón secundario con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonSecundario(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(NEGRO_CORPORATIVO);
        boton.setForeground(AMARILLO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
    }
    
    /**
     * Configura todos los eventos del formulario
     */
    private void configurarEventos() {
        configurarEventosBotones();
    }
    
    /**
     * Configura los eventos de los botones
     */
    private void configurarEventosBotones() {
        // Evento botón crear ticket
        botonCrear.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón crear ticket
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                crearTicket();
            }
        });
        
        // Evento botón cancelar
        botonCancelar.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón cancelar
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Procesa la creación del nuevo ticket
     * Valida los datos y guarda en la base de datos
     */
    private void crearTicket() {
        // Validar campos antes de proceder
        if (!validarCampos()) {
            return;
        }
        
        // Obtener datos del formulario
        DatosTicket datos = obtenerDatosFormulario();
        
        // Generar ID único para el ticket
        String ticketId = ticketDAO.generarNuevoTicketId();
        
        // Crear contenido combinando asunto y descripción
        String contenido = construirContenidoTicket(datos.asunto, datos.descripcion);
        
        // Intentar crear el ticket en la base de datos
        boolean exito = ticketDAO.crearNuevoTicket(
            ticketId, contenido, datos.tipo,
            datos.nombreCliente, datos.emailCliente, datos.asunto, 
            datos.descripcion, datos.prioridad, 1 // usuarioId por defecto
        );
        
        procesarResultadoCreacion(exito, ticketId);
    }
    
    /**
     * Obtiene todos los datos del formulario
     * @return Objeto con los datos del formulario
     */
    private DatosTicket obtenerDatosFormulario() {
        DatosTicket datos = new DatosTicket();
        datos.nombreCliente = campoNombreCliente.getText().trim();
        datos.emailCliente = campoEmailCliente.getText().trim();
        datos.asunto = campoAsunto.getText().trim();
        datos.descripcion = areaDescripcion.getText().trim();
        datos.prioridad = (String) comboPrioridad.getSelectedItem();
        datos.tipo = (String) comboTipo.getSelectedItem();
        return datos;
    }
    
    /**
     * Construye el contenido completo del ticket
     * @param asunto Asunto del ticket
     * @param descripcion Descripción detallada
     * @return Contenido formateado del ticket
     */
    private String construirContenidoTicket(String asunto, String descripcion) {
        return "Asunto: " + asunto + "\n\nDescripción: " + descripcion;
    }
    
    /**
     * Procesa el resultado de la creación del ticket
     * @param exito Si la creación fue exitosa
     * @param ticketId ID del ticket creado
     */
    private void procesarResultadoCreacion(boolean exito, String ticketId) {
        if (exito) {
            mostrarMensajeExito(ticketId);
            actualizarVentanaPadre();
            dispose();
        } else {
            mostrarMensajeError("Error al crear el ticket. Inténtelo nuevamente.");
        }
    }
    
    /**
     * Muestra mensaje de éxito con el ID del ticket creado
     * @param ticketId ID del ticket creado
     */
    private void mostrarMensajeExito(String ticketId) {
        JOptionPane.showMessageDialog(this,
            "Ticket creado exitosamente!\nID: " + ticketId,
            "Ticket Creado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Actualiza la tabla en la ventana padre
     */
    private void actualizarVentanaPadre() {
        if (ventanaPadre != null) {
            ventanaPadre.actualizarTabla();
        }
    }
    
    /**
     * Valida todos los campos del formulario
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        return validarNombreCliente() && 
               validarEmailCliente() && 
               validarAsunto() && 
               validarDescripcion();
    }
    
    /**
     * Valida el campo nombre del cliente
     * @return true si es válido, false en caso contrario
     */
    private boolean validarNombreCliente() {
        if (campoNombreCliente.getText().trim().isEmpty()) {
            mostrarMensajeError("El nombre del cliente es obligatorio.");
            campoNombreCliente.requestFocus();
            return false;
        }
        return true;
    }
    
    /**
     * Valida el campo email del cliente
     * @return true si es válido, false en caso contrario
     */
    private boolean validarEmailCliente() {
        String email = campoEmailCliente.getText().trim();
        if (email.isEmpty()) {
            mostrarMensajeError("El email del cliente es obligatorio.");
            campoEmailCliente.requestFocus();
            return false;
        }
        if (!validarFormatoEmail(email)) {
            mostrarMensajeError("Ingrese un email válido.");
            campoEmailCliente.requestFocus();
            return false;
        }
        return true;
    }
    
    /**
     * Valida el formato básico del email
     * @param email Email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean validarFormatoEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * Valida el campo asunto del ticket
     * @return true si es válido, false en caso contrario
     */
    private boolean validarAsunto() {
        if (campoAsunto.getText().trim().isEmpty()) {
            mostrarMensajeError("El asunto es obligatorio.");
            campoAsunto.requestFocus();
            return false;
        }
        return true;
    }
    
    /**
     * Valida el campo descripción del ticket
     * @return true si es válido, false en caso contrario
     */
    private boolean validarDescripcion() {
        if (areaDescripcion.getText().trim().isEmpty()) {
            mostrarMensajeError("La descripción es obligatoria.");
            areaDescripcion.requestFocus();
            return false;
        }
        return true;
    }
    
    /**
     * Muestra un mensaje de error al usuario
     * @param mensaje Texto del mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Clase interna para encapsular los datos del formulario
     */
    private static class DatosTicket {
        String nombreCliente;
        String emailCliente;
        String asunto;
        String descripcion;
        String prioridad;
        String tipo;
    }
}