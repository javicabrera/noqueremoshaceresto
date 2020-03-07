package threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class EmpresaGui extends Thread {
    private ArrayList<Socket> surtidores;
    private ServerSocket server;
    private Scanner scanner;

    public EmpresaGui(ServerSocket server){
        this.server = server;
        this.surtidores = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        int option;
        while(true){
            System.out.println("1 - actualizar precio");
            System.out.println("2 - salir");
            System.out.print("Ingrese una opción: ");
            option = scanner.nextInt();

            switch (option){
                case 1: actualizarPrecio(this.surtidores);
                    break;
                case 2: closeServer(this.server);
                    break;
                default: break;
            }

            if(option == 2) break;

        }
    }

    private void closeServer(ServerSocket server){
        try {
            server.close();
            System.out.println("...cerrando server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarPrecio(ArrayList<Socket> surtidores) {
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

        try {
            for(Socket surtidor : surtidores){
                System.out.println("actualizando el surtidor con puerto " + surtidor.getPort());
                DataInputStream in = new DataInputStream(surtidor.getInputStream());
                DataOutputStream out = new DataOutputStream(surtidor.getOutputStream());
                out.writeUTF(String.valueOf("actualizar-" + tipoCombustible + "-" + nuevoPrecio));
                String message = in.readUTF();
                System.out.println("incomming message: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addSocket(Socket socket) {
        System.out.println("nuevo socket!");
        return surtidores.add(socket);
    }
}
