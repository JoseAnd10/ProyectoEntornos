package com.mycompany.fabricalibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal de inicio de sesión con estilo corporativo
 * Maneja el login y registro de usuarios del sistema
 */
public class VentanaInicioSesion extends JFrame {
    private JTextField campoCorreoElectronico;
    private JPasswordField campoContrasena;
    private JButton botonIniciarSesion;
    private JButton botonRegistrarse;
    private Usuario gestorUsuarios;
    
    // Colores corporativos de la empresa
    private static final Color AMARILLO_CORPORATIVO = new Color(255, 215, 0);  // Dorado
    private static final Color NEGRO_CORPORATIVO = new Color(35, 35, 35);
    private static final Color BLANCO_CORPORATIVO = new Color(250, 250, 250);
    private static final Color AMARILLO_HOVER = new Color(255, 235, 59);
    
    /**
     * Constructor principal que inicializa la ventana de inicio de sesión
     * Configura todos los componentes y su comportamiento
     */
    public VentanaInicioSesion() {
        gestorUsuarios = new Usuario();
        inicializarComponentes();
        configurarDiseno();
        configurarEventos();
    }
    
    /**
     * Inicializa todos los componentes de la ventana
     * Configura propiedades básicas como título, tamaño y campos de entrada
     */
    private void inicializarComponentes() {
        setTitle("Sistema de Autenticación - Fábrica de Libros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        
        campoCorreoElectronico = new JTextField(25);
        campoContrasena = new JPasswordField(25);
        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonRegistrarse = new JButton("Registrarse");
    }
    
    /**
     * Configura el diseño visual de la ventana con estilo corporativo
     * Establece layouts, colores y posicionamiento de componentes
     */
    private void configurarDiseno() {
        setLayout(new BorderLayout());
        
        // Panel principal con fondo negro corporativo
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(NEGRO_CORPORATIVO);
        GridBagConstraints restricciones = new GridBagConstraints();
        
        // Contenedor del formulario con fondo blanco
        JPanel contenedorFormulario = new JPanel(new GridBagLayout());
        contenedorFormulario.setBackground(BLANCO_CORPORATIVO);
        contenedorFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AMARILLO_CORPORATIVO, 3),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        agregarElementosFormulario(contenedorFormulario);
        
        // Centrar el formulario en la pantalla
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(contenedorFormulario, restricciones);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    /**
     * Agrega todos los elementos del formulario al contenedor
     * @param contenedorFormulario Panel donde se agregan los componentes
     */
    private void agregarElementosFormulario(JPanel contenedorFormulario) {
        GridBagConstraints restriccionesForm = new GridBagConstraints();
        
        // Título principal
        agregarTitulo(contenedorFormulario, restriccionesForm);
        
        // Subtítulo
        agregarSubtitulo(contenedorFormulario, restriccionesForm);
        
        // Campo de correo electrónico
        agregarCampoCorreo(contenedorFormulario, restriccionesForm);
        
        // Campo de contraseña
        agregarCampoContrasena(contenedorFormulario, restriccionesForm);
        
        // Panel de botones
        agregarBotones(contenedorFormulario, restriccionesForm);
    }
    
    /**
     * Agrega el título principal al formulario
     * @param contenedor Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarTitulo(JPanel contenedor, GridBagConstraints gbc) {
        JLabel etiquetaTitulo = new JLabel("FÁBRICA DE LIBROS");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        etiquetaTitulo.setForeground(NEGRO_CORPORATIVO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 40, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        contenedor.add(etiquetaTitulo, gbc);
    }
    
    /**
     * Agrega el subtítulo al formulario
     * @param contenedor Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarSubtitulo(JPanel contenedor, GridBagConstraints gbc) {
        JLabel subtitulo = new JLabel("Sistema de Gestión Empresarial");
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitulo.setForeground(NEGRO_CORPORATIVO);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 30, 20);
        contenedor.add(subtitulo, gbc);
    }
    
    /**
     * Agrega la etiqueta y campo de correo electrónico
     * @param contenedor Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoCorreo(JPanel contenedor, GridBagConstraints gbc) {
        // Etiqueta
        JLabel etiquetaCorreo = new JLabel("Correo electrónico:");
        etiquetaCorreo.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaCorreo.setForeground(NEGRO_CORPORATIVO);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 20, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;
        contenedor.add(etiquetaCorreo, gbc);
        
        // Campo de texto
        configurarCampoTexto(campoCorreoElectronico);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contenedor.add(campoCorreoElectronico, gbc);
    }
    
    /**
     * Agrega la etiqueta y campo de contraseña
     * @param contenedor Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarCampoContrasena(JPanel contenedor, GridBagConstraints gbc) {
        // Etiqueta
        JLabel etiquetaContrasena = new JLabel("Contraseña:");
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaContrasena.setForeground(NEGRO_CORPORATIVO);
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 20, 8, 20);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        contenedor.add(etiquetaContrasena, gbc);
        
        // Campo de contraseña
        configurarCampoTexto(campoContrasena);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 20, 30, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contenedor.add(campoContrasena, gbc);
    }
    
    /**
     * Agrega el panel de botones al formulario
     * @param contenedor Panel contenedor
     * @param gbc Restricciones de GridBagLayout
     */
    private void agregarBotones(JPanel contenedor, GridBagConstraints gbc) {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        panelBotones.setBackground(BLANCO_CORPORATIVO);
        
        configurarBoton(botonIniciarSesion, true);
        configurarBoton(botonRegistrarse, false);
        
        panelBotones.add(botonIniciarSesion);
        panelBotones.add(botonRegistrarse);
        
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contenedor.add(panelBotones, gbc);
    }
    
    /**
     * Configura el aspecto visual de un campo de texto
     * @param campo Campo de texto a configurar
     */
    private void configurarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setBackground(BLANCO_CORPORATIVO);
    }
    
    /**
     * Configura el aspecto visual de un botón según su tipo
     * @param boton Botón a configurar
     * @param esPrincipal true para botón principal, false para secundario
     */
    private void configurarBoton(JButton boton, boolean esPrincipal) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(150, 45));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (esPrincipal) {
            // Botón principal: fondo amarillo, texto negro
            boton.setBackground(AMARILLO_CORPORATIVO);
            boton.setForeground(NEGRO_CORPORATIVO);
        } else {
            // Botón secundario: fondo negro, texto amarillo
            boton.setBackground(NEGRO_CORPORATIVO);
            boton.setForeground(AMARILLO_CORPORATIVO);
        }
        
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEGRO_CORPORATIVO, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
    
    /**
     * Configura todos los eventos de la ventana
     * Asocia listeners a botones y campos de entrada
     */
    private void configurarEventos() {
        // Evento del botón iniciar sesión
        botonIniciarSesion.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón iniciar sesión
             * @param evento Evento de acción del botón
             */
            @Override
            public void actionPerformed(ActionEvent evento) {
                procesarInicioSesion();
            }
        });
        
        // Evento del botón registrarse
        botonRegistrarse.addActionListener(new ActionListener() {
            /**
             * Maneja el click del botón registrarse
             * @param evento Evento de acción del botón
             */
            @Override
            public void actionPerformed(ActionEvent evento) {
                procesarRegistroUsuario();
            }
        });
        
        // Enter en el campo de contraseña ejecuta el login
        campoContrasena.addActionListener(new ActionListener() {
            /**
             * Maneja la tecla Enter en el campo de contraseña
             * @param e Evento de acción del campo
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarInicioSesion();
            }
        });
    }
    
    /**
     * Procesa el intento de inicio de sesión del usuario
     * Valida los datos y ejecuta el login si son correctos
     */
    private void procesarInicioSesion() {
        String correoIngresado = campoCorreoElectronico.getText().trim();
        String contrasenaIngresada = new String(campoContrasena.getPassword());
        
        // Validar que los campos no estén vacíos
        if (!validarCamposCompletos(correoIngresado, contrasenaIngresada)) {
            return;
        }
        
        // Validar formato del email
        if (!validarFormatoCorreo(correoIngresado)) {
            mostrarMensajeError("Por favor, ingrese un correo electrónico válido.");
            return;
        }
        
        // Intentar iniciar sesión
        if (gestorUsuarios.verificarInicioSesion(correoIngresado, contrasenaIngresada)) {
            mostrarMensajeExito("¡Inicio de sesión exitoso!");
            abrirMenuPrincipal();
            this.dispose();
        } else {
            mostrarMensajeError("Correo o contraseña incorrectos.");
        }
    }
    
    /**
     * Procesa el registro de un nuevo usuario
     * Valida los datos y crea la cuenta si todo es correcto
     */
    private void procesarRegistroUsuario() {
        String correoIngresado = campoCorreoElectronico.getText().trim();
        String contrasenaIngresada = new String(campoContrasena.getPassword());
        
        // Validar que los campos no estén vacíos
        if (!validarCamposCompletos(correoIngresado, contrasenaIngresada)) {
            return;
        }
        
        // Validar formato del email
        if (!validarFormatoCorreo(correoIngresado)) {
            mostrarMensajeError("Por favor, ingrese un correo electrónico válido.");
            return;
        }
        
        // Validar longitud mínima de contraseña
        if (!validarLongitudContrasena(contrasenaIngresada)) {
            mostrarMensajeError("La contraseña debe tener al menos 6 caracteres.");
            return;
        }
        
        // Verificar que el email no exista ya
        if (gestorUsuarios.verificarExistenciaCorreo(correoIngresado)) {
            mostrarMensajeError("Ya existe una cuenta con este correo electrónico.");
            return;
        }
        
        // Intentar registrar el usuario
        if (gestorUsuarios.registrarNuevoUsuario(correoIngresado, contrasenaIngresada)) {
            mostrarMensajeExito("¡Usuario registrado exitosamente!");
            limpiarCampos();
        } else {
            mostrarMensajeError("Error al registrar el usuario. Inténtelo nuevamente.");
        }
    }
    
    /**
     * Valida que ambos campos estén completos
     * @param correo Email ingresado
     * @param contrasena Contraseña ingresada
     * @return true si ambos campos tienen contenido, false si alguno está vacío
     */
    private boolean validarCamposCompletos(String correo, String contrasena) {
        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarMensajeError("Por favor, complete todos los campos.");
            return false;
        }
        return true;
    }
    
    /**
     * Valida el formato básico del correo electrónico
     * Verifica que contenga @ y al menos un punto
     * @param correo Email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean validarFormatoCorreo(String correo) {
        return correo.contains("@") && correo.contains(".");
    }
    
    /**
     * Valida que la contraseña tenga la longitud mínima requerida
     * @param contrasena Contraseña a validar
     * @return true si tiene al menos 6 caracteres, false en caso contrario
     */
    private boolean validarLongitudContrasena(String contrasena) {
        return contrasena.length() >= 6;
    }
    
    /**
     * Muestra un mensaje de error al usuario
     * @param mensaje Texto del mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Muestra un mensaje de éxito al usuario
     * @param mensaje Texto del mensaje de éxito
     */
    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Limpia todos los campos de entrada del formulario
     * Útil después de un registro exitoso
     */
    private void limpiarCampos() {
        campoCorreoElectronico.setText("");
        campoContrasena.setText("");
    }
    
    /**
     * Abre la ventana del menú principal y cierra la actual
     * Se ejecuta después de un login exitoso
     */
    private void abrirMenuPrincipal() {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Ejecuta la apertura del menú principal en el hilo de eventos
             */
            @Override
            public void run() {
                new VentanaMenuPrincipal().setVisible(true);
            }
        });
    }
}