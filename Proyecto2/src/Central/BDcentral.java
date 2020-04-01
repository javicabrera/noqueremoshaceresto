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
    String pass= "199230662";
    String user= "postgres";
    String baseDatos="jdbc:postgresql://localhost:5432/BDCentral";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BDcentral bd= new BDcentral();
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
    public void instertarVenta(Connection c, int litros, int id,int idsucursal, int idsurtidor,int idSurtidor,  String tipo, boolean enviado){
        
        try {
            Statement s= c.createStatement();
            s.executeUpdate("INSERT INTO reporte (idventa,idsucursal,idsurtidor,litros,tipo,enviado)"
                    + " VALUES ("+ id + "," + idsucursal + ","+idsurtidor +"," + litros + ","
                    + tipo+",'"+ enviado+"');");
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(BDcentral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCargas(Connection c){
        int cargas=0;
        String consulta= "SELECT count(*) FROM reporte";
        try {
            Statement s=c.createStatement();
            PreparedStatement ps=c.prepareStatement(consulta);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                cargas=rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDcentral.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}