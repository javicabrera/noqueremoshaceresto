package Central;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

// Esta clase se encarga de la gestión de las sucursales y de mostrar
// la interfaz gráfica (a sabiendas de que es una mala práctica de programación)
// en un nuevo hilo de ejecución

public class CentralGui extends Thread {
    private ArrayList<Socket> sucursales;
    private ServerSocket server;
    private Scanner scanner;
    final int MAX_INTENTOS = 50;

    public CentralGui(ServerSocket server){
        this.server = server;
        this.sucursales = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        int option;
        while(true){
            System.out.println("1 - actualizar precio");
            System.out.println("2 - mostrar reportes");
            System.out.println("3 - salir");
            System.out.print("Ingrese una opción: ");
            option = scanner.nextInt();

            switch (option){
                case 1: actualizarPrecio(this.sucursales);
                    break;
                case 2: mostrarReporte(this.sucursales);
                    break;
                case 3: closeServer(this.server);
                    break;
                default: break;
            }
            if(option == 2) break;
        }
    }

    private void mostrarReporte(ArrayList<Socket> sucursales){
        try {
            for(Socket sucursal : sucursales){
                DataInputStream in = new DataInputStream(sucursal.getInputStream());
                DataOutputStream out = new DataOutputStream((sucursal.getOutputStream()));

                out.writeUTF("rpt");
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeServer(ServerSocket server){
        try {
            for(Socket sucursal : this.sucursales){
                System.out.println("cerrando socket desde central en puerto " + sucursal.getPort());
                sucursal.close();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarPrecio(ArrayList<Socket> sucursales) {
        String tipoCombustible = "";
        double nuevoPrecio = 0.0;
        int option;

        System.out.println("1 - bencina 93");
        System.out.println("2 - bencina 95");
        System.out.println("3 - bencina 97");
        System.out.println("4 - diesel");
        System.out.println("5 - kerosene");
        System.out.print("ingrese una opción: ");
        option = scanner.nextInt();

        switch (option){
            case 1: tipoCombustible = "93";

                break;
            case 2: tipoCombustible = "95";
                break;
            case 3: tipoCombustible = "97";
                break;
            case 4: tipoCombustible = "diesel";
                break;
            case 5: tipoCombustible = "kerosene";
                break;
            default: break;
        }

        System.out.print("ingrese nuevo precio: ");
        nuevoPrecio = scanner.nextDouble();

        sendBroadcast("act-" + tipoCombustible + "-" + nuevoPrecio, sucursales);
    }

    private void sendBroadcast(String message, ArrayList<Socket> sucursales){
        int i = 0;
        try {
            for(Socket sucursal : sucursales){
                DataOutputStream out = new DataOutputStream(sucursal.getOutputStream());
                System.out.println("Enviando actualización !");
                out.writeUTF(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addSocket(Socket socket) {
        System.out.println("CENTRAL: nueva conexion a sucursal!");
        return sucursales.add(socket);
    }
}
