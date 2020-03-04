package proyecto2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Javiera Cabrera
 */
public class Sucursal {
    private ArrayList<Surtidor> s;
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket sc = null;
        final int PORT = 80;
        DataInputStream in;
        DataOutputStream out;
        Scanner scanner = new Scanner(System.in);
        int option;

        try{
            server = new ServerSocket(PORT);
            System.out.println("server initialized");
            sc = server.accept();  // esta sentencia hace que el programa espere por un nuevo cliente (surtidor)
            System.out.println("client connected");
            in = new DataInputStream(sc.getInputStream()); // se usará para actualizar la cantidad de gasolina vendida
            out = new DataOutputStream(sc.getOutputStream());

            while(true){
                System.out.println("1 - actualizar precio");
                System.out.println("2 - salir");
                System.out.print("Ingrese una opción: ");
                option = scanner.nextInt();

                switch (option){
                    case 1: actualizarPrecio(out, in, scanner);
                        break;
                    case 2: break;
                    default: break;
                }

                if(option == 2) break;

                System.out.println("test !");

            }
            sc.close();
            System.out.println("...cerrando server !");
            scanner.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void actualizarPrecio(DataOutputStream out, DataInputStream in,  Scanner scanner) {
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
            out.writeUTF(String.valueOf("actualizar-" + tipoCombustible + "-" + nuevoPrecio));
            String message = in.readUTF();
            System.out.println("incomming message: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
