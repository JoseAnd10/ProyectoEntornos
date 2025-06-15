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
        iniciarVentanaPrincipal(); // Solo esto, usa el Look and Feel predeterminado
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