package threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SurtidorListener extends Thread {
    private Socket socketCentral;
    private Socket socketSurtidor;

    public SurtidorListener(Socket socketCentral, Socket socketSurtidor){
        this.socketCentral = socketCentral;
        this.socketSurtidor = socketSurtidor;
    }

    @Override
    public void run() {
        try {
            DataInputStream inSurtidor = new DataInputStream(socketSurtidor.getInputStream());
            DataOutputStream outSurtidor = new DataOutputStream(socketSurtidor.getOutputStream());
            DataInputStream inCentral = new DataInputStream(socketCentral.getInputStream());
            DataOutputStream outCentral = new DataOutputStream(socketCentral.getOutputStream());
            String message;
            while((!socketSurtidor.isClosed()) && (!socketCentral.isClosed())){
                message = inSurtidor.readUTF();
                if(validateMessage(message)){
                    //TODO: actualizar la base de datos aquí
                    outCentral.writeUTF(message);
                    if(inCentral.readUTF().equals("ok"))     outSurtidor.writeUTF("ok");
                    else                                     outSurtidor.writeUTF("err");
                }else{
                    outSurtidor.writeUTF("err");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }
}
