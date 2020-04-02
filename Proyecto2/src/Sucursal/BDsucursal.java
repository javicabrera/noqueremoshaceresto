/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Sucursal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Javiera
 */
public class BDsucursal {
    Connection conexion= null;
    String pass= "password";
    String user= "postgres";
    String baseDatos="jdbc:postgresql://localhost:5432/BDSucursal";
    /**
     * @param args the command line arguments
     */
    BDsucursal() {
        conectar();
    }

    public void conectar(){
        try{
            conexion= DriverManager.getConnection(baseDatos, user, pass);
            System.out.println("BASE DE DATOS CONECTADA");
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("ERROR: no se pudo conectar a la base de datos");
        }
    }
    public void instertarVenta(int litros, int idVenta, int idSucursal, int idSurtidor,  String tipo, Boolean enviado){
        String env = enviado?"t":"f";
        try {
            Statement s= this.conexion.createStatement();
            s.executeUpdate("INSERT INTO ventas (idventa, idsucursal, litros, tipo, enviado)"
                    + " VALUES ("+ idVenta + "," + idSucursal + "," + litros + ",'"
                    + tipo +"','"+ enviado + "');");
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modificarPrecio(int idSucursal,int p93, int p95, int p97, int pDiesel, int pKer){
        try {
            Statement s= this.conexion.createStatement();
            s.executeUpdate("UPDATE parametros SET precio93="+p93+ ","
                    + "preci95="+ p95 + ", precio97="+ p97+","
                    + "precioKerosene="+ pKer+", precioDiesel="+pDiesel+""
                    + " WHERE id_sucursal=" + idSucursal);
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCargas(){
        int cargas=0;
        String consulta= "SELECT count(*) FROM ventas";
        try {
            PreparedStatement ps=this.conexion.prepareStatement(consulta);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                cargas=rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] getRezagados(){
        ArrayList<String> rezagados = new ArrayList<String>();

        int cargas=0;
        String consulta= "SELECT * FROM ventas WHERE enviado='f';";
        try {
            PreparedStatement ps = this.conexion.prepareStatement(consulta);
            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                String litros = rs.getString("litros");
                String tipo = rs.getString("tipo");
                String mensaje = "vnt-" + tipo+ "-" + litros;
                System.out.println("-->obteniendo el siguiente rezagado: " + mensaje);
                rezagados.add(mensaje);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }

        String arr[] = new String[rezagados.size()];

        for (int i=0; i<rezagados.size(); ++i) {
            arr[i] = rezagados.get(i);
        }

        return arr;
    }
}