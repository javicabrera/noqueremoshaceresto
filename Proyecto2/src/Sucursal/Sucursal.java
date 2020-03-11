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
    private SingletonBD db;

    public Sucursal(Socket socketCentral, Socket socketSurtidor) throws IOException {
        this.socketCentral = socketCentral;
        this.socketSurtidor = socketSurtidor;
        db = SingletonBD.getInstance();
    }

    @Override
    public void run() {
        try {
            DataInputStream inSurtidor = new DataInputStream(socketSurtidor.getInputStream());
            String message;
            while((!socketSurtidor.isClosed()) && (!socketCentral.isClosed())){
                message = inSurtidor.readUTF();
                System.out.println("Recibiendo en sucursal: " + message);
                //TODO: actualizar la base de datos aquí
                String [] splitted = message.split("-");
                if(splitted[0].equals("vnt"))
                    guardarVenta(splitted);
            }
            this.db.escribirBD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarVenta(String[] splitted) {
        int id = socketSurtidor.getPort();
        int tipo;
        switch (splitted[1]){
            case "93": tipo = 1;
            break;
            case "95": tipo = 2;
            break;
            case "97": tipo = 3;
            break;
            case "diesel": tipo = 4;
            break;
            case "kerosene": tipo = 5;
            break;
            default: tipo = 5;
            break;
        }
        int litros = Integer.valueOf(splitted[2]);
        System.out.println("Venta "+tipo+"  "+litros);
        this.db.añadirDato(id, tipo, litros);
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }
}
