package threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SurtidorGui extends Thread {

    private Socket sucursalSocket;
    private double gasolina93;
    private double gasolina95;
    private double gasolina97;
    private double diesel;
    private double kerosene;
    private Boolean disponible;

    public SurtidorGui(Socket sucursalSocket, double gasolina93, double gasolina95, double gasolina97, double diesel, double kerosene){
        this.sucursalSocket = sucursalSocket;
        this.gasolina93 = gasolina93;
        this.gasolina95 = gasolina95;
        this.gasolina97 = gasolina97;
        this.diesel = diesel;
        this.kerosene = kerosene;
        this.disponible = true;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            DataInputStream in = new DataInputStream(sucursalSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(sucursalSocket.getOutputStream());

            while(!this.sucursalSocket.isClosed()){
                TimeUnit.SECONDS.sleep(10);
                Boolean response = nuevaVenta(in, out);
            }


        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private Boolean nuevaVenta(DataInputStream in, DataOutputStream out) throws IOException {
        this.disponible = false;
        out.writeUTF("vnt-kerosene-6699");
        if(in.readUTF().equals("ok")) return true;
        return false;
    }

    // Actualizar precios!!

    public Socket getSucursalSocket() {
        return sucursalSocket;
    }

    public void setSucursalSocket(Socket sucursalSocket) {
        this.sucursalSocket = sucursalSocket;
    }

    public double getGasolina93() {
        return gasolina93;
    }

    public void setGasolina93(double gasolina93) {
        this.gasolina93 = gasolina93;
    }

    public double getGasolina95() {
        return gasolina95;
    }

    public void setGasolina95(double gasolina95) {
        this.gasolina95 = gasolina95;
    }

    public double getGasolina97() {
        return gasolina97;
    }

    public void setGasolina97(double gasolina97) {
        this.gasolina97 = gasolina97;
    }

    public double getDiesel() {
        return diesel;
    }

    public void setDiesel(double diesel) {
        this.diesel = diesel;
    }

    public double getKerosene() {
        return kerosene;
    }

    public void setKerosene(double kerosene) {
        this.kerosene = kerosene;
    }
}
