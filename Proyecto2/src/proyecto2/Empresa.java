/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 *
 * @author Javiera Cabrera
 */
public class Empresa {
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        InetAddress ip = InetAddress.getByName("224.0.0.5");
        MulticastSocket socket = new MulticastSocket(10600);
        //EL SERVIDOR SE UNE AL GRUPO
        socket.joinGroup(ip);
        //SE RECIBE EL MENSAJE DE ENTRADA PARA CONOCER LA IP Y PUERTO DEL CLIENTE CONECTADO00
        byte[] bufferEntrada = new byte[1000];
        DatagramPacket msjEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);             
        socket.receive(msjEntrada);
        //SE LE PIDE AL USUARIO QUE ESCRIBA UN MENSAJE PARA ENVIAR A LOS DEMAS CLIENTES EN ESPERA
        Scanner s= new Scanner(System.in);
        //MENU MENU MENU CON WHILE INFINITO
        
        System.out.print("MENU : ");
        System.out.println("1.- Actualizar precio");
        System.out.println("2.- Generar reporte");
        int opcion= s.nextInt();
        String text= s.nextLine();
        //SE ENVIA EL MENSAJE A LOS DEMAS CLIENTES
        byte[] bufferSalida = text.getBytes();
        DatagramPacket msjSalida = new DatagramPacket(bufferSalida, bufferSalida.length, ip, msjEntrada.getPort() );
        socket.send(msjSalida);        
    }
}