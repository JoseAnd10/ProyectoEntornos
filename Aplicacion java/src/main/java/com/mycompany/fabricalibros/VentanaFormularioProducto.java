package com.mycompany.fabricalibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana de formulario para crear y editar productos con estilo corporativo
 * Permite tanto la creación de nuevos productos como la edición de existentes
 */
public class VentanaFormularioProducto extends JDialog {
    private JTextField campoNombre, campoPrecio, campoCantidad;
    private JButton btnGuardar, btnCancelar;
    private GestorProductos productoDAO;
    private VentanaStock ventanaPadre;
    private Producto producto;
    private boolean esEdicion;
    
    // Colores corporativos de la empresa
    private static final Color AMARILLO_CORPORATIVO = new Color(255, 215, 0);
    private static final Color NEGRO_CORPORATIVO = new Color(35, 35, 35);
    private static final Color BLANCO_CORPORATIVO = new Color(250, 250, 250);
    private static final Color AMARILLO_CLARO = new Color(255, 245, 157);
    
    /**
     * Constructor principal del formulario de productos
     * @param padre Ventana padre que abrió este formulario
     * @param producto Producto a editar (null para crear nuevo)
     */
    public VentanaFormularioProducto(VentanaStock padre, Producto producto) {
        super(padre, producto == null ? "Nuevo Producto" : "Editar Producto", true);
        this.ventanaPadre = padre;
        this.producto = producto;
        this.esEdicion = producto != null;
        this.productoDAO = new GestorProductos();
        
        inicializar();
        if (esEdicion) cargarDatos();
    }
    
    /**
     * Inicializa todos los componentes y el diseño del formulario
     */
    private void inicializar() {
        configurarVentana();
        crearComponentes();
        configurarDiseno();
        configurarEventos();
    }
    
    /**
     * Configura las propiedades básicas de la ventana
     */
    private void configurarVentana() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
    }
    
    /**
     * Crea todos los componentes del formulario
     */
    private void crearComponentes() {
        campoNombre = new JTextField(20);
        campoPrecio = new JTextField(20);
        campoCantidad = new JTextField(20);
        btnGuardar = new JButton(esEdicion ? "ACTUALIZAR" : "CREAR");
        btnCancelar = new JButton("CANCELAR");
    }
    
    /**
     * Configura el diseño visual completo del formulario
     */
    private void configurarDiseno() {
        crearPanelPrincipal();
        crearPanelBotones();
    }
    
    /**
     * Crea el panel principal del formulario con todos los campos
     */
    private void crearPanelPrincipal() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(BLANCO_CORPORATIVO);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        agregarTitulo(panelPrincipal, gbc);
        agregarSeparador(panelPrincipal, gbc);
        agregarCamposFormulario(panelPrincipal, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    /**
     * Agrega el título principal al formulario
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarTitulo(JPanel panel, GridBagConstraints gbc) {
        JLabel etiquetaTitulo = new JLabel(esEdicion ? "EDITAR PRODUCTO" : "NUEVO PRODUCTO");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        etiquetaTitulo.setForeground(NEGRO_CORPORATIVO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 15, 30, 15);
        panel.add(etiquetaTitulo, gbc);
    }
    
    /**
     * Agrega una línea separadora decorativa
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarSeparador(JPanel panel, GridBagConstraints gbc) {
        JSeparator separador = new JSeparator();
        separador.setForeground(AMARILLO_CORPORATIVO);
        separador.setBackground(AMARILLO_CORPORATIVO);
        gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 15, 25, 15);
        panel.add(separador, gbc);
    }
    
    /**
     * Agrega todos los campos del formulario
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCamposFormulario(JPanel panel, GridBagConstraints gbc) {
        // Restaurar configuración normal
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 15, 15, 15);
        
        agregarCampoNombre(panel, gbc);
        agregarCampoPrecio(panel, gbc);
        agregarCampoCantidad(panel, gbc);
    }
    
    /**
     * Agrega el campo de nombre del producto
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoNombre(JPanel panel, GridBagConstraints gbc) {
        // Etiqueta
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre del Producto:");
        configurarEtiqueta(lblNombre);
        panel.add(lblNombre, gbc);
        
        // Campo
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        configurarCampoTexto(campoNombre);
        panel.add(campoNombre, gbc);
    }
    
    /**
     * Agrega el campo de precio del producto
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoPrecio(JPanel panel, GridBagConstraints gbc) {
        // Etiqueta
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblPrecio = new JLabel("Precio (€):");
        configurarEtiqueta(lblPrecio);
        panel.add(lblPrecio, gbc);
        
        // Campo
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        configurarCampoTexto(campoPrecio);
        panel.add(campoPrecio, gbc);
    }
    
    /**
     * Agrega el campo de cantidad del producto
     * @param panel Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoCantidad(JPanel panel, GridBagConstraints gbc) {
        // Etiqueta
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblCantidad = new JLabel("Cantidad en Stock:");
        configurarEtiqueta(lblCantidad);
        panel.add(lblCantidad, gbc);
        
        // Campo
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        configurarCampoTexto(campoCantidad);
        panel.add(campoCantidad, gbc);
    }
    
    /**
     * Crea el panel de botones con estilo corporativo
     */
    private void crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(AMARILLO_CLARO);
        panelBotones.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, AMARILLO_CORPORATIVO));
        
        configurarBotonPrincipal(btnGuardar);
        configurarBotonSecundario(btnCancelar);
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Configura el aspecto visual de una etiqueta
     * @param etiqueta Etiqueta a configurar
     */
    private void configurarEtiqueta(JLabel etiqueta) {
        etiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        etiqueta.setForeground(NEGRO_CORPORATIVO);
    }
    
    /**
     * Configura el aspecto visual de un campo de texto
     * @param campo Campo de texto a configurar
     */
    private void configurarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setBackground(BLANCO_CORPORATIVO);
    }
    
    /**
     * Configura un botón principal con estilo corporativo
     * @param boton Botón a configurar
     */
    private void configurarBotonPrincipal(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(AMARILLO_CORPORATIVO);
        boton.setForeground(NEGRO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(130, 40));
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
        boton.setPreferredSize(new Dimension(130, 40));
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
        configurarValidacionCampos();
    }
    
    /**
     * Configura los eventos de los botones
     */
    private void configurarEventosBotones() {
        // Evento botón guardar
        btnGuardar.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón guardar
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        
        // Evento botón cancelar
        btnCancelar.addActionListener(new ActionListener() {
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
     * Configura la validación en tiempo real para campos numéricos
     */
    private void configurarValidacionCampos() {
        // Validación para campo precio (solo números y punto decimal)
        campoPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            /**
             * Valida entrada de caracteres en el campo precio
             * @param evt Evento de teclado
             */
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
        
        // Validación para campo cantidad (solo números enteros)
        campoCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            /**
             * Valida entrada de caracteres en el campo cantidad
             * @param evt Evento de teclado
             */
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
    }
    
    /**
     * Carga los datos del producto en los campos del formulario
     * Solo se ejecuta en modo edición
     */
    private void cargarDatos() {
        if (producto != null) {
            campoNombre.setText(producto.getNombre());
            campoPrecio.setText(String.valueOf(producto.getPrecio()));
            campoCantidad.setText(String.valueOf(producto.getCantidad()));
        }
    }
    
    /**
     * Procesa el guardado del producto (crear o actualizar)
     * Realiza validaciones antes de guardar en la base de datos
     */
    private void guardar() {
        if (!validarCampos()) {
            return;
        }
        
        try {
            Producto p = prepararProducto();
            boolean exito = ejecutarGuardado(p);
            
            if (exito) {
                mostrarMensajeExito();
                actualizarVentanaPadre();
                dispose();
            } else {
                mostrarMensajeError("Error al guardar el producto. Inténtelo nuevamente.");
            }
            
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese valores numéricos válidos para precio y cantidad");
        }
    }
    
    /**
     * Prepara el objeto Producto con los datos del formulario
     * @return Producto configurado con los datos del formulario
     * @throws NumberFormatException si los valores numéricos no son válidos
     */
    private Producto prepararProducto() throws NumberFormatException {
        Producto p = esEdicion ? producto : new Producto();
        p.setNombre(campoNombre.getText().trim());
        p.setPrecio(Double.parseDouble(campoPrecio.getText().trim()));
        p.setCantidad(Integer.parseInt(campoCantidad.getText().trim()));
        return p;
    }
    
    /**
     * Ejecuta el guardado en la base de datos
     * @param p Producto a guardar
     * @return true si el guardado fue exitoso, false en caso contrario
     */
    private boolean ejecutarGuardado(Producto p) {
        if (esEdicion) {
            return productoDAO.actualizar(p);
        } else {
            return productoDAO.crear(p);
        }
    }
    
    /**
     * Muestra mensaje de éxito según el tipo de operación
     */
    private void mostrarMensajeExito() {
        String mensaje = esEdicion ? "Producto actualizado correctamente" : "Producto creado correctamente";
        JOptionPane.showMessageDialog(this, mensaje, 
            "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Actualiza los datos en la ventana padre
     */
    private void actualizarVentanaPadre() {
        if (ventanaPadre != null) {
            ventanaPadre.cargarDatos();
        }
    }
    
    /**
     * Valida todos los campos del formulario
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        return validarNombre() && validarPrecio() && validarCantidad();
    }
    
    /**
     * Valida el campo nombre del producto
     * @return true si el nombre es válido, false en caso contrario
     */
    private boolean validarNombre() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarMensajeError("El nombre del producto es obligatorio");
            campoNombre.requestFocus();
            return false;
        }
        return true;
    }
    
    /**
     * Valida el campo precio del producto
     * @return true si el precio es válido, false en caso contrario
     */
    private boolean validarPrecio() {
        String precioTexto = campoPrecio.getText().trim();
        if (precioTexto.isEmpty()) {
            mostrarMensajeError("El precio es obligatorio");
            campoPrecio.requestFocus();
            return false;
        }
        
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                mostrarMensajeError("El precio no puede ser negativo");
                campoPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese un precio válido");
            campoPrecio.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida el campo cantidad del producto
     * @return true si la cantidad es válida, false en caso contrario
     */
    private boolean validarCantidad() {
        String cantidadTexto = campoCantidad.getText().trim();
        if (cantidadTexto.isEmpty()) {
            mostrarMensajeError("La cantidad es obligatoria");
            campoCantidad.requestFocus();
            return false;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad < 0) {
                mostrarMensajeError("La cantidad no puede ser negativa");
                campoCantidad.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese una cantidad válida");
            campoCantidad.requestFocus();
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
}