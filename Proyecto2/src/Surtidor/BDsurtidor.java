/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Surtidor;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Javiera
 */
public class BDsurtidor {
    Connection conexion= null;
    String pass= "199230662";
    String user= "postgres";
    String baseDatos="jdbc:postgresql://localhost:5432/BDSurtidor";
    /**
     * @param args the command line arguments
     */
    public BDsurtidor(){
        conectar();
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
    public void insertarSurtidor(Connection c, int idSucursal,int p93, int p95, int p97, int pDiesel, int pKer){
        try {
            Statement s= c.createStatement();
            s.executeUpdate("INSERT INTO surtidor (precio93 , precio95, precio97, preciok, preciodiesel, id_sucursal) VALUES"
                    + "( "+p93+","+p95+","+p97+","+pDiesel+","+pKer+","+idSucursal+");");
        } catch (SQLException ex) {
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    public void instertarVenta(Connection c, int litros, int id, int idSurtidor,  String tipo, boolean x){
        
        try {
            Statement s= c.createStatement();
            s.executeUpdate("INSERT INTO Venta (litros,id_surtidor,id,tipo,enviado) VALUES ("+ litros + "," + id + ","
                    + idSurtidor+","+tipo+","+x+");");
        } catch (SQLException ex) {
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modificarPrecio(Connection c, int id, int idSucursal, String tipo, int precio) throws SQLException{
        try {
            Statement s= c.createStatement();                
            if (tipo.equals("93")){
                s.executeUpdate("UPDATE surtidor SET precio93="+precio+ " WHERE id="+ id+" AND id_sucursal="+idSucursal+");");
            }
            else if (tipo.equals("95")){
                s.executeUpdate("UPDATE surtidor SET precio95="+precio+ " WHERE id="+ id+" AND id_sucursal="+idSucursal+");");
            }
            else if (tipo.equals("97")){
                s.executeUpdate("UPDATE surtidor SET precio97="+precio+ " WHERE id="+ id+" AND id_sucursal="+idSucursal+");");
            }
            else if (tipo.equals("diesel")){
                s.executeUpdate("UPDATE surtidor SET preciodiesel="+precio+ " WHERE id="+ id+" AND id_sucursal="+idSucursal+");");
            }
            else if (tipo.equals("kerosene")){
                s.executeUpdate("UPDATE surtidor SET preciok="+precio+ " WHERE id="+ id+" AND id_sucursal="+idSucursal+");");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}