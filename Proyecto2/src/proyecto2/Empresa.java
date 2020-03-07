/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import threads.EmpresaGui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Javiera Cabrera
 */
public class Empresa {
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket sc = null;
        final int PORT = 4200;

        try{
            server = new ServerSocket(PORT);
            System.out.println("servidor iniciado. Esperano por clientes...");
            EmpresaGui gui = new EmpresaGui(server); // Este objeto extiende a Thread, por o tanto implementa el método run que se ejecuta en un hilo de ejecución aparte
            sc = server.accept();  // Se espera la conexión del primer surtidor
            System.out.println("cliente conectado...");
            gui.addSocket(sc);
            gui.run();  // Luego, se inicia la GUI

            while(!server.isClosed()){
                sc = server.accept();  // esta sentencia hace que el programa espere por un nuevo cliente (surtidor)
                System.out.println("client connected");
                gui.addSocket(sc);
            }

            sc.close();
            System.out.println("...saliendo !");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
