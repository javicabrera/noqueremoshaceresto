package Surtidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Clase que representa el menu e interacción con el usuario de un surtidor
 *
 * @author 56992
 */
public class SurtidorGui extends Thread {

    private Socket sucursalSocket;
    private double gasolina93;
    private double gasolina95;
    private double gasolina97;
    private double diesel;
    private double kerosene;
    private Boolean disponible;
    private Boolean running;
    private DataOutputStream out;

    /**
     * Constructor de la interfaz de surtidor
     * @param sucursalSocket
     * @param gasolina93
     * @param gasolina95
     * @param gasolina97
     * @param diesel
     * @param kerosene
     */
    public SurtidorGui(Socket sucursalSocket, double gasolina93, double gasolina95, double gasolina97, double diesel, double kerosene){
        this.sucursalSocket = sucursalSocket;
        this.gasolina93 = gasolina93;
        this.gasolina95 = gasolina95;
        this.gasolina97 = gasolina97;
        this.diesel = diesel;
        this.kerosene = kerosene;
        this.disponible = true;
        this.running = true;
        this.out = null;
    }
    public SurtidorGui(double gasolina93, double gasolina95, double gasolina97, double diesel, double kerosene){
        System.out.println("--> iniciando surtidor en modo autónomo...");
        this.sucursalSocket = null;
        this.out = null;
        this.gasolina93 = gasolina93;
        this.gasolina95 = gasolina95;
        this.gasolina97 = gasolina97;
        this.diesel = diesel;
        this.kerosene = kerosene;
        this.disponible = true;
        this.running = true;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int option;
        String tipo;
        String cantidad;

        try {
            //Mientras exista un socket de sucursal abierto se mostrara el menú
            //para vender combustible
            while (this.running) {
                if(this.sucursalSocket!=null && this.out == null) {
                    this.out = new DataOutputStream(sucursalSocket.getOutputStream());
                } else{
                    System.out.println("*** modo autónomo ***");
                }
                System.out.println("~~~ NUEVA VENTA ~~~");
                System.out.println("1 - bencina 93");
                System.out.println("2 - bencina 95");
                System.out.println("3 - bencina 97");
                System.out.println("4  - diesel");
                System.out.println("5  - kerosene");

                System.out.print("ingrese una opción: ");

                option = this.running?scanner.nextInt() :-1;

                switch (option) {
                    case 1:
                        tipo = "93";
                        break;
                    case 2:
                        tipo = "95";
                        break;
                    case 3:
                        tipo = "97";
                        break;
                    case 4:
                        tipo = "diesel";
                        break;
                    case 5:
                        tipo = "kerosene";
                        break;
                    default:
                        tipo = "kerosene";
                        break;
                }
                scanner.nextLine();
                System.out.print("Ingrese cantidad: ");
                cantidad = this.running?scanner.nextLine():"";

                Boolean response = this.running?nuevaVenta("vnt-" + tipo + "-" + cantidad, out):false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("--> terminando proceso gui");
    }

    private Boolean nuevaVenta(String message, DataOutputStream out) throws IOException {
        if(this.sucursalSocket!=null && this.out == null) {
            this.out = new DataOutputStream(sucursalSocket.getOutputStream());
        }
        this.disponible = false;
        if(out != null) {
            System.out.println("-->SurtidorGui: venta exitosa, enviando venta a Sucursal");
            out.writeUTF(message);
            // guardar aqui en la base de datos local
        }else{
            //solo guardar en base de datos local
            System.out.println("--> SurtidorGui: venta guardada solo en base de datos local.");
        }
        return true;
    }

    public void killThread(){
        this.running = false;
    }

    public Boolean isRunning(){
        return this.running;
    }

    // Actualizar precios!!

    public Socket getSucursalSocket() {
        return sucursalSocket;
    }

    public void setSucursalSocket(Socket sucursalSocket) {
        this.sucursalSocket = sucursalSocket;
        System.out.println("--> SucursalGui: Saliendo del modo autónomo...");
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
