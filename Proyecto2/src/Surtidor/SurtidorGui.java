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
    private Boolean running;
    private DataOutputStream out;
    private BDsurtidor basedatos;

    /**
     * Constructor de la interfaz de surtidor
     * @param sucursalSocket
     * @param gasolina93
     * @param gasolina95
     * @param gasolina97
     * @param diesel
     * @param kerosene
     */
    public SurtidorGui(Socket sucursalSocket){
        this.sucursalSocket = sucursalSocket;
        this.running = true;
        this.out = null;
        this.basedatos=new BDsurtidor();
    }
    public SurtidorGui(){
        System.out.println("--> iniciando Surtidor en modo autónomo...");
        this.sucursalSocket = null;
        this.out = null;
        this.running = true;
        this.basedatos=new BDsurtidor();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int option;
        String tipo;
        String cantidad;
        String modo = "";

        while (this.running) {
            try {
                if(this.sucursalSocket!=null && this.out == null) {
                    this.out = new DataOutputStream(sucursalSocket.getOutputStream());
                    modo = "";
                } else if(this.sucursalSocket == null && this.out == null){
                    modo = " (modo autónomo)";
                }else{
                    modo = "";
                }
                System.out.println(":::: NUEVA VENTA"+modo+" ::::");
                System.out.println("1 - bencina 93");
                System.out.println("2 - bencina 95");
                System.out.println("3 - bencina 97");
                System.out.println("4  - diesel");
                System.out.println("5  - kerosene");

                System.out.print("ingrese una opción: ");

                option = this.running?scanner.nextInt() :-1;
                testConnection();

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
                testConnection();


                Boolean response = this.running?nuevaVenta("vnt-" + tipo + "-" + cantidad,tipo,Integer.parseInt(cantidad)):false;
            } catch (IOException e) {
                System.out.println("--> SurtidorGui: se perdió la conexión con Sucursal.");
                this.sucursalSocket = null;
                this.out = null;
            }
        }
        System.out.println("--> terminando proceso gui");
    }

    private void testConnection(){
        try {
            if(this.out != null) {
                this.out.writeUTF("testing");
                this.out.writeUTF("testing");
            }
        } catch (IOException e) {
            System.out.println("--> ERROR: se perdió la conexión con Sucursal..\n-->entrando en modo autónomo...");
            this.sucursalSocket = null;
            this.out = null;
        }
    }

    private Boolean nuevaVenta(String message, String tipo, int cantidad) throws IOException {
        if(this.sucursalSocket!=null && this.out == null) {
            this.out = new DataOutputStream(sucursalSocket.getOutputStream());
        }
        if(this.out != null) {
            System.out.println("--> SurtidorGui: venta exitosa, enviando venta a Sucursal..");
                basedatos.instertarVenta(cantidad, 1, tipo,true);
            out.writeUTF(message);
        }else{
            basedatos.instertarVenta(cantidad, 1, tipo, false);
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
        System.out.println("\n--> SucursalGui: Saliendo del modo autónomo...");
    }
    public BDsurtidor getBD(){
        return basedatos;
    }
}
