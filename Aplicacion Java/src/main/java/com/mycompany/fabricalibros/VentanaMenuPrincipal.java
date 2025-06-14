package com.mycompany.fabricalibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana del menú principal con estilo corporativo
 * Punto central de navegación del sistema con acceso a módulos principales
 */
public class VentanaMenuPrincipal extends JFrame {
    private JButton botonSoporte;
    private JButton botonStock;
    private JButton botonCerrarSesion;
    
    // Colores corporativos de la empresa
    private static final Color AMARILLO_CORPORATIVO = new Color(255, 215, 0);
    private static final Color NEGRO_CORPORATIVO = new Color(35, 35, 35);
    private static final Color BLANCO_CORPORATIVO = new Color(250, 250, 250);
    private static final Color AMARILLO_CLARO = new Color(255, 245, 157);
    
    /**
     * Constructor principal que inicializa la ventana del menú
     * Configura todos los componentes y su comportamiento
     */
    public VentanaMenuPrincipal() {
        inicializarComponentes();
        configurarDiseno();
        configurarEventos();
    }
    
    /**
     * Inicializa todos los componentes de la ventana
     * Establece propiedades básicas como título, tamaño y botones
     */
    private void inicializarComponentes() {
        setTitle("Menú Principal - Fábrica de Libros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        
        botonSoporte = new JButton("SOPORTE");
        botonStock = new JButton("STOCK");
        botonCerrarSesion = new JButton("Cerrar Sesión");
    }
    
    /**
     * Configura el diseño visual completo de la ventana
     * Establece layouts, colores y distribución de componentes
     */
    private void configurarDiseno() {
        setLayout(new BorderLayout());
        
        // Crear y configurar cada sección de la ventana
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();
    }
    
    /**
     * Crea y configura el panel superior con título y subtítulo
     */
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(NEGRO_CORPORATIVO);
        panelSuperior.setPreferredSize(new Dimension(0, 150));
        
        // Título principal
        JLabel etiquetaTitulo = new JLabel("FÁBRICA DE LIBROS", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        etiquetaTitulo.setForeground(AMARILLO_CORPORATIVO);
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        
        // Subtítulo
        JLabel subtitulo = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitulo.setForeground(BLANCO_CORPORATIVO);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        panelSuperior.add(etiquetaTitulo, BorderLayout.CENTER);
        panelSuperior.add(subtitulo, BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    /**
     * Crea y configura el panel central con los botones principales
     */
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(BLANCO_CORPORATIVO);
        GridBagConstraints restricciones = new GridBagConstraints();
        
        // Contenedor para los botones principales
        JPanel contenedorBotones = new JPanel(new GridLayout(1, 2, 80, 0));
        contenedorBotones.setBackground(BLANCO_CORPORATIVO);
        contenedorBotones.setPreferredSize(new Dimension(800, 200));
        
        // Configurar botones principales
        configurarBotonPrincipal(botonSoporte, "SOPORTE TÉCNICO", "Gestión de tickets y atención al cliente");
        configurarBotonPrincipal(botonStock, "GESTIÓN DE STOCK", "Control de inventario y productos");
        
        contenedorBotones.add(botonSoporte);
        contenedorBotones.add(botonStock);
        
        // Centrar los botones en el panel
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.CENTER;
        panelCentral.add(contenedorBotones, restricciones);
        
        add(panelCentral, BorderLayout.CENTER);
    }
    
    /**
     * Crea y configura el panel inferior con información y botón de salida
     */
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(AMARILLO_CLARO);
        panelInferior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(3, 0, 0, 0, AMARILLO_CORPORATIVO),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelInferior.setPreferredSize(new Dimension(0, 80));
        
        // Panel para el botón de cerrar sesión
        JPanel panelBotonCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonCerrar.setBackground(AMARILLO_CLARO);
        
        configurarBotonSecundario(botonCerrarSesion);
        panelBotonCerrar.add(botonCerrarSesion);
        
        // Información corporativa
        JLabel etiquetaInfo = new JLabel("© 2024 Fábrica de Libros - Sistema de Gestión Empresarial");
        etiquetaInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        etiquetaInfo.setForeground(NEGRO_CORPORATIVO);
        
        panelInferior.add(etiquetaInfo, BorderLayout.WEST);
        panelInferior.add(panelBotonCerrar, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Configura un botón principal con diseño corporativo y efectos
     * @param boton Botón a configurar
     * @param titulo Título principal del botón
     * @param descripcion Texto descriptivo del botón
     */
    private void configurarBotonPrincipal(JButton boton, String titulo, String descripcion) {
        boton.setLayout(new BorderLayout());
        boton.setFont(new Font("Arial", Font.BOLD, 24));
        boton.setBackground(AMARILLO_CORPORATIVO);
        boton.setForeground(NEGRO_CORPORATIVO);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Crear panel interno con etiquetas
        JPanel panelInterno = crearPanelInternoBoton(titulo, descripcion);
        boton.removeAll();
        boton.add(panelInterno);
        
        // Configurar borde del botón
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 3),
            BorderFactory.createRaisedBevelBorder()
        ));
        
        // Agregar efecto hover
        agregarEfectoHover(boton, panelInterno);
    }
    
    /**
     * Crea el panel interno de un botón principal con título y descripción
     * @param titulo Título del botón
     * @param descripcion Descripción del botón
     * @return Panel configurado con las etiquetas
     */
    private JPanel crearPanelInternoBoton(String titulo, String descripcion) {
        JPanel panelInterno = new JPanel(new BorderLayout());
        panelInterno.setBackground(AMARILLO_CORPORATIVO);
        panelInterno.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel labelTitulo = new JLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setForeground(NEGRO_CORPORATIVO);
        
        JLabel labelDescripcion = new JLabel(descripcion, SwingConstants.CENTER);
        labelDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        labelDescripcion.setForeground(NEGRO_CORPORATIVO);
        
        panelInterno.add(labelTitulo, BorderLayout.CENTER);
        panelInterno.add(labelDescripcion, BorderLayout.SOUTH);
        
        return panelInterno;
    }
    
    /**
     * Agrega efecto hover a un botón principal
     * @param boton Botón al que agregar el efecto
     * @param panelInterno Panel interno del botón para cambiar color
     */
    private void agregarEfectoHover(JButton boton, JPanel panelInterno) {
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Maneja el evento cuando el mouse entra al botón
             * @param evt Evento del mouse
             */
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(AMARILLO_CLARO);
                panelInterno.setBackground(AMARILLO_CLARO);
            }
            
            /**
             * Maneja el evento cuando el mouse sale del botón
             * @param evt Evento del mouse
             */
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(AMARILLO_CORPORATIVO);
                panelInterno.setBackground(AMARILLO_CORPORATIVO);
            }
        });
    }
    
    /**
     * Configura un botón secundario con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonSecundario(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setPreferredSize(new Dimension(130, 35));
        boton.setBackground(NEGRO_CORPORATIVO);
        boton.setForeground(AMARILLO_CORPORATIVO);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
    
    /**
     * Configura todos los eventos de los botones
     * Asocia listeners a cada botón de la ventana
     */
    private void configurarEventos() {
        // Evento del botón soporte
        botonSoporte.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón soporte
             * @param evento Evento de acción del botón
             */
            @Override
            public void actionPerformed(ActionEvent evento) {
                abrirModuloSoporte();
            }
        });
        
        // Evento del botón stock
        botonStock.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón stock
             * @param evento Evento de acción del botón
             */
            @Override
            public void actionPerformed(ActionEvent evento) {
                abrirModuloStock();
            }
        });
        
        // Evento del botón cerrar sesión
        botonCerrarSesion.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón cerrar sesión
             * @param evento Evento de acción del botón
             */
            @Override
            public void actionPerformed(ActionEvent evento) {
                cerrarSesionUsuario();
            }
        });
    }
    
    /**
     * Abre el módulo de soporte y cierra la ventana actual
     * Navega a la gestión de tickets de soporte
     */
    private void abrirModuloSoporte() {
        this.dispose();
        new VentanaSoporte().setVisible(true);
    }
    
    /**
     * Abre el módulo de stock y cierra la ventana actual
     * Navega a la gestión de inventario y productos
     */
    private void abrirModuloStock() {
        this.dispose();
        new VentanaStock().setVisible(true);
    }
    
    /**
     * Procesa el cierre de sesión del usuario
     * Solicita confirmación y regresa al login si es confirmado
     */
    private void cerrarSesionUsuario() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea cerrar sesión?",
            "Confirmar cierre de sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
            regresarAInicioSesion();
        }
    }
    
    /**
     * Regresa a la ventana de inicio de sesión
     * Se ejecuta después de cerrar sesión
     */
    private void regresarAInicioSesion() {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Ejecuta la apertura de la ventana de login en el hilo de eventos
             */
            @Override
            public void run() {
                new VentanaInicioSesion().setVisible(true);
            }
        });
    }
}