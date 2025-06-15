package com.mycompany.fabricalibros;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Ventana principal del sistema de soporte con estilo corporativo
 * Gestiona la visualización y administración de tickets de soporte
 */
public class VentanaSoporte extends JFrame {
    private JTable tablaTickets;
    private DefaultTableModel modeloTabla;
    private GestorSoporte ticketDAO;
    private JButton botonNuevoTicket;
    private JButton botonActualizar;
    private JButton botonVolver;
    private JLabel labelEstadisticas;
    
    // Colores corporativos de la empresa
    private static final Color AMARILLO_CORPORATIVO = new Color(255, 215, 0);
    private static final Color NEGRO_CORPORATIVO = new Color(35, 35, 35);
    private static final Color BLANCO_CORPORATIVO = new Color(250, 250, 250);
    private static final Color AMARILLO_CLARO = new Color(255, 245, 157);
    
    /**
     * Constructor principal que inicializa la ventana de soporte
     * Configura componentes y carga datos iniciales
     */
    public VentanaSoporte() {
        ticketDAO = new GestorSoporte();
        inicializarComponentes();
        configurarDiseno();
        configurarEventos();
        cargarTickets();
        actualizarEstadisticas();
    }
    
    /**
     * Inicializa todos los componentes básicos de la ventana
     */
    private void inicializarComponentes() {
        configurarVentanaPrincipal();
        crearModeloTabla();
        crearBotones();
        crearEtiquetas();
    }
    
    /**
     * Configura las propiedades básicas de la ventana principal
     */
    private void configurarVentanaPrincipal() {
        setTitle("Sistema de Soporte - Fábrica de Libros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
    }
    
    /**
     * Crea el modelo de datos para la tabla de tickets
     */
    private void crearModeloTabla() {
        String[] columnasTabla = {
            "ID Ticket", "Cliente", "Email", "Asunto", "Prioridad", "Estado", "Fecha"
        };
        modeloTabla = new DefaultTableModel(columnasTabla, 0) {
            /**
             * Determina si una celda es editable
             * @param row Fila de la celda
             * @param column Columna de la celda
             * @return false - todas las celdas son no editables
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaTickets = new JTable(modeloTabla);
    }
    
    /**
     * Crea todos los botones de la interfaz
     */
    private void crearBotones() {
        botonNuevoTicket = new JButton("Nuevo Ticket");
        botonActualizar = new JButton("Actualizar");
        botonVolver = new JButton("Volver al Menú");
    }
    
    /**
     * Crea las etiquetas informativas
     */
    private void crearEtiquetas() {
        labelEstadisticas = new JLabel("Cargando estadísticas...");
    }
    
    /**
     * Configura el diseño visual completo de la ventana
     */
    private void configurarDiseno() {
        setLayout(new BorderLayout());
        
        crearPanelSuperior();
        configurarTabla();
        crearPanelCentral();
        crearPanelInferior();
    }
    
    /**
     * Crea el panel superior con título y estadísticas
     */
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(NEGRO_CORPORATIVO);
        panelSuperior.setPreferredSize(new Dimension(0, 120));
        
        // Título principal
        JLabel etiquetaTitulo = new JLabel("SISTEMA DE SOPORTE", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        etiquetaTitulo.setForeground(AMARILLO_CORPORATIVO);
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        
        // Configurar etiqueta de estadísticas
        labelEstadisticas.setFont(new Font("Arial", Font.PLAIN, 14));
        labelEstadisticas.setForeground(BLANCO_CORPORATIVO);
        labelEstadisticas.setHorizontalAlignment(SwingConstants.CENTER);
        labelEstadisticas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        panelSuperior.add(etiquetaTitulo, BorderLayout.CENTER);
        panelSuperior.add(labelEstadisticas, BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    /**
     * Configura las propiedades visuales y funcionales de la tabla
     */
    private void configurarTabla() {
        configurarPropiedadesBasicas();
        configurarEncabezado();
        configurarAnchoColumnas();
        configurarRenderer();
    }
    
    /**
     * Configura las propiedades visuales básicas de la tabla
     */
    private void configurarPropiedadesBasicas() {
        tablaTickets.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaTickets.setRowHeight(32);
        tablaTickets.setGridColor(AMARILLO_CORPORATIVO);
        tablaTickets.setSelectionBackground(AMARILLO_CLARO);
        tablaTickets.setSelectionForeground(NEGRO_CORPORATIVO);
        tablaTickets.setBackground(BLANCO_CORPORATIVO);
    }
    
    /**
     * Configura el aspecto del encabezado de la tabla
     */
    private void configurarEncabezado() {
        tablaTickets.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaTickets.getTableHeader().setBackground(AMARILLO_CORPORATIVO);
        tablaTickets.getTableHeader().setForeground(NEGRO_CORPORATIVO);
        tablaTickets.getTableHeader().setBorder(BorderFactory.createLineBorder(NEGRO_CORPORATIVO));
    }
    
    /**
     * Establece el ancho preferido para cada columna
     */
    private void configurarAnchoColumnas() {
        tablaTickets.getColumnModel().getColumn(0).setPreferredWidth(100); // ID Ticket
        tablaTickets.getColumnModel().getColumn(1).setPreferredWidth(150); // Cliente
        tablaTickets.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        tablaTickets.getColumnModel().getColumn(3).setPreferredWidth(300); // Asunto
        tablaTickets.getColumnModel().getColumn(4).setPreferredWidth(100); // Prioridad
        tablaTickets.getColumnModel().getColumn(5).setPreferredWidth(120); // Estado
        tablaTickets.getColumnModel().getColumn(6).setPreferredWidth(130); // Fecha
    }
    
    /**
     * Configura el renderer personalizado para colorear celdas
     */
    private void configurarRenderer() {
        tablaTickets.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            /**
             * Configura el color de fondo alternado para las filas
             * @param c Componente a configurar
             * @param row Número de fila
             */
            private void configurarColorFila(Component c, int row) {
                if (row % 2 == 0) {
                    c.setBackground(BLANCO_CORPORATIVO);
                } else {
                    c.setBackground(new Color(255, 252, 230)); // Amarillo muy claro
                }
            }
            
            /**
             * Configura colores especiales para columnas de prioridad y estado
             * @param c Componente a configurar
             * @param column Número de columna
             * @param value Valor de la celda
             */
            private void configurarColorEspecial(Component c, int column, Object value) {
                if (column == 4) { // Prioridad
                    String prioridad = (String) value;
                    c.setForeground(obtenerColorPrioridad(prioridad));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else if (column == 5) { // Estado
                    String estado = (String) value;
                    c.setForeground(obtenerColorEstado(estado));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(NEGRO_CORPORATIVO);
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
            }
        });
    }
    
    /**
     * Crea el panel central que contiene la tabla con scroll
     */
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(BLANCO_CORPORATIVO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JScrollPane scrollTabla = new JScrollPane(tablaTickets);
        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 2),
                "Tickets de Soporte - Doble click para abrir chat",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                NEGRO_CORPORATIVO
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollTabla.getViewport().setBackground(BLANCO_CORPORATIVO);
        
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel inferior con botones de acción
     */
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelInferior.setBackground(AMARILLO_CLARO);
        panelInferior.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, AMARILLO_CORPORATIVO));
        panelInferior.setPreferredSize(new Dimension(0, 80));
        
        // Configurar estilo de botones
        configurarBotonPrincipal(botonNuevoTicket);
        configurarBotonSecundario(botonActualizar);
        configurarBotonVolver(botonVolver);
        
        panelInferior.add(botonNuevoTicket);
        panelInferior.add(botonActualizar);
        panelInferior.add(botonVolver);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Configura un botón principal con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonPrincipal(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(AMARILLO_CORPORATIVO);
        boton.setForeground(NEGRO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(140, 40));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
    
    /**
     * Configura un botón secundario con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonSecundario(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(BLANCO_CORPORATIVO);
        boton.setForeground(NEGRO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(120, 40));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
    
    /**
     * Configura el botón de volver con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonVolver(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(NEGRO_CORPORATIVO);
        boton.setForeground(AMARILLO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(130, 40));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
    
    /**
     * Configura todos los eventos de la ventana
     */
    private void configurarEventos() {
        configurarEventosTabla();
        configurarEventosBotones();
    }
    
    /**
     * Configura los eventos de la tabla (doble click para abrir chat)
     */
    private void configurarEventosTabla() {
        tablaTickets.addMouseListener(new MouseAdapter() {
            /**
             * Maneja el evento de doble click en la tabla
             * @param e Evento del mouse
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirChatTicketSeleccionado();
                }
            }
        });
    }
    
    /**
     * Configura los eventos de todos los botones
     */
    private void configurarEventosBotones() {
        // Evento botón nuevo ticket
        botonNuevoTicket.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón nuevo ticket
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirNuevoTicket();
            }
        });
        
        // Evento botón actualizar
        botonActualizar.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón actualizar
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDatos();
            }
        });
        
        // Evento botón volver
        botonVolver.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón volver
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlMenuPrincipal();
            }
        });
    }
    
    /**
     * Abre el chat del ticket seleccionado en la tabla
     */
    private void abrirChatTicketSeleccionado() {
        int filaSeleccionada = tablaTickets.getSelectedRow();
        if (filaSeleccionada != -1) {
            String ticketId = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            abrirChatTicket(ticketId);
        }
    }
    
    /**
     * Actualiza todos los datos de la ventana
     */
    private void actualizarDatos() {
        cargarTickets();
        actualizarEstadisticas();
        JOptionPane.showMessageDialog(this, 
            "Tickets actualizados correctamente", "Actualizado", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Cierra la ventana actual y regresa al menú principal
     */
    private void volverAlMenuPrincipal() {
        dispose();
        new VentanaMenuPrincipal().setVisible(true);
    }
    
    /**
     * Carga todos los tickets desde la base de datos y los muestra en la tabla
     */
    private void cargarTickets() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Obtener tickets de la base de datos
        List<TicketInfo> tickets = ticketDAO.obtenerTodosLosTickets();
        
        // Llenar tabla con datos
        for (TicketInfo ticket : tickets) {
            Object[] fila = {
                ticket.getTicketId(),
                ticket.getNombreCliente(),
                ticket.getEmailCliente(),
                ticket.getAsunto(),
                ticket.getPrioridad().toUpperCase(),
                ticket.getEstado().replace("_", " ").toUpperCase(),
                formatearFecha(ticket.getFechaCreacion())
            };
            modeloTabla.addRow(fila);
        }
    }
    
    /**
     * Actualiza las estadísticas de tickets mostradas en el panel superior
     */
    private void actualizarEstadisticas() {
        int abiertos = ticketDAO.contarTicketsPorEstado("abierto");
        int enProceso = ticketDAO.contarTicketsPorEstado("en_proceso");
        int resueltos = ticketDAO.contarTicketsPorEstado("resuelto");
        int cerrados = ticketDAO.contarTicketsPorEstado("cerrado");
        int total = abiertos + enProceso + resueltos + cerrados;
        
        String estadisticas = String.format(
            "Total: %d | Abiertos: %d | En Proceso: %d | Resueltos: %d | Cerrados: %d",
            total, abiertos, enProceso, resueltos, cerrados
        );
        labelEstadisticas.setText(estadisticas);
    }
    
    /**
     * Abre la ventana de chat para un ticket específico
     * @param ticketId ID del ticket a abrir
     */
    private void abrirChatTicket(String ticketId) {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Ejecuta la apertura del chat en el hilo de eventos
             */
            @Override
            public void run() {
                new VentanaChatTicket(ticketId).setVisible(true);
            }
        });
    }
    
    /**
     * Abre la ventana para crear un nuevo ticket
     */
    private void abrirNuevoTicket() {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Ejecuta la apertura del formulario nuevo ticket en el hilo de eventos
             */
            @Override
            public void run() {
                VentanaNuevoTicket ventanaNuevo = new VentanaNuevoTicket(VentanaSoporte.this);
                ventanaNuevo.setVisible(true);
            }
        });
    }
    
    /**
     * Formatea un timestamp para mostrar en la tabla
     * @param timestamp Fecha a formatear
     * @return Fecha formateada como string o "Sin fecha" si es null
     */
    private String formatearFecha(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formato.format(timestamp);
        }
        return "Sin fecha";
    }
    
    /**
     * Obtiene el color apropiado según la prioridad del ticket
     * @param prioridad Prioridad del ticket
     * @return Color correspondiente a la prioridad
     */
    private Color obtenerColorPrioridad(String prioridad) {
        switch (prioridad.toLowerCase()) {
            case "urgente": return new Color(220, 53, 69);   // Rojo
            case "alta": return new Color(255, 143, 0);      // Naranja
            case "media": return NEGRO_CORPORATIVO;          // Negro corporativo
            case "baja": return new Color(40, 167, 69);      // Verde
            default: return NEGRO_CORPORATIVO;
        }
    }
    
    /**
     * Obtiene el color apropiado según el estado del ticket
     * @param estado Estado del ticket
     * @return Color correspondiente al estado
     */
    private Color obtenerColorEstado(String estado) {
        switch (estado.toLowerCase().replace(" ", "_")) {
            case "abierto": return new Color(40, 167, 69);      // Verde
            case "en_proceso": return new Color(255, 143, 0);   // Naranja
            case "resuelto": return new Color(0, 123, 255);     // Azul
            case "cerrado": return new Color(108, 117, 125);    // Gris
            default: return NEGRO_CORPORATIVO;
        }
    }
    
    /**
     * Método público para actualizar la tabla desde otras ventanas
     * Útil cuando se crea un nuevo ticket desde otra ventana
     */
    public void actualizarTabla() {
        cargarTickets();
        actualizarEstadisticas();
    }
}