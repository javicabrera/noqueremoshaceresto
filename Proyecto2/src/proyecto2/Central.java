/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import threads.CentralGui;
import threads.SucursalListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Javiera Cabrera
 */
public class Central {
    private static Central c;
    private VentanaEmpresa vista;
    public Central(){
        vista=new VentanaEmpresa();
        vista.setVisible(true);
    }
    public static void main(String[] args) {
        c= new Central();
        ServerSocket server = null;
        Socket sc = null;
        final int PORT = 80; // En la práctica, este parámetro debería corresponder con el puerto que la maquina virtual tenga habilitado

        try{
            server = new ServerSocket(PORT);
            System.out.println("servidor iniciado. Esperano por clientes...");
            CentralGui gui = new CentralGui(server); // Este objeto extiende a Thread, por o tanto implementa el método run que se ejecuta en un hilo de ejecución aparte
            sc = server.accept();  // Se espera la conexión de la primera sucursal
            System.out.println("cliente conectado en puerto " + sc.getPort());
            // Se crea un nuevo hilo que espere por mensajes provenientes de las sucursales
            thread = new SucursalListener(sc);
            // Se inicia el hilo del listener
            thread.start();
            // Se agrega al listado
            listeners.add(thread);
            gui.addSocket(sc);
            gui.start();  // Luego, se inicia la GUI

            while(!server.isClosed()){
                sc = server.accept();  // esta sentencia hace que el programa espere por un nuevo cliente (surtidor)
                System.out.println("cliente conectado en puerto " + sc.getPort());
                // Se crea un nuevo hilo que espere por mensajes provenientes de las sucursales
                thread = new SucursalListener(sc);
                // Se inicia el hilo del listener
                thread.start();
                // S agrega el socket a la clase que gestiona las actualizaciones de los precios de las gasolinas
                gui.addSocket(sc);
            }

            sc.close();
            System.out.println("Cerrando servidor central...saliendo !");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
