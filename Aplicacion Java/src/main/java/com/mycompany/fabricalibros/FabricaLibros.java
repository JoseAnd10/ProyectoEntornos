/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.fabricalibros;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal de la aplicación Fábrica de Libros
 * Punto de entrada del sistema de autenticación
 
 */
public class FabricaLibros {
    
    /**
     * Método principal de la aplicación - punto de entrada del programa
     * Configura el look and feel y lanza la ventana de inicio de sesión
     * @param argumentos argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] argumentos) {
        // Configurar el look and feel del sistema para mejor apariencia
        configurarAparienciaVisual();
        
        // Crear y mostrar la ventana de inicio de sesión en el hilo de eventos de Swing
        iniciarVentanaPrincipal();
    }
    
    /**
     * Configura el aspecto visual de la aplicación usando Nimbus
     * Si Nimbus no está disponible, usa el look and feel predeterminado
     */
    private static void configurarAparienciaVisual() {
        try {
            // Buscar el look and feel Nimbus entre los disponibles
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception excepcion) {
            // Si no se puede establecer Nimbus, usar el predeterminado
            System.err.println("No se pudo establecer el aspecto Nimbus: " + excepcion.getMessage());
        }
    }
    
    /**
     * Inicia la ventana principal de inicio de sesión
     * Utiliza SwingUtilities.invokeLater para asegurar la ejecución en el hilo de eventos
     */
    private static void iniciarVentanaPrincipal() {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Ejecuta la creación de la ventana de inicio de sesión
             * Se ejecuta en el hilo de eventos de Swing para thread safety
             */
            @Override
            public void run() {
                new VentanaInicioSesion().setVisible(true);
            }
        });
    }
}