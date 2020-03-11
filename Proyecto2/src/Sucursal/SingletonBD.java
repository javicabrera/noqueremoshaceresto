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
    private static final SingletonBD instance = new SingletonBD();

    public SingletonBD() {
        BD= new int[200][3];
        BD=leerBD(BD);
        contador=0;
    }

    public static SingletonBD getInstance(){
        return instance;
    }
    public int[][] leerBD(int[][] m) {
        File BD = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            BD= new File("Sucursal/SucursalBD.txt");
            fr= new FileReader(BD);
            br= new BufferedReader(fr);
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
        }catch (IOException e){
            e.printStackTrace();
        }
        return m;
    }
    public void escribirBD() throws IOException{
        FileWriter fw = new FileWriter("Sucursal/SucursalBD.txt");
        PrintWriter pw = new PrintWriter(fw);
        for(int i=0; i<BD.length; i++){
            pw.println(BD[i][0]+" "+BD[i][1]+" "+BD[i][2]);
        }
        fw.close();
    }

    public void añadirDato(int id, int tipo, int litros){
        System.out.println("from sb: AÑADIENDO DATO");
        BD[contador][0]=id;
        BD[contador][1]=tipo;
        BD[contador][2]=litros;
        contador++;
    }

    public String reportePorLitros(int tipo){
        switch (tipo)
        {
            case 1: return "Se han vendido " + contador(tipo)+" de combustible 93";
            case 2: return "Se han vendido "+ contador(tipo)+" de combustible 95";
            case 3: return "Se han vendido "+ contador(tipo)+" de combustible 97";
            case 4: return "Se han vendido "+ contador(tipo)+" de combustible Diesel";
            case 5: return "Se han vendido "+ contador(tipo)+" de combustible Kerosene";
            default: return "ERROR";
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
