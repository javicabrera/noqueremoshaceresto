package Central;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


// Esta clase escucha por actualizaciones proveninentes de la base de datos


public class SucursalListener extends Thread {
    private Socket sucursalSocket;

    public SucursalListener(Socket sucursalSocket){
        this.sucursalSocket = sucursalSocket;
    }

    @Override
    public void run() {
        String message;
        try {
            DataInputStream in = new DataInputStream(this.sucursalSocket.getInputStream());

            while(!this.sucursalSocket.isClosed()){
                message = in.readUTF();
                System.out.println("-->insertando en base de datos: " + message);

                // aquí va la inserción a la base de datos
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
