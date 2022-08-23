/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Clases.Producto;
import Model.Persistencia.CRUD;
import Model.Persistencia.DbConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JUAN MARIÑO
 */
public abstract class Controller {

    public static boolean registrarProducto(String nombre, int cantidad, String categoria, double precio) throws SQLException {
        Producto c1 = new Producto(0, nombre, cantidad, categoria,precio);
        CRUD.setConnection(DbConnection.ConexionBD());
        String sentencia = "INSERT INTO productos(nombre,cantidad,categoria,precio) "
                + " VALUES ('" + c1.getNombre() + "','" + c1.getCantidad()+ "','" + c1.getCategoria()+"','"+c1.getPrecio()+ "');";
        if (CRUD.setAutoCommitBD(false)) {
            if (CRUD.insertarBD(sentencia)) {
                CRUD.commitBD();
                CRUD.cerrarConexion();
                return true;
            } else {
                CRUD.rollbackBD();
                CRUD.cerrarConexion();
                return false;
            }
        } else {
            CRUD.cerrarConexion();
            return false;
        }
    }

    public static boolean actualizarProducto(int id,String nombre, int cantidad, String categoria, double precio) {
        Producto c1 = new Producto(id, nombre, cantidad, categoria,precio);
        CRUD.setConnection(DbConnection.ConexionBD());
        String Sentencia = "UPDATE `productos` SET nombre='" + c1.getNombre() + "',cantidad='" + c1.getCantidad() + "',categoria='" + c1.getCategoria()
                + "',precio='" + c1.getPrecio()+ "' WHERE id=" + c1.getId()+ ";";
        if (CRUD.setAutoCommitBD(false)) {
            if (CRUD.actualizarBD(Sentencia)) {
                CRUD.commitBD();
                CRUD.cerrarConexion();
                return true;
            } else {
                CRUD.rollbackBD();
                CRUD.cerrarConexion();
                return false;
            }
        } else {
            CRUD.cerrarConexion();
            return false;
        }
    }

    public static boolean borrarProducto(int id) {
        CRUD.setConnection(DbConnection.ConexionBD());
        String Sentencia = "DELETE FROM `productos` WHERE `id`='" + id + "';";
        if (CRUD.setAutoCommitBD(false)) {
            if (CRUD.borrarBD(Sentencia)) {
                CRUD.commitBD();
                CRUD.cerrarConexion();
                return true;
            } else {
                CRUD.rollbackBD();
                CRUD.cerrarConexion();
                return false;
            }
        } else {
            CRUD.cerrarConexion();
            return false;
        }
    }

    public static Producto obtenerProducto(int id) {
        CRUD.setConnection(DbConnection.ConexionBD());
        String sql = "select * from productos where id=" + id + ";";
        ResultSet rs = CRUD.consultarBD(sql);
        Producto p1 = new Producto();
        try {
            if (rs.next()) {
                p1.setId(rs.getInt("id"));
                p1.setNombre(rs.getString("nombre"));
                p1.setCantidad(rs.getInt("cantidad"));
                p1.setCategoria(rs.getString("categoria"));
                p1.setPrecio(rs.getDouble("precio"));

                CRUD.cerrarConexion();
            } else {
                CRUD.cerrarConexion();
                return null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return p1;
    }

    public static List<Producto> listarProductos() {
        CRUD.setConnection(DbConnection.ConexionBD());
        List<Producto> listaProductos = new ArrayList<>();
        try {
            String sql = "select * from productos";

            ResultSet rs = CRUD.consultarBD(sql);
            
            while (rs.next()) {
                Producto p1 = new Producto();
                p1.setId(rs.getInt("ID"));
                p1.setNombre(rs.getString("nombre"));
                p1.setCantidad(rs.getInt("cantidad"));
                p1.setCategoria(rs.getString("categoria"));
                p1.setPrecio(rs.getDouble("precio"));
                listaProductos.add(p1);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            CRUD.cerrarConexion();
        }
        return listaProductos;
    }

}
