/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Sucursal;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Javiera
 */
public class BDsucursal {
    Connection conexion= null;
    String pass= "199230662";
    String user= "postgres";
    String baseDatos="jdbc:postgresql://localhost:5432/BDSucursal";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BDsucursal bd= new BDsucursal();
        bd.conectar();
    }
    
    public void conectar(){
        try{
        conexion= DriverManager.getConnection(baseDatos, user, pass);
        System.out.println("CONECTADO");
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("No se conectó");
        }
    }
    public void instertarVenta(Connection c, int litros, int id, int idSurtidor,  String tipo){
        
        try {
            Statement s= c.createStatement();
            s.executeUpdate("INSERT INTO transcaccion VALUES ("+ litros + "," + id + ","
                    + idSurtidor+",'"+ tipo+"');");
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modificarPrecio(Connection c, int id, int idSucursal,int p93, int p95, int p97, int pDiesel, int pKer){
        try {
            Statement s= c.createStatement();
            s.executeUpdate("UPDATE surtidor SET precio93="+p93+ ","
                    + "preci95="+ p95 + ", precio97="+ p97+","
                    + "precioKerosene="+ pKer+", precioDiesel="+pDiesel+""
                    + " WHERE id="+ id+" AND id_sucursal="+idSucursal);
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void getCargas(Connection c){
        int cargas=0;
        String consulta= "SELECT count(*) FROM ventas";
        try {
            Statement s=c.createStatement();
            PreparedStatement ps=c.prepareStatement(consulta);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                cargas=rs.getInt("count");
            }
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(BDsucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}