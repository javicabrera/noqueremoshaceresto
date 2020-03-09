package threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



// Esta clase debería emitir ls reportes??



public class SucursalListener extends Thread {
    private Socket sucursalSocket;
    private double total93;
    private double total95;
    private double total97;
    private double totalDiesel;
    private double totalKerosene;

    public SucursalListener(Socket sucursalDocket, double total93, double total95, double total97, double totalDiesel, double totalKerosene){
        this.sucursalSocket = sucursalDocket;
        this.total93 = total93;
        this.total95 = total95;
        this.total97 = total97;
        this.totalDiesel = totalDiesel;
        this.totalKerosene = totalKerosene;
    }

    public SucursalListener(Socket sucursalDocket){
        this.sucursalSocket = sucursalDocket;
        this.total93 = 0;
        this.total95 = 0;
        this.total97 = 0;
        this.totalDiesel = 0;
        this.totalKerosene = 0;
    }

    @Override
    public void run() {
        String message;
        try {
            DataInputStream in = new DataInputStream(this.sucursalSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(this.sucursalSocket.getOutputStream());

            while(!this.sucursalSocket.isClosed()){
                // Se espera por un mensaje desde el socket
                message = in.readUTF();
                // Si el mensaje es válido, se actualiza el valor de la gasolina que corresponda
                // Sino, se contesta con un mensaje de error
                if(validateMessage(message)){
                    System.out.println("recibiento desde central para actualizar: " + message);
                    updateGas(message);
//                    out.writeUTF("ok");
                }else{
//                    out.writeUTF("err");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void updateGas(String message){
        String [] splitted = message.split("-");
        String kindOf = splitted[1];
        int newTotal = Integer.valueOf(splitted[2]);

        switch(kindOf){
            case "93": this.setTotal93(newTotal);
            break;
            case "95": this.setTotal95(newTotal);
            break;
            case "97": this.setTotal97(newTotal);
            break;
            case "diesel": this.setTotalDiesel(newTotal);
            break;
            case "kerosene": this.setTotalKerosene(newTotal);
            break;
        }
    }

    private Boolean validateMessage(String message){
        return (message!=null);
    }

    public double getTotal93() {
        return total93;
    }

    public void setTotal93(double total93) {
        this.total93 = total93;
    }

    public double getTotal95() {
        return total95;
    }

    public void setTotal95(int total95) {
        this.total95 = total95;
    }

    public double getTotal97() {
        return total97;
    }

    public void setTotal97(int total97) {
        this.total97 = total97;
    }

    public double getTotalDiesel() {
        return totalDiesel;
    }

    public void setTotalDiesel(int totalDiesel) {
        this.totalDiesel = totalDiesel;
    }

    public double getTotalKerosene() {
        return totalKerosene;
    }

    public void setTotalKerosene(int totalKerosene) {
        this.totalKerosene = totalKerosene;
    }
}
