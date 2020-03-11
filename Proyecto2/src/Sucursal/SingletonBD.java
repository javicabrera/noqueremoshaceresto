/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sucursal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Javiera Cabrera
 */
public class SingletonBD {
    
    public int[][] BD;
    public int contador;
    public SingletonBD(int n) throws IOException{
        BD= new int[200][3];
        BD=leerBD(BD);
        contador=0;
    }
    public int[][] leerBD(int[][] m) throws FileNotFoundException, IOException{
        File BD= new File("SucursalBD.txt");
        FileReader fr= new FileReader(BD);
        BufferedReader br= new BufferedReader(fr);
        String linea;
        int i=0;
        while((linea=br.readLine())!=null){
            String[] line= linea.split(" ");
            int id= Integer.valueOf(line[0]);
            int combustible= Integer.valueOf(line[1]);
            int litros= Integer.valueOf(line[2]);
            m[i][0]=id;
            m[i][1]=combustible;
            m[i][2]=litros;            
            i++;
        }
        contador=i;
        fr.close();
        return m;
    }
    public void escribirBD(int[][] m) throws IOException{
        FileWriter BD= new FileWriter("SucursalBD.txt");
        PrintWriter pw= new PrintWriter(BD);
        for(int i=0; i<m.length; i++){
            pw.println(m[i][0]+" "+m[i][1]+" "+m[i][2]);
        }
        BD.close();
    }
    public void añadirDato(int[] id, int[] tipo, int[] litros){
        BD[contador][0]=id;
        BD[contador][1]=tipo;
        BD[contador][2]=litros;
        contador++;
    }
    public void reportePorLitros(int tipo){
        switch (tipo)
        {
            case 1: 
                System.out.println("Se han vendido " + contador(tipo)+" de combustible 93");
                break;
            case 2:
                System.out.println("Se han vendido "+ contador(tipo)+" de combustible 95");
                break;
            case 3:
                System.out.println("Se han vendido "+ contador(tipo)+" de combustible 97");
                break;
            case 4:
                System.out.println("Se han vendido "+ contador(tipo)+" de combustible Diesel");
                break;
            case 5:
                System.out.println("Se han vendido "+ contador(tipo)+" de combustible Kerosene");
                break;
              
        }
    }    
    public int contador(int t){
        int litros=0;
        for (int i = 0; i < BD.length; i++) {
            if(t==BD[i][1]){
                litros+=BD[i][2];
            }
        }
        return litros;
    }
}
