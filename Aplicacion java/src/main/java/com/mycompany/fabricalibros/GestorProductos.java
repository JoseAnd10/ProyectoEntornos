/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor para manejar operaciones CRUD de productos en la base de datos
 * Clase responsable de toda la lógica de acceso a datos para productos

 */
public class GestorProductos {
    
    /**
     * Obtiene todos los productos de la base de datos ordenados por nombre
     * @return Lista de productos encontrados, lista vacía si no hay productos
     */
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, cantidad, fecha_modificacion FROM productos ORDER BY nombre";
        
        try (Connection conn = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }
    
    /**
     * Crea un nuevo producto en la base de datos
     * @param producto Objeto producto con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean crear(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getCantidad());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualiza un producto existente en la base de datos
     * @param producto Objeto producto con los datos actualizados (debe incluir ID)
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ? WHERE id = ?";
        
        try (Connection conn = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getCantidad());
            stmt.setInt(4, producto.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina un producto de la base de datos
     * @param id ID del producto a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Busca productos por nombre, precio o cantidad (método adicional útil)
     * @param termino Término de búsqueda
     * @return Lista de productos que coinciden con el término
     */
    public List<Producto> buscarPorTermino(String termino) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, cantidad, fecha_modificacion FROM productos " +
                    "WHERE nombre LIKE ? OR precio LIKE ? ORDER BY nombre";
        
        try (Connection conn = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + termino + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }
    
    /**
     * Obtiene un producto específico por su ID
     * @param id ID del producto a buscar
     * @return Producto encontrado o null si no existe
     */
    public Producto obtenerPorId(int id) {
        String sql = "SELECT id, nombre, precio, cantidad, fecha_modificacion FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionBaseDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}