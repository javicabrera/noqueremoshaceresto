/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bd;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Javiera
 */
public class BD {
    Connection conexion= null;
    String pass= "199230662";
    String user= "postgres";
    String central="jdbc:postgresql://localhost:5432/BDCentral";
    String sucursal="jdbc:postgresql://localhost:5432/BDSucursal";
    String surtidor="jdbc:postgresql://localhost:5432/BDSurtidor";
    String baseDatos=surtidor;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BD bd= new BD();
        bd.conectar();
        
    }
    
    public void conectar(){
        try{
        conexion= DriverManager.getConnection(baseDatos, user, pass);
        System.out.println("CONECTADO");
        insertarSurtidor(conexion,1,2,3,4,5,2);
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("No se conectó");
        }
    }
    public void insertarSurtidor(Connection c, int idSucursal,int p93, int p95, int p97, int pDiesel, int pKer){
        try {
            Statement s= c.createStatement();
            s.executeUpdate("INSERT INTO surtidor (precio93 , precio95, precio97, preciok, preciodiesel, id_sucursal) VALUES"
                    + "( "+p93+","+p95+","+p97+","+pDiesel+","+pKer+","+idSucursal+");");
        } catch (SQLException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
    public void instertarVenta(Connection c, int litros, int id, int idSurtidor,  String tipo){
        
        try {
            Statement s= c.createStatement();
            s.executeUpdate("INSERT INTO ventaa VALUES ("+ litros + "," + id + ","
                    + idSurtidor+",'"+ tipo+"');");
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modificarPrecio(Connection c, int id, int idSucursal,int p93, int p95, int p97, int pDiesel, int pKer){
        try {
            Statement s= c.createStatement();
            s.executeUpdate("UPDATE surtidor SET precio93="+p93+ ","
                    + "preci95="+ p95 + ", precio97="+ p97+","
                    + "preciok="+ pKer+", preciodiesel="+pDiesel+""
                    + " WHERE id="+ id+" AND id_sucursal="+idSucursal);
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}