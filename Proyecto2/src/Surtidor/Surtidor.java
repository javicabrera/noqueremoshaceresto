/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Surtidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Clase que representa el socket de un surtidor
 *
 * @author Matias Escobar, Javiera Cabrera, Yarixa Galvez
 */
public class Surtidor {
    private int id;
    private int gasolina93;
    private int gasolina95;
    private int gasolina97;
    private int diesel;
    private int kerosene;

    public Surtidor(int id, int gasolina93, int gasolina95, int gasolina97, int diesel, int kerosene) {
        this.id = id;
        this.gasolina93 = gasolina93;
        this.gasolina95 = gasolina95;
        this.gasolina97 = gasolina97;
        this.diesel = diesel;
        this.kerosene = kerosene;
    }

    public Surtidor(){
        this.id = 0;
        this.gasolina93 = 100;
        this.gasolina95 = 100;
        this.gasolina97 = 100;
        this.diesel = 100;
        this.kerosene = 100;
    }

    public int getGasolina93() {
        return gasolina93;
    }

    public void setGasolina93(int gasolina93) {
        this.gasolina93 = gasolina93;
    }

    public int getGasolina95() {
        return gasolina95;
    }

    public void setGasolina95(int gasolina95) {
        this.gasolina95 = gasolina95;
    }

    public int getGasolina97() {
        return gasolina97;
    }

    public void setGasolina97(int gasolina97) {
        this.gasolina97 = gasolina97;
    }

    public int getDiesel() {
        return diesel;
    }

    public void setDiesel(int diesel) {
        this.diesel = diesel;
    }

    public void setKerosene(int kerosene){
        this.kerosene=kerosene;
    }

    @Override
    public String toString(){
        return "Precios este surtidor:\n93: " + this.gasolina93 + "\n95: " + this.gasolina95 +
                "\n97: " + this.gasolina97 + "\ndiesel: " + this.diesel + "\nkerosene: " + this.kerosene;
    }


    public static void main(String[] args) throws SQLException {
        final String HOST = "127.0.0.1"; // ip del equipo que contiene la sucursal;
        final int PORT = 4200;
        DataInputStream in;
        DataOutputStream out;
        SurtidorGui gui = null;
        BDsurtidor bd=gui.getBD();
        Surtidor surtidor1 = new Surtidor(1,100,100,100,100,100);
        // el surtidor debería inicializarse con la info que le llega como respuesa desde la central con los precios de los combustibles
        while(true){
            try {
                Socket sc = new Socket(HOST, PORT);
                if(gui!=null) {
                    gui.setSucursalSocket(sc);
                }else{
                    gui = new SurtidorGui(sc);
                    gui.start();
                }

                in = new DataInputStream(sc.getInputStream());
                //            out = new DataOutputStream(sc.getOutputStream());

                while(!sc.isClosed()){
                    String message = in.readUTF();
                    System.out.println("-> recibiendo desde el sucursal: " + message);

                    if(message.contains("act")){
                        String [] splitted  = message.split("-");
                        String tipoCompbustible = splitted[1];
                        int nuevoPrecio = Integer.valueOf(splitted[2]);
                        actualizarCombustible(bd,tipoCompbustible, nuevoPrecio, surtidor1);
                        System.out.println("nuevos precios: " + surtidor1);
                    }
                    if(message.equals("end")) break;
                }
            } catch (IllegalMonitorStateException e){
                System.out.println("ERORR");
                //                e.printStackTrace();
                freeze(3);
            }catch (IOException e){
                // entonces, cada vez que el servidor se desconecte, se hará
                // una pausa de cinco segundos y volverá a intentar la conexión con el servidor
                if(gui == null){
                    gui = new SurtidorGui();
                    gui.start();
                }
                freeze(3);
            }
        }
    }

    private static void freeze(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            System.out.println("-> interruption error");
            ex.printStackTrace();
        }
    }

    /**
     * Actualiza el precio de un tipo de combustible .
     * @param tipoCompbustible
     * @param nuevoPrecio
     * @param surtidor
     */
    private static void actualizarCombustible(BDsurtidor bd,String tipoCompbustible, int nuevoPrecio,Surtidor surtidor) throws SQLException {
        bd.modificarPrecio(bd.conexion, surtidor.id, 1, tipoCompbustible, nuevoPrecio);
        switch (tipoCompbustible){
            case "93":  
                surtidor.setGasolina93(nuevoPrecio);                 
                break;
            case "95": surtidor.setGasolina95(nuevoPrecio);
                break;
            case "97": surtidor.setGasolina97(nuevoPrecio);
                break;
            case "diesel": surtidor.setDiesel(nuevoPrecio);
                break;
            case "kerosene": surtidor.setKerosene(nuevoPrecio);
                break;
            default: break;
        }
    }
}



