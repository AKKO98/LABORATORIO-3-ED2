/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab.ed2;

import java.io.Serializable;
import java.util.Stack;

/**
 *
 * @author bazas
 */
public class Sorter implements Serializable{
    private static final long serialVersionUID = 1L;
    private volatile int[] array;           // Array a ordenar
    private volatile boolean finished = false; // Estado del algoritmo
    private long acomulatedTime = 0;

    // Variables de control para el algoritmo iterativo
    private Stack<int[]> stack = new Stack<>(); // Pila para manejar particiones
    private long startTime;                    // Tiempo inicial de ejecución
    private long timeLimitMillis;              // Límite de tiempo en milisegundos

    public Sorter(int[] array) {
        this.array = array;
        finished = false;
    }
    
    public long getAcomulatedTime() {
        return acomulatedTime;
    }

    public void startQuickSort(int t) {
        // Inicializa el tiempo límite
        this.timeLimitMillis = t * 1000;
        this.startTime = System.currentTimeMillis();

        // Si la pila está vacía, inicia la partición completa
        if (stack.isEmpty()) {
            stack.push(new int[] { 0, array.length - 1 });
        }

        // Ejecuta el QuickSort iterativo
        while (!stack.isEmpty()) {
            if ((System.currentTimeMillis() - startTime) >= timeLimitMillis) {
                // Si se alcanza el tiempo límite, pausa
                acomulatedTime =+ t * 1000;
                break;
            }

            int[] range = stack.pop();
            int low = range[0];
            int high = range[1];

            if (low < high) {
                int pivotIndex = partition(low, high);

                // Agrega las nuevas particiones a la pila
                stack.push(new int[] { low, pivotIndex - 1 });
                stack.push(new int[] { pivotIndex + 1, high });
            }
        }

        if (stack.isEmpty()) {
            finished = true; // Marca como terminado si la pila está vacía
        }
    }
    
    public void resumeQuickSort() {
        startQuickSort((int) timeLimitMillis / 1000);
    }
    
    public boolean isFinished() {
        return finished;
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public int[] getArray() {
        return array;
    }

}