/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import ventana.VentanaSurtidor;

/**
 *
 * @author 56992
 */
public class Surtidor {
    private int id;
    private double gasolina93;
    private double gasolina95;
    private double gasolina97;
    private double diesel;
    private double kerosene;
    private VentanaSurtidor vista;
    private static Surtidor s;

    public Surtidor(int id, double gasolina93, double gasolina95, double gasolina97, double diesel, double kerosene) {
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
        vista= new VentanaSurtidor();
        vista.setVisible(true);
    }

    public double getGasolina93() {
        return gasolina93;
    }

    public void setGasolina93(double gasolina93) {
        this.gasolina93 = gasolina93;
    }

    public double getGasolina95() {
        return gasolina95;
    }

    public void setGasolina95(double gasolina95) {
        this.gasolina95 = gasolina95;
    }

    public double getGasolina97() {
        return gasolina97;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGasolina97(double gasolina97) {
        this.gasolina97 = gasolina97;
    }

    public double getDiesel() {
        return diesel;
    }

    public void setDiesel(double diesel) {
        this.diesel = diesel;
    }

    public double getKerosene() {
        return kerosene;
    }

    public void setKerosene(double kerosene) {
        this.kerosene = kerosene;
    }

    @Override
    public String toString(){
        return "Precios this surtidor:\n93: " + this.gasolina93 + "\n95: " + this.gasolina95 +
                "\n97: " + this.gasolina97 + "\ndiesel: " + this.diesel + "\nkerosene: " + this.kerosene;
    }


    public static void main(String[] args) {
        s= new Surtidor();
        final String HOST = "127.0.0.1";
//        final String HOST = "35.247.228.145"; //virtual machine
        final int PORT = 4200;
        DataInputStream in;
        DataOutputStream out;
        Surtidor surtidor1 = new Surtidor(1,100,100,100,100,100);
        // el surtidor debería inicializarse con la info que le llega como respuesa desde la central con los precios de los combustibles
        try {
            Socket sc = new Socket(HOST, PORT);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            while(true){
                String message = in.readUTF();
                System.out.println("-> recibiendo desde el surtidor: " + message);

                if(message.contains("act")){
                    String [] splitted  = message.split("-");
                    String tipoCompbustible = splitted[1];
                    double nuevoPrecio = Double.valueOf(splitted[2]);
                    actualizarCombustible(tipoCompbustible, nuevoPrecio, surtidor1);
                    System.out.println("nuevos precios: " + surtidor1);
                    out.writeUTF("ok");
                }

                if(message.equals("end")) break;
            }
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private static void actualizarCombustible(String tipoCompbustible, double nuevoPrecio, Surtidor surtidor) {
        switch (tipoCompbustible){
            case "93":  surtidor.setGasolina93(nuevoPrecio);
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



