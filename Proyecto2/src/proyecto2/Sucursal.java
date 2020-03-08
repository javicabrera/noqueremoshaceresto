package proyecto2;

import threads.AdminSurtidores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
    public static int surtidores=0;
    public static int[] precios= new int[5];
    public static int factorUtilidad;
    public static void main(String[] args) throws UnknownHostException, IOException {
        InetAddress ip = InetAddress.getByName("localhost");
        MulticastSocket socket = new MulticastSocket(10500);   
        socket.joinGroup(ip);
        while(true){
            //RECIBE EL VALOR A MODIFICAR PRECIO
            byte[] bufferEntrada = new byte[1];
            DatagramPacket msjEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length); 
            socket.receive(msjEntrada);
            //SE SEPARA LOS DATOS QUE RECIBE SUPONIENDO QUE RECIBE
            //NUEVO.PRECIO TIPO.GASOLINA
            //SEPARADOS POR UN ESPACIO
            String datos = new String(bufferEntrada);   
            String datos2[]= datos.split(" ");
            
            int nuevoPrecio= Integer.valueOf(datos2[0]);
            int y= Integer.valueOf(datos2[1]);           
            modificarPrecios(nuevoPrecio,y);
            //SE ENVIA EL MENSAJE AL CLIENTE CON EL CONTADOR ACTUALIZADO
            byte[] bufferSalida = (String.valueOf(getContador())).getBytes();
            DatagramPacket msjSalida = new DatagramPacket(bufferSalida, bufferSalida.length, msjEntrada.getAddress(), msjEntrada.getPort());
            socket.send(msjSalida);        
        }                
    }
    /***
     * 
     * @param x
     * @param y un ID para saber cual tipo de combustible modificara su precio
     */
    private static void modificarPrecios(int x, int y) {
        precios[y]=x;     
    }

    public static void main(String[] args) throws IOException {
        final int PORT = 4200;
        ArrayList<Socket> surtidores = new ArrayList<Socket>();
        ServerSocket server = null;
        Socket sc = null;
        AdminSurtidores admin;

        try{
            server = new ServerSocket(PORT);
            System.out.println("servidor sucursal iniciado. Esperando por surtidores...");
            admin = new AdminSurtidores();
            admin.start();

            while(!server.isClosed()){
                if((sc = server.accept()) == null) break;  // esta sentencia hace que el programa espere por un nuevo cliente (surtidor)
                System.out.println("surtidor conectado");
                admin.addSurtidor(sc);
                surtidores.add(sc);
            }
            sc.close();
            System.out.println("...saliendo !");
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
