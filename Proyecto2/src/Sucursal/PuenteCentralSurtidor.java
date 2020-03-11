package Sucursal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

// Ésta clase atiende losmensajes provenientes de los surtidores
// y los gestiona. Además crea un hilo que quda a la escucha de las
// ventas de los surtidores.

public class PuenteCentralSurtidor extends Thread {
    ArrayList<Socket> surtidores;
    ArrayList<Sucursal> listeners;
    SingletonBD db;
    Socket socketCentral;
    final String HOST = "35.247.228.145"; // Ip pública de la máquina virtual
    final int CENTRAL_PORT = 80;  // este corresponde al puerto mediante la maquina virtual está escuchando

    public PuenteCentralSurtidor() throws IOException {
        this.surtidores = new ArrayList<Socket>();
        this.listeners = new ArrayList<Sucursal>();
        this.db = SingletonBD.getInstance();
        socketCentral = new Socket(HOST, CENTRAL_PORT);
        System.out.println("Puente iniciado !!");
    }

    @Override
    public void run() {
        try {
            DataInputStream inCentral = new DataInputStream(socketCentral.getInputStream());
            DataOutputStream outCentral = new DataOutputStream(socketCentral.getOutputStream());
            String message;
            String reporte;
            while(!socketCentral.isClosed()){
                message = inCentral.readUTF();
                System.out.println("messae readed en admin surtidores: " + message);

                if(message.equals("rpt")){
                    outCentral.writeUTF("~~~ MOSTRANDO REPORTE SUCURSAL X ~~~");
                    reporte = db.reportePorLitros(1);
                    outCentral.writeUTF(reporte);
                    reporte = db.reportePorLitros(2);
                    outCentral.writeUTF(reporte);
                    reporte = db.reportePorLitros(3);
                    outCentral.writeUTF(reporte);
                    reporte = db.reportePorLitros(4);
                    outCentral.writeUTF(reporte);
                    reporte = db.reportePorLitros(5);
                    outCentral.writeUTF(reporte);
                    outCentral.writeUTF("eof");
                }else{
                    //enviar broadcast para actualizar surtidores
                    sendBroadcast(message);
                }
            }

            db.escribirBD();

        } catch (IOException e){
            e.printStackTrace();
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
        if(nuevoSurtidor == null)
            return false;
        Sucursal th = new Sucursal(this.socketCentral, nuevoSurtidor);
        th.start();
        listeners.add(th); // Se crea un hilo por cada surtidor el cual escuchará por mensajes
        this.surtidores.add(nuevoSurtidor);
        return true;
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }
}
