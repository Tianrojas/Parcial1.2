package edu.eci.arsw.math;


/**
 * Se implementó esta clase que extiende de hilos, la cual es la que realiza los cálculos independientes de los dígitos
 * de Pi de cada sub intervalo, esta posee asigna en el método run() la cadena resultada que retornara por medio del
 * método getSubChain()
 */
class Calculator extends Thread {
    private int start;
    private int count;
    private byte[] subChain;
    private final Object lock;

    /**
     * Construye una instancia de la clase Calculador.
     *
     * @param start El inicio del intervalo para los cálculos.
     * @param count El número de dígitos a calcular.
     * @param lock Un objeto de bloqueo utilizado para la sincronización.
     */
    public Calculator(int start, int count, Object lock) {
        this.start = start;
        this.count = count;
        this.lock = lock;
    }

    /**
     * Ejecuta los cálculos de los dígitos de Pi dentro del intervalo especificado.
     * Los resultados se almacenan en la cadena subChain.
     */
    @Override
    public void run() {
        //Calculo sin pausa <-------------------------------------------------------------------------------------------
        //subChain = PiDigits.getDigits(start, count); //Des comentar
        //Calculo con pausa cada 5seg <---------------------------------------------------------------------------------
        subChain = PiDigits.getDigits(start, count, lock, this);
    }

    /**
     * Obtiene la cadena de dígitos calculada en el subintervalo.
     *
     * @return Un array de bytes que contiene los dígitos calculados en el subintervalo.
     */
    public byte[] getSubChain() {
        return subChain;
    }
}
