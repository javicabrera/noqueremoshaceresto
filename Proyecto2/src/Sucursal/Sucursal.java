package Sucursal;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sucursal extends Thread {
    private Socket socketCentral;
    private Socket socketSurtidor;

    public Sucursal(Socket socketCentral, Socket socketSurtidor){
        this.socketCentral = socketCentral;
        this.socketSurtidor = socketSurtidor;
    }

    @Override
    public void run() {
        try {
            DataInputStream inSurtidor = new DataInputStream(socketSurtidor.getInputStream());
            DataOutputStream outCentral = new DataOutputStream(socketCentral.getOutputStream());
            String message;
            while((!socketSurtidor.isClosed()) && (!socketCentral.isClosed())){
                message = inSurtidor.readUTF();
                //TODO: actualizar la base de datos aquí
                outCentral.writeUTF(message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }
}
