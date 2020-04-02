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
    String pass= "password";
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
            System.out.println("BASE DE DATOS CONECTADA");
        }catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("ERROR: no se pudo conectar a la base de datos");
        }
    }

//    public void insertarSurtidor(Connection c, int idSucursal,int p93, int p95, int p97, int pDiesel, int pKer){
//        try {
//            Statement s= c.createStatement();
//            s.executeUpdate("INSERT INTO surtidor (precio93 , precio95, precio97, preciok, preciodiesel, id_sucursal) VALUES"
//                    + "( "+p93+","+p95+","+p97+","+pDiesel+","+pKer+","+idSucursal+");");
//        } catch (SQLException ex) {
//            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public void instertarVenta(int litros, int idSurtidor,  String tipo, Boolean enviado){
        String env = enviado?"t":"f";
        try {
            Statement s= this.conexion.createStatement();
            s.executeUpdate("INSERT INTO ventas (litros, idsurtidor, tipo, enviado) VALUES ("+ litros + "," + idSurtidor
                    + ",'" + tipo + "','" + env + "');");
        } catch (SQLException ex) {
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarPrecio(int idSurtidor, String tipo, int precio) throws SQLException{
        try {
            Statement s= this.conexion.createStatement();
            if (tipo.equals("93")){
                s.executeUpdate("UPDATE parametros SET precio93=" + precio + " WHERE idsurtidor="+ idSurtidor+";");
            }
            else if (tipo.equals("95")){
                s.executeUpdate("UPDATE parametros SET precio95=" + precio + " WHERE idsurtidor="+ idSurtidor+";");
            }
            else if (tipo.equals("97")){
                s.executeUpdate("UPDATE parametros SET precio97=" + precio + " WHERE idsurtidor="+ idSurtidor+";");
            }
            else if (tipo.equals("diesel")){
                s.executeUpdate("UPDATE parametros SET precioDiesel=" + precio + " WHERE idsurtidor="+ idSurtidor+";");
            }
            else if (tipo.equals("kerosene")){
                s.executeUpdate("UPDATE parametros SET precioKerosene=" + precio + " WHERE idsurtidor=" + idSurtidor+";");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BDsurtidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}