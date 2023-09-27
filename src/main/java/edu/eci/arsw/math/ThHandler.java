package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * La clase ThHandler se encarga de coordinar múltiples hilos (Calculadores) para calcular los dígitos de Pi en paralelo
 * y luego combinar los resultados en un solo arreglo de bytes.
 */
public class ThHandler {
    private int start;
    private int count;
    private int numTh;
    List<Calculator> il;
    private final Object lock = new Object();
    private Thread waitForEnterThread;

    /**
     * Construye una instancia de la clase ThHandler.
     *
     * @param start  El inicio del intervalo para los cálculos.
     * @param count  El número total de dígitos a calcular.
     * @param numTh  El número de hilos (Calculadores) a utilizar para la computación paralela.
     */
    public ThHandler(int start, int count, int numTh) {
        this.start = start;
        this.count = count;
        this.numTh = numTh;
        waitForEnterThread = new Thread(this::waitForEnter); //crea un nuevo hilo que se ejecutará en paralelo con el resto del programa.
        waitForEnterThread.start();// Esto es útil para tareas que pueden ejecutarse en segundo plano sin bloquear la ejecución otros hilos.
    }

    /**
     * Inicia un hilo para esperar la entrada del usuario y permitir la sincronización manual.
     */
    private void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            scanner.nextLine();
            synchronized (lock) {
                lock.notifyAll(); //Se utiliza el mismo Lock para todos los hilos
            }
        }
    }

    /**
     * Calcula los dígitos de Pi en paralelo utilizando múltiples hilos (Calculadores).
     *
     * @return Un array de bytes que contiene los dígitos calculados.
     */
    public byte[] calculate(){
        il = generateList(); //Genera la lista de hilos
        for (Calculator cal : il) {
            cal.start();  //Corre los hilos
        }
        for (Calculator cal : il) {
            try {
                cal.join(); //Espera a que todos los hilos terminen su ejecución para proceder a unir las sub cadenas
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        byte[] digits = new byte[count];
        int currentIndex = 0;

        for (Calculator cal : il) {
            for (int i = 0; i < cal.getSubChain().length; i++){
                digits[currentIndex++] = cal.getSubChain()[i]; //Union de los hilos
            }
        }
        waitForEnterThread.interrupt(); //Detengo el hilo que permite el bloqueo
        return digits;
    }

    /**
     * Genera una lista de Calculadores para dividir el trabajo en subintervalos.
     *
     * @return Una lista de Calculadores.
     */
    public List<Calculator> generateList() {
        List<Calculator> il = new ArrayList<>();
        int subintervalLength = count / numTh;
        int dis = count;
        for (int i = 0; i < numTh; i++) {
            int subintervalStart = start + i * subintervalLength;
            dis = dis-subintervalLength;
            if (i == numTh-1 && dis>0){ //Se controla el ultimo sub intervalo para que tenga en cuenta todo el intervalo
                //Al mismo tiempo que entre ellos son mutuamente excluyentes
                il.add(new Calculator(subintervalStart, subintervalLength+dis,lock)); //Se crean todos los hilos con el mismo LOCK
                break;
            }
            il.add(new Calculator(subintervalStart, subintervalLength,lock)); //Se crean todos los hilos con el mismo LOCK
        }
        return il;
    }
}

