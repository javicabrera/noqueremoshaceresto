package threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

// Ésta clase atiende losmensajes provenientes de los surtidores
// y los gestiona. Además crea un hilo que quda a la escucha de las
// ventas de los surtidores.

public class AdminSurtidores extends Thread {
    ArrayList<Socket> surtidores;
    ArrayList<SurtidorListener> listeners;
    Socket socketCentral;
    final String HOST = "35.247.228.145"; // Ip pública de la máquina virtual
    final int CENTRAL_PORT = 80;  // este corresponde al puerto mediante la maquina virtual está escuchando

    public AdminSurtidores() throws IOException {
        this.surtidores = new ArrayList<Socket>();
        this.listeners = new ArrayList<SurtidorListener>();
        socketCentral = new Socket(HOST, CENTRAL_PORT);
    }

    @Override
    public void run() {
        try {
            DataInputStream inCentral = new DataInputStream(socketCentral.getInputStream());
            DataOutputStream outCentral = new DataOutputStream(socketCentral.getOutputStream());
            String message;
            while(!socketCentral.isClosed()){
                message = inCentral.readUTF();
                System.out.println("messae readed !");
                if(validateMessage(message)){
                    //enviar broadcast
                    sendBroadcast(message);
                    outCentral.writeUTF("ok");
                }else{
                    System.out.println("ERROR!");
                    outCentral.writeUTF("err");
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendBroadcast(String message) throws IOException {
        DataOutputStream out = null;
        for(Socket surtidor : this.surtidores){
            System.out.println("...actualizando!");
            out = new DataOutputStream(surtidor.getOutputStream());
            out.writeUTF(message);
        }
    }

    public Boolean addSurtidor(Socket nuevoSurtidor){
        if(nuevoSurtidor == null)
            return false;
        SurtidorListener th = new SurtidorListener(this.socketCentral, nuevoSurtidor);
        th.start();
        listeners.add(th); // Se crea un hilo por cada surtidor el cual escuchará por mensajes
        this.surtidores.add(nuevoSurtidor);
        return true;
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }
}
