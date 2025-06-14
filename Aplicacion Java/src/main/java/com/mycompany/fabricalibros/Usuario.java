/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gestor para manejar operaciones de usuarios en la base de datos
 * Clase responsable de autenticación, registro y gestión de usuarios

 */
public class Usuario {
    
    /**
     * Registra un nuevo usuario en el sistema
     * @param correoElectronico Email del usuario (debe ser único)
     * @param contrasenaUsuario Contraseña del usuario
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarNuevoUsuario(String correoElectronico, String contrasenaUsuario) {
        Connection conexion = null;
        PreparedStatement declaracionPreparada = null;
        
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            
            String consultaSQL = "INSERT INTO usuarios (correo, contrasena) VALUES (?, ?)";
            declaracionPreparada = conexion.prepareStatement(consultaSQL);
            declaracionPreparada.setString(1, correoElectronico);
            declaracionPreparada.setString(2, contrasenaUsuario);
            
            int filasAfectadas = declaracionPreparada.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException excepcion) {
            System.err.println("Error al registrar usuario: " + excepcion.getMessage());
            excepcion.printStackTrace();
            return false;
        } finally {
            // Cerrar recursos de forma segura
            cerrarRecursos(conexion, declaracionPreparada, null);
        }
    }
    
    /**
     * Verifica las credenciales de inicio de sesión de un usuario
     * @param correoElectronico Email del usuario
     * @param contrasenaUsuario Contraseña del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean verificarInicioSesion(String correoElectronico, String contrasenaUsuario) {
        Connection conexion = null;
        PreparedStatement declaracionPreparada = null;
        ResultSet resultadoConsulta = null;
        
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            
            String consultaSQL = "SELECT id, correo FROM usuarios WHERE correo = ? AND contrasena = ?";
            declaracionPreparada = conexion.prepareStatement(consultaSQL);
            declaracionPreparada.setString(1, correoElectronico);
            declaracionPreparada.setString(2, contrasenaUsuario);
            
            resultadoConsulta = declaracionPreparada.executeQuery();
            return resultadoConsulta.next(); // true si encuentra un usuario con esas credenciales
            
        } catch (SQLException excepcion) {
            System.err.println("Error al verificar inicio de sesión: " + excepcion.getMessage());
            excepcion.printStackTrace();
            return false;
        } finally {
            // Cerrar recursos de forma segura
            cerrarRecursos(conexion, declaracionPreparada, resultadoConsulta);
        }
    }
    
    /**
     * Verifica si ya existe un usuario con el email proporcionado
     * @param correoElectronico Email a verificar
     * @return true si el email ya existe, false si está disponible
     */
    public boolean verificarExistenciaCorreo(String correoElectronico) {
        Connection conexion = null;
        PreparedStatement declaracionPreparada = null;
        ResultSet resultadoConsulta = null;
        
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            
            String consultaSQL = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
            declaracionPreparada = conexion.prepareStatement(consultaSQL);
            declaracionPreparada.setString(1, correoElectronico);
            
            resultadoConsulta = declaracionPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                return resultadoConsulta.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException excepcion) {
            System.err.println("Error al verificar existencia de correo: " + excepcion.getMessage());
            excepcion.printStackTrace();
            return false;
        } finally {
            // Cerrar recursos de forma segura
            cerrarRecursos(conexion, declaracionPreparada, resultadoConsulta);
        }
    }
    
    /**
     * Obtiene la información básica de un usuario por su email
     * @param correoElectronico Email del usuario a buscar
     * @return ID del usuario si existe, -1 si no se encuentra
     */
    public int obtenerIdUsuarioPorCorreo(String correoElectronico) {
        Connection conexion = null;
        PreparedStatement declaracionPreparada = null;
        ResultSet resultadoConsulta = null;
        
        try {
            conexion = ConexionBaseDatos.obtenerConexion();
            
            String consultaSQL = "SELECT id FROM usuarios WHERE correo = ?";
            declaracionPreparada = conexion.prepareStatement(consultaSQL);
            declaracionPreparada.setString(1, correoElectronico);
            
            resultadoConsulta = declaracionPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                return resultadoConsulta.getInt("id");
            }
            return -1; // Usuario no encontrado
            
        } catch (SQLException excepcion) {
            System.err.println("Error al obtener ID de usuario: " + excepcion.getMessage());
            excepcion.printStackTrace();
            return -1;
        } finally {
            // Cerrar recursos de forma segura
            cerrarRecursos(conexion, declaracionPreparada, resultadoConsulta);
        }
    }
    
    /**
     * Método utilitario para cerrar recursos de base de datos de forma segura
     * @param conexion Conexión a cerrar
     * @param statement Statement a cerrar
     * @param resultSet ResultSet a cerrar
     */
    private void cerrarRecursos(Connection conexion, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}