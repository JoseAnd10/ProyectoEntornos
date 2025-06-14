/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.fabricalibros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión con la base de datos MySQL
 * Usando el mismo patrón de conexión que el código de referencia

 */
public class ConexionBaseDatos {
    // Datos de conexión a la base de datos
    private static final String URL_BASE_DATOS = "jdbc:mysql://localhost:3306/fabricalibro";
    private static final String USUARIO_BD = "root";
    private static final String CONTRASENA_BD = "";
    
    /**
     * Obtiene una conexión directa a la base de datos MySQL
     * Siguiendo el mismo patrón del código de referencia
     * @return Connection objeto de conexión a la base de datos
     * @throws SQLException si hay error en la conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL_BASE_DATOS, USUARIO_BD, CONTRASENA_BD);
    }
    
    /**
     * Cierra la conexión a la base de datos de forma segura
     * Verifica que la conexión no sea nula antes de cerrarla
     * @param conexion la conexión a cerrar
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException excepcion) {
                System.err.println("Error al cerrar conexión: " + excepcion.getMessage());
                excepcion.printStackTrace();
            }
        }
    }
}