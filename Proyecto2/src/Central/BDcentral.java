/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Central;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Javiera
 */

public class BDcentral {
    Connection conexion= null;
    String pass= "password";
    String user= "postgres";
    String baseDatos="jdbc:postgresql://localhost:5432/BDCentral";


    BDcentral() { conectar(); }

    public void conectar(){
        try{
            conexion= DriverManager.getConnection(baseDatos, user, pass);
            System.out.println("BASE DE DATOS CONECTADA");
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("ERROR: no se pudo conectar a la base de datos");
        }
    }
    public void instertarVenta(int litros, int id,int idsucursal, int idsurtidor,  String tipo, boolean enviado){

        try {
            Statement s= this.conexion.createStatement();
            s.executeUpdate("INSERT INTO ventas (idventa, idsucursal,litros, tipo, enviado)"
                    + " VALUES ("+ id + "," + idsucursal + "," + litros + ",'"
                    + tipo + "','" + enviado + "');");
            System.out.println("DATABASE: venta guardada de forma exitosa");
        } catch (SQLException ex) {
            Logger.getLogger(BDcentral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCargas(){
        int cargas=0;
        String consulta= "SELECT count(*) FROM ventas";
        try {
            PreparedStatement ps = this.conexion.prepareStatement(consulta);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                cargas=rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDcentral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCantidadDeCargas(String tipo){
        int resultado=-1;
        String query = "SELECT count(*) FROM ventas WHERE tipo='" + tipo +"';";
        try {
            PreparedStatement ps = this.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                resultado = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return resultado;
    }

    public float getLitrosConsumidos(String tipo){
        float resultado=-1;
        String query = "SELECT sum(litros) FROM ventas  WHERE tipo='" + tipo +"';";

        try {
            PreparedStatement ps = this.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                resultado = rs.getFloat("sum");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return resultado;
    }
}