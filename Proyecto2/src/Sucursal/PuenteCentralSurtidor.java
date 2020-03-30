package Sucursal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

// Ésta clase atiende los mensajes provenientes de los surtidores
// y los gestiona. Además crea un hilo que quda a la escucha de las
// ventas de los surtidores.

public class PuenteCentralSurtidor extends Thread {
    ArrayList<Socket> surtidores;
    ArrayList<Sucursal> listeners;
    SingletonBD db;
    Socket socketCentral;

    final String HOST = "127.0.0.1"; // Ip del equipo que contiene la central
    final int CENTRAL_PORT = 6900;  // este corresponde al puerto mediante la maquina virtual está escuchando

    public PuenteCentralSurtidor(){
        this.surtidores = new ArrayList<Socket>();
        this.listeners = new ArrayList<Sucursal>();
        this.db = SingletonBD.getInstance();
        this.socketCentral = null;
//        socketCentral = new Socket(HOST, CENTRAL_PORT);
//        System.out.println("Puente iniciado !!");
    }

    @Override
    public void run() {
        // cada vez que se inicie este thread, se buscará una conexión con el servidor de forma infinita
        while(true){
            try {
                socketCentral = new Socket(HOST, CENTRAL_PORT);
                System.out.println("--> Puente iniciado: conexión establecida con servidor Central");

                DataInputStream inCentral = new DataInputStream(socketCentral.getInputStream());
//                DataOutputStream outCentral = new DataOutputStream(socketCentral.getOutputStream());
                String message;
                String reporte;

                // si se logra establecer la conexión, se entra a otro ciclo que mantendrá su ejecución mientras
                // la conexión con el servidor esté vigente.
                while(!socketCentral.isClosed()){
                    message = inCentral.readUTF();

                    System.out.println("message readed en admin surtidores: " + message);

                    //enviar broadcast para actualizar surtidores
                    sendBroadcast(message);
                }
                db.escribirBD();

            } catch (IllegalMonitorStateException e){
                System.out.println("ERORR");
//                e.printStackTrace();
                freeze(3000);

            }catch (IOException e){
                // entonces, cada vez que el servidor se desconecte, se hará
                // una pausa de cinco segundos y volverá a intentar la conexión con el servidor
                System.out.println("-> ERROR: server Central desconectado (PuenteSucursalSurtidor): ");
                System.out.println("reconectando...");
//                e.printStackTrace();
                freeze(3000);
            }
        }
    }

    private void freeze(long millis){
        try {
            this.sleep(millis);
        } catch (InterruptedException ex) {
            System.out.println("-> interruption error");
            ex.printStackTrace();
        }
    }

    private void sendBroadcast(String message) throws IOException {
        DataOutputStream out = null;
        for(Socket surtidor : this.surtidores){
            System.out.println("...actualizando!: " + message);
            out = new DataOutputStream(surtidor.getOutputStream());
            out.writeUTF(message);
        }
    }

    public Boolean addSurtidor(Socket nuevoSurtidor) throws IOException {
        Sucursal th = null;
        if(nuevoSurtidor == null)           return false;

        if(this.socketCentral == null)      th = new Sucursal(nuevoSurtidor);
        else                                   th = new Sucursal(this.socketCentral, nuevoSurtidor);

        th.start();
        listeners.add(th); // Se crea un hilo por cada surtidor el cual escuchará por mensajes
        this.surtidores.add(nuevoSurtidor);
        return true;
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }
}
