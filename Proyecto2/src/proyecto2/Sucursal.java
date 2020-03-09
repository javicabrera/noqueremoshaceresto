package proyecto2;

import threads.AdminSurtidores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import ventana.VentanaSucursal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Javiera Cabrera
 */
public class Sucursal {
    private VentanaSucursal vista;
    private static Sucursal s;
    public Sucursal(){
        vista= new VentanaSucursal();
        vista.setVisible(true);
    } 
    public static void main(String[] args) throws IOException {
        s= new Sucursal();
        final int PORT = 4200;
        ArrayList<Socket> surtidores = new ArrayList<Socket>();
        ServerSocket server = null;
        Socket sc = null;
        AdminSurtidores admin;

        try{
            server = new ServerSocket(PORT);
            System.out.println("servidor sucursal iniciado. Esperando por surtidores...");
            admin = new AdminSurtidores();
            admin.start();

            while(!server.isClosed()){
                if((sc = server.accept()) == null) break;  // esta sentencia hace que el programa espere por un nuevo cliente (surtidor)
                System.out.println("surtidor conectado");
                admin.addSurtidor(sc);
                surtidores.add(sc);
            }
            sc.close();
            System.out.println("...saliendo !");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
