/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;


/**
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) {
        //Se comparan los tiempos de ejecución entre los dos tipos de ejecución
        long startTime1 = System.nanoTime();
        System.out.println(bytesToHex(PiDigits.getDigits(1, 30000)));
        long endTime1 = System.nanoTime();
        long duration1 = (endTime1 - startTime1) / 1_000_000;

        System.out.println(" ");

        //Para la opción de pausa cada 5 segundos, los hilos que van terminando su ejecución van saliendo del conteo
        //Por eso iteración tras iteración se ven menos hilos
        //Para jugar entre modalidades cambie la manera en que los hilos se ejecutan en el methods run() de la clase
        //Calculator, puede que con esta manera aumente el tiempo de compilación con hilos, ya que se estan haciendo pausas
        //recurrentes, para ver mas la diferencia entre la compilación sin hilos y la compilación con hilos sin pausas
        long startTime2 = System.nanoTime();
        System.out.println(bytesToHex(new ThHandler(1, 30000, 10).calculate()));
        long endTime2 = System.nanoTime();
        long duration2 = (endTime2 - startTime2) / 1_000_000;

        System.out.println("Tiempo de compilación sin hilos: " + duration1 + " ms");
        System.out.println("Tiempo de compilación con hilos: " + duration2 + " ms");
    }


    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }

}
