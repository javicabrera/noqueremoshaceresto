package Central;

import sun.nio.cs.ext.DoubleByte;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;


// Esta clase escucha por actualizaciones proveninentes de la base de datos


public class SucursalListener extends Thread {
    private Socket sucursalSocket;
    private BDcentral db;

    public SucursalListener(Socket sucursalSocket){
        this.sucursalSocket = sucursalSocket;
        this.db = new BDcentral();
    }

    @Override
    public void run() {
        String message;
        try {
            DataInputStream in = new DataInputStream(this.sucursalSocket.getInputStream());

            while(!this.sucursalSocket.isClosed()){
                message = in.readUTF();
                System.out.println("-->insertando en base de datos: " + message);
                guardarVenta(message);
                // aquí va la inserción a la base de datos
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void guardarVenta(String message){
        String splitted[] = message.split("-");
        if(!splitted[0].equals("vnt")) return;
        int litros = Integer.parseInt(splitted[2]);
        String tipo = splitted[1];
        this.db.instertarVenta(this.db.conexion, litros, 69, 69, 69, tipo, true);

        System.out.println("CENTRAL: venta " + message + " guardada.");
        }

}
