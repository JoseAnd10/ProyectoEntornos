/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fabricalibros;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Clase modelo que representa un producto en el sistema de stock
 * Contiene toda la información básica de un producto y métodos de utilidad

 */
public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private Timestamp fechaModificacion;
    
    /**
     * Constructor vacío por defecto
     */
    public Producto() {}
    
    /**
     * Constructor con parámetros principales para crear un producto
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param cantidad Cantidad en stock
     */
    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    
    /**
     * Obtiene el ID único del producto
     * @return ID del producto
     */
    public int getId() { return id; }
    
    /**
     * Establece el ID del producto
     * @param id ID único del producto
     */
    public void setId(int id) { this.id = id; }
    
    /**
     * Obtiene el nombre del producto
     * @return Nombre del producto
     */
    public String getNombre() { return nombre; }
    
    /**
     * Establece el nombre del producto
     * @param nombre Nombre del producto
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    /**
     * Obtiene el precio del producto
     * @return Precio del producto
     */
    public double getPrecio() { return precio; }
    
    /**
     * Establece el precio del producto
     * @param precio Precio del producto (debe ser mayor o igual a 0)
     */
    public void setPrecio(double precio) { this.precio = precio; }
    
    /**
     * Obtiene la cantidad en stock del producto
     * @return Cantidad disponible en stock
     */
    public int getCantidad() { return cantidad; }
    
    /**
     * Establece la cantidad en stock del producto
     * @param cantidad Cantidad en stock (debe ser mayor o igual a 0)
     */
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    /**
     * Obtiene la fecha de última modificación del producto
     * @return Timestamp de la última modificación
     */
    public Timestamp getFechaModificacion() { return fechaModificacion; }
    
    /**
     * Establece la fecha de última modificación del producto
     * @param fechaModificacion Timestamp de la modificación
     */
    public void setFechaModificacion(Timestamp fechaModificacion) { 
        this.fechaModificacion = fechaModificacion; 
    }
    
    /**
     * Formatea la fecha de modificación para mostrar en la interfaz
     * @return Fecha formateada como "dd/MM/yyyy HH:mm" o cadena vacía si no hay fecha
     */
    public String getFechaFormateada() {
        if (fechaModificacion != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formato.format(fechaModificacion);
        }
        return "";
    }
    
    /**
     * Calcula el valor total del producto (precio × cantidad)
     * @return Valor total del stock de este producto
     */
    public double getValorTotalStock() {
        return precio * cantidad;
    }
    
    /**
     * Verifica si el producto está disponible (cantidad mayor a 0)
     * @return true si hay stock disponible, false si está agotado
     */
    public boolean estaDisponible() {
        return cantidad > 0;
    }
    
    /**
     * Verifica si los datos del producto son válidos para guardar
     * @return true si el producto tiene datos válidos, false en caso contrario
     */
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
               precio >= 0 &&
               cantidad >= 0;
    }
    
    /**
     * Representación en cadena del producto para debugging
     * @return Cadena con información básica del producto
     */
    @Override
    public String toString() {
        return String.format("Producto{id=%d, nombre='%s', precio=%.2f, cantidad=%d}", 
            id, nombre, precio, cantidad);
    }
    
    /**
     * Compara dos productos por igualdad basándose en el ID
     * @param obj Objeto a comparar
     * @return true si son el mismo producto (mismo ID)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto = (Producto) obj;
        return id == producto.id;
    }
    
    /**
     * Genera código hash basado en el ID del producto
     * @return Código hash del producto
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}