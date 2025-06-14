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
 * Ventana principal de gestión de stock con estilo corporativo
 * Permite ver, crear, editar y eliminar productos del inventario
 */
public class VentanaStock extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private GestorProductos productoDAO;
    private JButton btnNuevo, btnEditar, btnEliminar, btnVolver;
    
    // Colores corporativos de la empresa
    private static final Color AMARILLO_CORPORATIVO = new Color(255, 215, 0);
    private static final Color NEGRO_CORPORATIVO = new Color(35, 35, 35);
    private static final Color BLANCO_CORPORATIVO = new Color(250, 250, 250);
    private static final Color AMARILLO_CLARO = new Color(255, 245, 157);
    
    /**
     * Constructor principal que inicializa la ventana de stock
     * Configura componentes, carga datos iniciales
     */
    public VentanaStock() {
        productoDAO = new GestorProductos();
        inicializar();
        cargarDatos();
    }
    
    /**
     * Inicializa todos los componentes de la ventana
     * Configura layout, tabla, botones y paneles principales
     */
    private void inicializar() {
        configurarVentanaPrincipal();
        crearPanelSuperior();
        configurarTabla();
        crearPanelCentral();
        crearPanelBotones();
        configurarEventos();
    }
    
    /**
     * Configura las propiedades básicas de la ventana principal
     */
    private void configurarVentanaPrincipal() {
        setTitle("Gestión de Stock - Fábrica de Libros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }
    
    /**
     * Crea y configura el panel superior con título
     */
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(NEGRO_CORPORATIVO);
        panelSuperior.setPreferredSize(new Dimension(0, 100));
        
        JLabel etiquetaTitulo = new JLabel("GESTIÓN DE STOCK", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        etiquetaTitulo.setForeground(AMARILLO_CORPORATIVO);
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        panelSuperior.add(etiquetaTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    /**
     * Configura la tabla de productos con modelo y renderizado personalizado
     */
    private void configurarTabla() {
        crearModeloTabla();
        configurarPropiedadesTabla();
        configurarEncabezadoTabla();
        configurarAnchoColumnas();
        configurarRendererPersonalizado();
    }
    
    /**
     * Crea el modelo de datos para la tabla
     */
    private void crearModeloTabla() {
        String[] columnas = {"ID", "Nombre", "Precio", "Cantidad", "Última Modificación"};
        modelo = new DefaultTableModel(columnas, 0) {
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
        tabla = new JTable(modelo);
    }
    
    /**
     * Configura las propiedades visuales básicas de la tabla
     */
    private void configurarPropiedadesTabla() {
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(30);
        tabla.setGridColor(AMARILLO_CORPORATIVO);
        tabla.setSelectionForeground(NEGRO_CORPORATIVO);
        tabla.setBackground(BLANCO_CORPORATIVO);
        tabla.setSelectionBackground(AMARILLO_CLARO);
    }
    
    /**
     * Configura el aspecto visual del encabezado de la tabla
     */
    private void configurarEncabezadoTabla() {
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(AMARILLO_CORPORATIVO);
        tabla.getTableHeader().setForeground(NEGRO_CORPORATIVO);
        tabla.getTableHeader().setBorder(BorderFactory.createLineBorder(NEGRO_CORPORATIVO));
    }
    
    /**
     * Establece el ancho preferido para cada columna de la tabla
     */
    private void configurarAnchoColumnas() {
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(300);  // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);  // Precio
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);  // Cantidad
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150);  // Última Modificación
    }
    
    /**
     * Configura el renderer personalizado para colorear filas alternadas
     */
    private void configurarRendererPersonalizado() {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            /**
             * Personaliza la apariencia de cada celda de la tabla
             * @param table Tabla que contiene la celda
             * @param value Valor de la celda
             * @param isSelected Si la celda está seleccionada
             * @param hasFocus Si la celda tiene el foco
             * @param row Fila de la celda
             * @param column Columna de la celda
             * @return Componente configurado para renderizar la celda
             */
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    // Alternar colores de filas
                    if (row % 2 == 0) {
                        c.setBackground(BLANCO_CORPORATIVO);
                    } else {
                        c.setBackground(new Color(255, 252, 230)); // Amarillo muy claro
                    }
                    c.setForeground(NEGRO_CORPORATIVO);
                    
                    // Resaltar columnas importantes con negrita
                    if (column == 2 || column == 3) { // Precio y Cantidad
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        setFont(getFont().deriveFont(Font.PLAIN));
                    }
                }
                
                return c;
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
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 2),
                "Productos en Stock",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                NEGRO_CORPORATIVO
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scroll.getViewport().setBackground(BLANCO_CORPORATIVO);
        
        panelCentral.add(scroll, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de botones con estilo corporativo
     */
    private void crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotones.setBackground(AMARILLO_CLARO);
        panelBotones.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, AMARILLO_CORPORATIVO));
        panelBotones.setPreferredSize(new Dimension(0, 80));
        
        // Inicializar botones
        btnNuevo = new JButton("Nuevo Producto");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnVolver = new JButton("Volver al Menú");
        
        // Configurar estilo de cada botón
        configurarBotonPrincipal(btnNuevo);
        configurarBotonSecundario(btnEditar);
        configurarBotonEliminar(btnEliminar);
        configurarBotonVolver(btnVolver);
        
        // Agregar botones al panel
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);
        
        add(panelBotones, BorderLayout.SOUTH);
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
        boton.setPreferredSize(new Dimension(100, 40));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
    
    /**
     * Configura el botón de eliminar con color de advertencia
     * @param boton Botón a configurar
     */
    private void configurarBotonEliminar(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(new Color(220, 53, 69)); // Rojo para eliminar
        boton.setForeground(BLANCO_CORPORATIVO);
        boton.setPreferredSize(new Dimension(100, 40));
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
     * Asocia listeners a botones y tabla
     */
    private void configurarEventos() {
        configurarEventosTabla();
        configurarEventosBotones();
    }
    
    /**
     * Configura los eventos de la tabla (doble click para editar)
     */
    private void configurarEventosTabla() {
        tabla.addMouseListener(new MouseAdapter() {
            /**
             * Maneja el evento de doble click en la tabla
             * @param e Evento del mouse
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarProducto();
                }
            }
        });
    }
    
    /**
     * Configura los eventos de todos los botones
     */
    private void configurarEventosBotones() {
        // Evento botón nuevo producto
        btnNuevo.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón nuevo producto
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioNuevoProducto();
            }
        });
        
        // Evento botón editar
        btnEditar.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón editar
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProducto();
            }
        });
        
        // Evento botón eliminar
        btnEliminar.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón eliminar
             * @param e Evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });
        
        // Evento botón volver
        btnVolver.addActionListener(new ActionListener() {
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
     * Carga todos los productos desde la base de datos y los muestra en la tabla
     * Limpia la tabla antes de cargar los nuevos datos
     */
    public void cargarDatos() {
        modelo.setRowCount(0); // Limpiar tabla
        List<Producto> productos = productoDAO.obtenerTodos();
        
        for (Producto p : productos) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                String.format("€%.2f", p.getPrecio()),
                p.getCantidad(),
                p.getFechaFormateada()
            };
            modelo.addRow(fila);
        }
    }
    
    /**
     * Abre el formulario para crear un nuevo producto
     */
    private void abrirFormularioNuevoProducto() {
        new VentanaFormularioProducto(this, null).setVisible(true);
    }
    
    /**
     * Edita el producto seleccionado en la tabla
     * Verifica que haya una fila seleccionada antes de proceder
     */
    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarMensajeAdvertencia("Seleccione un producto para editar");
            return;
        }
        
        Producto producto = obtenerProductoDeFilaSeleccionada(fila);
        if (producto != null) {
            new VentanaFormularioProducto(this, producto).setVisible(true);
        }
    }
    
    /**
     * Obtiene un objeto Producto desde los datos de la fila seleccionada
     * @param fila Índice de la fila seleccionada
     * @return Objeto Producto con los datos de la fila o null si hay error
     */
    private Producto obtenerProductoDeFilaSeleccionada(int fila) {
        try {
            int id = (Integer) modelo.getValueAt(fila, 0);
            String nombre = (String) modelo.getValueAt(fila, 1);
            String precioStr = (String) modelo.getValueAt(fila, 2);
            double precio = Double.parseDouble(precioStr.replace("€", "").replace(",", "."));
            int cantidad = (Integer) modelo.getValueAt(fila, 3);
            
            Producto producto = new Producto(nombre, precio, cantidad);
            producto.setId(id);
            return producto;
        } catch (Exception e) {
            mostrarMensajeError("Error al obtener datos del producto: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Elimina el producto seleccionado después de solicitar confirmación
     * Verifica que haya una fila seleccionada antes de proceder
     */
    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarMensajeAdvertencia("Seleccione un producto para eliminar");
            return;
        }
        
        String nombre = (String) modelo.getValueAt(fila, 1);
        if (confirmarEliminacion(nombre)) {
            ejecutarEliminacion(fila);
        }
    }
    
    /**
     * Solicita confirmación al usuario para eliminar un producto
     * @param nombreProducto Nombre del producto a eliminar
     * @return true si el usuario confirma, false en caso contrario
     */
    private boolean confirmarEliminacion(String nombreProducto) {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el producto:\n" + nombreProducto + "?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        return confirmacion == JOptionPane.YES_OPTION;
    }
    
    /**
     * Ejecuta la eliminación del producto en la base de datos
     * @param fila Fila del producto a eliminar
     */
    private void ejecutarEliminacion(int fila) {
        int id = (Integer) modelo.getValueAt(fila, 0);
        if (productoDAO.eliminar(id)) {
            mostrarMensajeExito("Producto eliminado correctamente");
            cargarDatos(); // Recargar tabla
        } else {
            mostrarMensajeError("Error al eliminar el producto");
        }
    }
    
    /**
     * Cierra la ventana actual y regresa al menú principal
     */
    private void volverAlMenuPrincipal() {
        dispose();
        new VentanaMenuPrincipal().setVisible(true);
    }
    
    /**
     * Muestra un mensaje de advertencia al usuario
     * @param mensaje Texto del mensaje
     */
    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, 
            "Ningún producto seleccionado", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Muestra un mensaje de éxito al usuario
     * @param mensaje Texto del mensaje
     */
    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, 
            "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Muestra un mensaje de error al usuario
     * @param mensaje Texto del mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}